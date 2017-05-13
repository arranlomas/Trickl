package com.schiwfty.tex.models

import java.util.*

/**
 * Created by arran on 30/04/2017.
 */
data class TorrentInfo constructor(var name: String) {
    var announce: String = ""
    var pieceLength: Long = 0
    var piecesBlob: ByteArray? = null
    var pieces: List<String> = emptyList()
    var singleFileTorrent: Boolean = false
    var totalSize: Long = 0
    var fileList: List<TorrentFile> = emptyList()
    var comment: String = ""
    var createdBy: String = ""
    var creationDate: Date? = null
    var announceList: List<String> = emptyList()
    var info_hash: String = ""
    var percComplete: Float = 0f
}