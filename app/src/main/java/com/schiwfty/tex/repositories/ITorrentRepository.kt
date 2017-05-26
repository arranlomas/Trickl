package com.schiwfty.tex.repositories

import android.content.Context
import com.schiwfty.tex.models.ConfluenceInfo
import com.schiwfty.tex.models.FileStatePiece
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.models.TorrentInfo
import okhttp3.ResponseBody
import rx.Observable
import java.io.File
import rx.subjects.PublishSubject



/**
 * Created by arran on 29/04/2017.
 */
interface ITorrentRepository {
    val torrentFileProgressSource: PublishSubject<Boolean>
    val torrentFileDeleteListener: PublishSubject<TorrentFile>
    val torrentInfoListener: PublishSubject<TorrentInfo>

    //API

    fun getStatus(): Observable<ConfluenceInfo>

    fun postTorrentFile(hash: String, file: File): Observable<ResponseBody>

    fun getFileState(torrentFile: TorrentFile): Observable<Pair<TorrentFile,List<FileStatePiece>>>

    fun downloadTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun getTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun startFileDownloading(torrentFile: TorrentFile, context: Context)

    //PERSISTENCE

    fun addTorrentFileToPersistence(torrentFile: TorrentFile)

    fun getAllTorrentsFromStorage(): Observable<List<TorrentInfo>>

    fun getDownloadingFilesFromPersistence(): Observable<List<TorrentFile>>

    fun deleteTorrentData(torrentInfo: TorrentInfo): Boolean

    fun deleteTorrentFileFromPersistence(torrentFile: TorrentFile)

    fun deleteTorrentFileData(torrentFile: TorrentFile): Boolean

    fun deleteTorrentInfoFromStorage(torrentInfo: TorrentInfo): Boolean

    fun getTorrentFileFromPersistence(hash: String, path: String): TorrentFile
}