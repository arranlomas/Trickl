package com.schiwfty.tex.persistence

import com.schiwfty.tex.models.TorrentFile
import rx.Observable

/**
 * Created by arran on 19/05/2017.
 */
interface ITorrentPersistence {
    fun getDownloadFiles(): List<TorrentFile>

    fun getDownloadingFile(hash: String, path: String): TorrentFile

    fun removeTorrentDownloadFile(downloadingFile: TorrentFile)

    fun saveTorrentFile(torrentFile: TorrentFile)
}