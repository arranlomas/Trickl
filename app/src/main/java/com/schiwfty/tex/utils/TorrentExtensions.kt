package com.schiwfty.tex.utils

import com.schiwfty.tex.bencoding.TorrentParser
import com.schiwfty.tex.models.TorrentInfo
import java.io.File
import java.io.FileNotFoundException
import java.net.URLDecoder
import java.util.regex.Pattern

/**
 * Created by arran on 30/04/2017.
 */
@Throws(FileNotFoundException::class, IllegalAccessException::class)
fun File.getAsTorrent(): TorrentInfo? {
    if (!exists() && !canRead()) throw FileNotFoundException("File cannot be found, or is not readable: $path")
    if (!path.endsWith(".torrent")) throw IllegalAccessException("File must end with .torrent")
    val torrentInfo = TorrentParser.parseTorrent(this.absolutePath)
    if (torrentInfo?.totalSize == 0L && torrentInfo.fileList.size > 1) {
        torrentInfo.fileList.forEach {
            torrentInfo.totalSize += it.fileLength ?: 0
        }
    }
    return torrentInfo
}

fun Long.formatBytesAsSize(): String {
    if (this > 0.1 * 1024.0 * 1024.0 * 1024.0) {
        val f = this.toFloat() / 1024f / 1024f / 1024f
        return String.format("%1$.1f GB", f)
    } else if (this > 0.1 * 1024.0 * 1024.0) {
        val f = this.toFloat() / 1024f / 1024f
        return String.format("%1$.1f MB", f)
    } else {
        val f = this / 1024f
        return String.format("%1$.1f kb", f)
    }
}

fun String.findHashFromMagnet(): String? {
    val pattern = Pattern.compile("xt=urn:btih:(.*?)(&|$)")
    val matcher = pattern.matcher(this)
    if (matcher.find())
        return matcher.group(1)
    else
        return null
}

fun String.findNameFromMagnet(): String? {
    val pattern = Pattern.compile("dn=(.*?)(&|$)")
    val matcher = pattern.matcher(this)
    if (matcher.find())
        return matcher.group(1)
    else
        return null
}

fun String.findTrackersFromMagnet(): List<String> {
    val trackerList = mutableListOf<String>()
    val pattern = Pattern.compile("tr=(.*?)(&|$)")
    val matcher = pattern.matcher(this)
    while (matcher.find()) {
        trackerList.add(URLDecoder.decode(matcher.group(1), "UTF-8"))
    }
    return trackerList.toList()
}