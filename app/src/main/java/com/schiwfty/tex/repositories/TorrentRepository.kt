package com.schiwfty.tex.repositories

import com.pawegio.kandroid.i
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.confluence.Confluence.torrentRepo
import com.schiwfty.tex.models.ConfluenceInfo
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.retrofit.ConfluenceApi
import com.schiwfty.tex.utils.composeIo
import com.schiwfty.tex.utils.copyToTorrentDirectory
import com.schiwfty.tex.utils.getAsTorrent
import com.schiwfty.tex.utils.isValidTorrentFile
import okhttp3.ResponseBody
import org.apache.commons.io.IOUtils
import rx.Observable
import java.io.File
import java.io.FileInputStream


/**
 * Created by arran on 29/04/2017.
 */
class TorrentRepository(val confluenceApi: ConfluenceApi) : ITorrentRepository {

    override fun setupClientFromRepo(): Observable<ResponseBody> {
        return Observable.just({
            val torrentList = mutableListOf<File>()
            Confluence.torrentRepo.walkTopDown().iterator().forEach {
                if (it.isValidTorrentFile()) { torrentList.add(it) }
            }
            torrentList.toList()
        }.invoke())
                .composeIo()
                .flatMapIterable { it }
                .filter { it!=null }
                .map { Pair(it.getAsTorrent()?.info_hash, it) }
                .flatMap {
                    val hash = it.first
                    val file = it.second
                    if(hash==null) throw IllegalStateException("Hash cannot be null")
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
                    if(torrentInfo!=null) torrentList.add(torrentInfo)
                }
            }
            torrentList.toList()
        }.invoke())
                .composeIo()
    }

    override fun getTorrentFileData(hash: String, path: String): Observable<ResponseBody> {
        return confluenceApi.getFileDatda(hash, path)
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
}