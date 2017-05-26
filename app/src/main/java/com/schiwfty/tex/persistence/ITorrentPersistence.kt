package com.schiwfty.tex.persistence

import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.views.torrentfiles.list.TorrentFilesAdapter
import rx.Observable

/**
 * Created by arran on 19/05/2017.
 */
interface ITorrentPersistence {
    var torrentFileDeleted: (TorrentFile) -> Unit

    fun getDownloadFiles(): List<TorrentFile>

    fun getDownloadingFile(hash: String, path: String): TorrentFile

    fun removeTorrentDownloadFile(torrentFile: TorrentFile)

    fun saveTorrentFile(torrentFile: TorrentFile)
}