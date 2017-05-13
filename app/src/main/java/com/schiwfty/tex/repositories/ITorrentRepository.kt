package com.schiwfty.tex.repositories

import com.schiwfty.tex.models.TorrentInfo
import okhttp3.ResponseBody
import rx.Observable

/**
 * Created by arran on 29/04/2017.
 */
interface ITorrentRepository {
    fun getStatus(): Observable<String>

    fun downloadTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun getTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun getAllTorrentsFromStorage(): Observable<List<TorrentInfo>?>

    fun getTorrentFileData(hash: String, path: String): Observable<ResponseBody>
}