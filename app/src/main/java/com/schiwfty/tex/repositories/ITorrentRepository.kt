package com.schiwfty.tex.repositories

import com.schiwfty.tex.models.ConfluenceInfo
import com.schiwfty.tex.models.TorrentInfo
import okhttp3.ResponseBody
import rx.Observable
import java.io.File

/**
 * Created by arran on 29/04/2017.
 */
interface ITorrentRepository {
    fun setupClientFromRepo(): Observable<ResponseBody>

    fun getStatus(): Observable<ConfluenceInfo>

    fun downloadTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun getTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun getAllTorrentsFromStorage(): Observable<List<TorrentInfo>>

    fun getTorrentFileData(hash: String, path: String): Observable<ResponseBody>

    fun postTorrentFile(hash: String, file: File): Observable<ResponseBody>
}