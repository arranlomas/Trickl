package com.shwifty.tex.persistence

import com.shwifty.tex.models.TorrentFile

/**
 * Created by arran on 19/05/2017.
 */
interface ITorrentPersistence {
    var torrentFileDeleted: (TorrentFile) -> Unit

    fun getDownloadFiles(): List<TorrentFile>

    fun getDownloadingFile(hash: String, path: String): TorrentFile?

    fun removeTorrentDownloadFile(torrentFile: TorrentFile)

    fun saveTorrentFile(torrentFile: TorrentFile)
}