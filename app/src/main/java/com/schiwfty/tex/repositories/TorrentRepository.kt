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
import rx.subjects.PublishSubject
import java.io.File
import java.io.FileInputStream


/**
 * Created by arran on 29/04/2017.
 */
class TorrentRepository(val confluenceApi: ConfluenceApi, val torrentPersistence: ITorrentPersistence): ITorrentRepository {

    override val torrentFileProgressSource: PublishSubject<List<TorrentFile>> = PublishSubject.create<List<TorrentFile>>()

    private var statusUpdateRunning = true

    private val statusThread = Thread({
        while (statusUpdateRunning) {
                        val files = torrentPersistence.getDownloadFiles()
                        files.forEach {
                            val file = it
                            getFileState(it.torrentHash, it.getFullPath())
                                    .subscribe({
                                        Log.v ("SIZE", "${it.size}")
                                        var totalFileSize: Long = 0
                                        var totalCompletedSize: Long = 0
                                        it.forEach {
                                            totalFileSize += it.bytes
                                            if (it.complete) {
                                                totalCompletedSize += it.bytes
                                            }
                                        }
                                        val percCompleted = (totalCompletedSize.toDouble() / totalFileSize.toDouble()) * 100.0
                                        file.percComplete = percCompleted.toInt()
                                        torrentPersistence.saveTorrentFile(file)
                                        Log.v  ( "HASH", file.torrentHash)
                                        Log.v  ( "PATH", file.getFullPath())
                                        Log.v  ( "PERC", percCompleted.toString() )
                                        Log.v  ( "-----------","-----------------------" )
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

    override fun getFileState(hash: String, filePath: String): Observable<List<FileStatePiece>> {
        return confluenceApi.getFileState(hash, filePath)
                .composeIo()
    }

    override fun getDownloadingFilesFromPersistence(): Observable<List<TorrentFile>> {
        return Observable.just(torrentPersistence.getDownloadFiles())
    }

    override fun addTorrentForDownload(torrentFile: TorrentFile) {
        torrentPersistence.saveTorrentFile(torrentFile)
    }
}