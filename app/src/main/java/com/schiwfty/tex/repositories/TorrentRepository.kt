package com.schiwfty.tex.repositories

import android.util.Log
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.confluence.Confluence.torrentRepo
import com.schiwfty.tex.models.ConfluenceInfo
import com.schiwfty.tex.models.FileStatePiece
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.persistence.ITorrentPersistence
import com.schiwfty.tex.retrofit.ConfluenceApi
import com.schiwfty.tex.utils.*
import okhttp3.ResponseBody
import org.apache.commons.io.IOUtils
import rx.Observable
import rx.Subscription
import rx.subjects.PublishSubject
import java.io.File
import java.io.FileInputStream


/**
 * Created by arran on 29/04/2017.
 */
class TorrentRepository(val confluenceApi: ConfluenceApi, val torrentPersistence: ITorrentPersistence) : ITorrentRepository {

    override val torrentFileProgressSource: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    private val downloadMap = HashMap<TorrentFile, Subscription>()

    private var statusUpdateRunning = true

    private val statusThread = Thread({
        while (statusUpdateRunning) {
            val files = torrentPersistence.getDownloadFiles()
            var percentagesCompleted = 0
            files.forEach {
                getFileState(it)
                        .subscribe({
                            val (torrentFile, pieces) = it
                            var totalFileSize: Long = 0
                            var totalCompletedSize: Long = 0
                            pieces.forEach {
                                totalFileSize += it.bytes
                                if (it.complete) {
                                    totalCompletedSize += it.bytes
                                }
                            }
                            val percCompleted = (totalCompletedSize.toDouble() / totalFileSize.toDouble()) * 100.0
                            torrentFile.percComplete = Math.round(percCompleted).toInt()
                            torrentPersistence.saveTorrentFile(torrentFile)
                            percentagesCompleted++
                            if(percentagesCompleted == files.size) torrentFileProgressSource.onNext(true)
                            Log.v("HASH", torrentFile.torrentHash)
                            Log.v("PATH", torrentFile.getFullPath())
                            Log.v("PERC", percCompleted.toString())
                            Log.v("-----------", "-----------------------")
                        }, {
                            it.printStackTrace()
                        })
            }
            Thread.sleep(2000)
        }
    })

    init {
        statusThread.start()
    }

    override fun setupClientFromRepo(): Observable<ResponseBody> {
        return Observable.just({
            val torrentList = mutableListOf<File>()
            Confluence.torrentRepo.walkTopDown().iterator().forEach {
                if (it.isValidTorrentFile()) {
                    torrentList.add(it)
                }
            }
            torrentList.toList()
        }.invoke())
                .composeIo()
                .flatMapIterable { it }
                .filter { it != null }
                .map { Pair(it.getAsTorrent()?.info_hash, it) }
                .flatMap {
                    val hash = it.first
                    val file = it.second
                    if (hash == null) throw IllegalStateException("Hash cannot be null")
                    postTorrentFile(hash, file)
                }
    }

    override fun getStatus(): Observable<ConfluenceInfo> {
        return confluenceApi.getStatus
                .composeIo()
                .map { it }
    }

    override fun downloadTorrentInfo(hash: String): Observable<TorrentInfo?> {
        return confluenceApi.getInfo(hash)
                .composeIo()
                .map {
                    val file: File = File(torrentRepo, "$hash.torrent")
                    file.getAsTorrent()
                }
    }

    override fun getTorrentInfo(hash: String): Observable<TorrentInfo?> {
        val file: File = File(torrentRepo, "$hash.torrent")
        if (file.isValidTorrentFile()) return Observable.just(file.getAsTorrent())
        else return downloadTorrentInfo(hash)
    }

    override fun getAllTorrentsFromStorage(): Observable<List<TorrentInfo>> {
        return Observable.just({
            val torrentList = mutableListOf<TorrentInfo>()
            Confluence.torrentRepo.walkTopDown().iterator().forEach {
                if (it.isValidTorrentFile()) {
                    val torrentInfo = it.getAsTorrent()
                    if (torrentInfo != null) torrentList.add(torrentInfo)
                }
            }
            torrentList.toList()
        }.invoke())
                .composeIo()
    }

    override fun getTorrentFileData(hash: String, path: String): Observable<ResponseBody> {
        return confluenceApi.getFileData(hash, path)
    }

    override fun postTorrentFile(hash: String, file: File): Observable<ResponseBody> {
        if (!file.isValidTorrentFile()) throw IllegalStateException("File is not a valid torrent file")
        return Observable.just({
            file.copyToTorrentDirectory()
            val inputStream = FileInputStream(file)
            val bytes = IOUtils.toByteArray(inputStream)
            bytes
        }.invoke())
                .flatMap {
                    confluenceApi.postTorrent(hash, it)
                }
                .composeIo()
    }

    override fun getFileState(torrentFile: TorrentFile): Observable<Pair<TorrentFile, List<FileStatePiece>>> {
        val torrentFileObs = Observable.just(torrentFile)
        return Observable.zip(confluenceApi.getFileState(torrentFile.torrentHash, torrentFile.getFullPath()), torrentFileObs, {
            list, torrentFile ->
            Pair(torrentFile, list)
        })
                .composeIo()
    }

    override fun getDownloadingFilesFromPersistence(): Observable<List<TorrentFile>> {
        return Observable.just(torrentPersistence.getDownloadFiles())
    }

    override fun addFileForDownload(torrentFile: TorrentFile) {
        torrentPersistence.saveTorrentFile(torrentFile)
    }

    override fun startFileDownloading(torrentFile: TorrentFile) {
        val subscription = confluenceApi.getFileData(torrentFile.torrentHash, torrentFile.getFullPath())
                .composeIo()
                .map {
                    torrentPersistence.getDownloadingFile(torrentFile.torrentHash, torrentFile.getFullPath())
                }.subscribe({
            Log.v("Got bytes for file", torrentFile.getFullPath())
        }, {
            it.printStackTrace()
        })

        downloadMap.put(torrentFile, subscription)
    }
}