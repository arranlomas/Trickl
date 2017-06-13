package com.shwifty.tex.repositories

import android.content.Context
import com.shwifty.tex.models.ConfluenceInfo
import com.shwifty.tex.models.FileStatePiece
import com.shwifty.tex.models.TorrentFile
import com.shwifty.tex.models.TorrentInfo
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
    val torrentInfoDeleteListener: PublishSubject<TorrentInfo>

    //API

    fun getStatus(): Observable<ConfluenceInfo>

    fun postTorrentFile(hash: String, file: File): Observable<ResponseBody>

    fun getFileState(torrentFile: TorrentFile): Observable<Pair<TorrentFile,List<FileStatePiece>>>

    fun downloadTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun getTorrentInfo(hash: String): Observable<TorrentInfo?>

    fun startFileDownloading(torrentFile: TorrentFile, context: Context, wifiOnly: Boolean)

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