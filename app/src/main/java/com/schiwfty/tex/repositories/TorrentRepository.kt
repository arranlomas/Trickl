package com.schiwfty.tex.repositories

import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.retrofit.ConfluenceApi
import com.schiwfty.tex.utils.Constants
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

    override fun getTorrentInfo(hash: String): Observable<TorrentInfo> {
        return confluenceApi.getInfo(hash)
                .composeIo()
                .map {
                    it.bytes()
                }
                .map {
                    val file: File = File(Constants.torrentRepo, "$hash.torrent")
                    file.createNewFile()
                    file.writeBytes(it)
                    file
                }
                .map {
                    it.getAsTorrent()
                }
    }
}