package com.schiwfty.tex.repositories

import rx.Observable

/**
 * Created by arran on 29/04/2017.
 */
interface ITorrentRepository {
    fun getStatus(): Observable<String>
}