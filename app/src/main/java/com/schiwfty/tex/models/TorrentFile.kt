package com.schiwfty.tex.models

import io.realm.annotations.PrimaryKey

/**
 * Created by arran on 30/04/2017.
 */
data class TorrentFile(val fileLength: Long? = null,
                       val fileDirs: List<String>? = null,
                       val torrentHash: String,
                       @PrimaryKey
                       val primaryKey: String
) {
    var percComplete: Int = 0
}