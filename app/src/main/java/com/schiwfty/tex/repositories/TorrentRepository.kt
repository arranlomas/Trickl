package com.schiwfty.tex.repositories

import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.confluence.Confluence.torrentRepo
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.retrofit.ConfluenceApi
import com.schiwfty.tex.utils.composeIo
import com.schiwfty.tex.utils.getAsTorrent
import com.schiwfty.tex.utils.isValidTorrentFile
import rx.Observable
import java.io.File


/**
 * Created by arran on 29/04/2017.
 */
class TorrentRepository(val confluenceApi: ConfluenceApi) : ITorrentRepository {

    override fun getStatus(): Observable<String> {
        return confluenceApi.getStatus
                .composeIo()
                .map { it.string() }
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
        if(file.isValidTorrentFile()) return Observable.just(file.getAsTorrent())
        else return downloadTorrentInfo(hash)
    }

    override fun getAllTorrentsFromStorage(): Observable<List<TorrentInfo>?> {
        return Observable.just({
            val torrentInfoList = mutableListOf<TorrentInfo?>()
            val torrentInfoListNonNull = mutableListOf<TorrentInfo>()
            Confluence.torrentRepo.walkTopDown().iterator().forEach {
                if(it.isValidTorrentFile()) {
                    val torrentInfo: TorrentInfo? = it.getAsTorrent()
                    torrentInfoList.add(torrentInfo)
                }
            }

            //TODO remove this extra iteration
            torrentInfoList.forEach { if (it != null) torrentInfoListNonNull.add(it) }
            torrentInfoListNonNull.toList()
        }.invoke())
                .composeIo()

    }
}