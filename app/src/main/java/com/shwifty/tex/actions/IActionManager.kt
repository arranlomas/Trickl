package com.shwifty.tex.actions

import android.content.Context
import com.schiwfty.torrentwrapper.models.TorrentFile

/**
 * Created by arran on 4/03/2018.
 */
interface IActionManager {
    fun startDownload(context: Context, torrentFile: TorrentFile, onError: (String) -> Unit)

    fun startChromecast(context: Context, torrentFile: TorrentFile, onError: (String) -> Unit)

    fun openDeleteTorrentDialog(context: Context, torrentFile: TorrentFile)

    fun openTorrentFile(context: Context, torrentFile: TorrentFile, onError: (String) -> Unit)
}