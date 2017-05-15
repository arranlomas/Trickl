package com.schiwfty.tex.repositories

import com.pawegio.kandroid.v
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.confluence.Confluence.torrentRepo
import com.schiwfty.tex.models.ConfluenceInfo
import com.schiwfty.tex.models.FileStatePiece
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.models.TorrentInfo
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
class TorrentRepository(val confluenceApi: ConfluenceApi) : ITorrentRepository {

    override val torrentFileProgressSource: PublishSubject<List<Triple<String, String, Int>>> = PublishSubject.create<List<Triple<String, String, Int>>>()

    private var statusUpdateRunning = true

    private val statusThread = Thread({
        while (statusUpdateRunning) {
            getPercentagesFromDownloadingFiles()
                    .subscribe({
                        torrentFileProgressSource.onNext(it)
                    }, {
                        /*swallow the error*/
                        it.printStackTrace()
                    },{
                        v { "yay" }
                    })
            Thread.sleep(5000)
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
                .composeIo()
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

    override fun getPercentagesFromDownloadingFiles(): Observable<List<Triple<String, String, Int>>> {
        return getAllTorrentInfos()
                .flatMap { getAllTorrentFiles() }
                .flatMapIterable { it }
                .flatMap {
                    val hash = it.first
                    val path = it.second.getFullPath()
                    getFileState(hash, path).map { Triple(hash, path, it) }
                }
                .map {
                    val hash = it.first
                    val path = it.second

                    var totalFileSize: Long = 0
                    var totalCompletedSize: Long = 0
                    it.third.forEach {
                        totalFileSize += it.bytes
                        if(it.complete){
                            totalCompletedSize+=it.bytes
                        }
                    }
                    v("FILE NAME:               $path   ||  $hash")
                    v("TOTAL FILE SIZE:         $totalFileSize")
                    v("TOTAL COMPLETED SIZE:    $totalCompletedSize")
                    val percCompleted = (totalCompletedSize.toDouble()/totalFileSize.toDouble()) *100.0
                    v("PERC COMPLETED:          $percCompleted%")
                    v("--------------------------------------------")
                    Triple(hash, path, Math.round(percCompleted).toInt())
                }
                .toList()
    }

    private fun getAllTorrentFiles(): Observable<List<Pair<String, TorrentFile>>> {
        return getAllTorrentInfos()
                .flatMapIterable { it.map { Pair(it.info_hash, it) } }
                .flatMapIterable {
                    val hash = it.first
                    it.second.fileList.map { Pair(hash, it) }
                }
                .toList()
                .map { it }

    }

    private fun getAllTorrentInfos(): Observable<List<TorrentInfo>> {
        return getStatus()
                .flatMapIterable { it.torrentList }
                .flatMap { getTorrentInfo(it.infoHash) }
                .filter { it != null }
                .map { if (it == null) throw NullPointerException() else it }
                .map { it }
                .toList()
                .map { it }
    }

}