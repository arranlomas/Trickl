package com.schiwfty.tex.repositories

import com.schiwfty.tex.retrofit.ConfluenceApi
import com.schiwfty.tex.utils.composeIo
import com.schiwfty.tex.utils.composeIoWithRetry
import rx.Observable


/**
 * Created by arran on 29/04/2017.
 */
class TorrentRepository(val confluenceApi: ConfluenceApi) : ITorrentRepository {


    override fun getStatus(): Observable<String> {
        return confluenceApi.getStatus
                .composeIo()
                .map { it.string() }
    }

    override fun getTorrentInfo(hash: String): Observable<String> {
        return confluenceApi.getInfo(hash)
                .composeIo()
                .map { it.toString() }
    }
}