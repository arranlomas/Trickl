package com.schiwfty.tex.utils

import com.schiwfty.tex.bencoding.TorrentParser
import com.schiwfty.tex.models.TorrentInfo
import java.io.File
import java.io.FileNotFoundException

/**
 * Created by arran on 30/04/2017.
 */
@Throws(FileNotFoundException::class, IllegalAccessException::class)
fun File.getAsTorrent(): TorrentInfo? {
    if (!exists() && !canRead()) throw FileNotFoundException("File cannot be found, or is not readable: $path")
    if (!path.endsWith(".torrent")) throw IllegalAccessException("File must end with .torrent")


    val torrentInfo = TorrentParser.parseTorrent(this.absolutePath)
    return torrentInfo
}