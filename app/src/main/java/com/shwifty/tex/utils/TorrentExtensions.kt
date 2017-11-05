package com.shwifty.tex.utils

import com.crashlytics.android.Crashlytics
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.utils.ParseTorrentResult
import com.schiwfty.torrentwrapper.utils.getFullPath
import com.schiwfty.torrentwrapper.utils.getShareableDataUrl


/**
 * Created by arran on 30/04/2017.
 */
fun TorrentFile.buildMediaInfo(mime: String): MediaInfo {
    val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
    movieMetadata.putString(MediaMetadata.KEY_TITLE, getFullPath())
    return MediaInfo.Builder(getShareableDataUrl())
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType(mime)
            .setMetadata(movieMetadata)
            .build()
}

fun ParseTorrentResult.logTorrentParseError() {
    if (this is ParseTorrentResult.Error) {
        val error = this.exception
        error.printStackTrace()
        Crashlytics.logException(error)
    }
}