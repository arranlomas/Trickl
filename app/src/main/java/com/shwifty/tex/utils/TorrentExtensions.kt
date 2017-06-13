package com.shwifty.tex.utils

import android.content.Context
import com.google.android.gms.cast.MediaInfo
import com.shwifty.tex.bencoding.TorrentParser
import com.shwifty.tex.confluence.Confluence
import com.shwifty.tex.models.TorrentFile
import com.shwifty.tex.models.TorrentInfo
import com.shwifty.tex.repositories.ITorrentRepository
import java.io.File
import java.net.URLDecoder
import java.util.*
import java.util.regex.Pattern


/**
 * Created by arran on 30/04/2017.
 */
fun File.getAsTorrent(): TorrentInfo? {
    if (!isValidTorrentFile())return null
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

fun TorrentFile.openFile(context: Context, torrentRepository: ITorrentRepository){
    torrentRepository.addTorrentFileToPersistence(this)
    val url = Confluence.fullUrl + "/data?ih=" + torrentHash + "&path=" + URLEncoder.encode(getFullPath(), "UTF-8")
    val file = File(getFullPath())
    val map = MimeTypeMap.getSingleton()
    var type: String? = map.getMimeTypeFromExtension(file.extension)

    if (type == null)
        type = "*/*"

    val intent = Intent(Intent.ACTION_VIEW)

    intent.setDataAndType(Uri.parse(url), type)
    context.startActivity(intent)
}

fun TorrentFile.getFullPath(): String{
    var path = ""
    fileDirs?.forEachIndexed { index, s ->
        if (index == (fileDirs.size - 1))
            path += s
        else
            path += "$s/"
    }
    return path
}

fun LinkedList<String>.concatStrings(): String{
    var path = ""
    forEachIndexed { index, s ->
        if (index == (size - 1))
            path += s
        else
            path += "$s/"
    }
    return path
}

fun File.isValidTorrentFile():Boolean{
    if (!exists()) return false
    if(!canRead()) return false
    if (!path.endsWith(".torrent")) return false
    return true
}

fun File.copyToTorrentDirectory():Boolean{
    if(!isValidTorrentFile()) return false
    if (parentFile.absolutePath.equals(Confluence.torrentRepo)) return true
    val testFile = File(Confluence.torrentRepo.absolutePath + File.separator + name)
    if(testFile.exists())return true
    val result = copyTo(File(Confluence.torrentRepo.absolutePath, name), true)
    return result.isValidTorrentFile()
}

fun TorrentFile.buildMediaInfo(mime: String): MediaInfo {
    val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
    movieMetadata.putString(MediaMetadata.KEY_TITLE, getFullPath())
    return MediaInfo.Builder(getShareableUrl())
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType(mime)
            .setMetadata(movieMetadata)
            .build()
}

fun TorrentFile.getShareableUrl(): String{
   return "${Confluence.fullUrl}/data?ih=$torrentHash&path=${getFullPath()}"
}

fun TorrentFile.getMimeType(): String{
    val file = File(getFullPath())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
}

fun TorrentFile.canCast(): Boolean{
    val splitMime = getMimeType().split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val fileType = splitMime[0].toLowerCase()
    val format = splitMime[1].toLowerCase()
    if (fileType == "video" || fileType == "audio"){
        return MimeConstants.chromecastSet.contains(format)
    }else return false
}