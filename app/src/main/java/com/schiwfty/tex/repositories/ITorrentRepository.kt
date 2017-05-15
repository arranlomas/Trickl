package com.schiwfty.tex.repositories

import com.schiwfty.tex.models.ConfluenceInfo
import com.schiwfty.tex.models.FileStatePiece
import com.schiwfty.tex.models.TorrentInfo
import okhttp3.ResponseBody
import rx.Observable
import java.io.File
import rx.subjects.PublishSubject



/**
 * Created by arran on 29/04/2017.
 */
interface ITorrentRepository {
    val torrentFileProgressSource: PublishSubject<List<Triple<String, String, Int>>>

    fun setupClientFromRepo(): Observable<ResponseBody>

    fun getStatus(): Observable<ConfluenceInfo>

    fun downloadTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun getTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun getAllTorrentsFromStorage(): Observable<List<TorrentInfo>>

    fun getTorrentFileData(hash: String, path: String): Observable<ResponseBody>

    fun postTorrentFile(hash: String, file: File): Observable<ResponseBody>

    fun getFileState(hash: String, filePath: String): Observable<List<FileStatePiece>>

    fun getPercentagesFromDownloadingFiles(): Observable<List<Triple<String, String, Int>>>
}