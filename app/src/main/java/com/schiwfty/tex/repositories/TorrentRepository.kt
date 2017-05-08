package com.schiwfty.tex.repositories

import com.schiwfty.tex.confluence.Confluence.torrentRepo
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.retrofit.ConfluenceApi
import com.schiwfty.tex.utils.composeIo
import com.schiwfty.tex.utils.getAsTorrent
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

    override fun downloadTorrentInfo(hash: String): Observable<TorrentInfo> {
        return confluenceApi.getInfo(hash)
                .composeIo()
                .map {
                    //HERE TO GET THE INFO IF WE WANT IT
//                    val infoFile: File = File(torrentRepo, "$hash.info")
//                    infoFile.createNewFile()
//                    infoFile.writeBytes(it.bytes())
                    val file: File = File(torrentRepo, "$hash.torrent")
                    file.getAsTorrent()
                }
    }

    override fun getTorrentInfoFromCache(hash: String): Observable<TorrentInfo> {
        val file: File = File(torrentRepo, "$hash.torrent")
        if(file.exists()) return Observable.just(file.getAsTorrent())

        return downloadTorrentInfo(hash)
    }
}