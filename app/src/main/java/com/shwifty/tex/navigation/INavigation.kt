package com.shwifty.tex.navigation

import android.content.Context
import com.schiwfty.torrentwrapper.models.TorrentFile

/**
 * Created by arran on 4/03/2018.
 */
interface INavigation {
    fun goTo(navigationKey: NavigationKey)
//    fun showTorrentInfo(torrentInfo: TorrentInfo)

//    fun downloadTorrent(torrentFile: TorrentFile)

//    fun openTorrent(torrentFile: TorrentFile)

//    fun deleteTorrent(torrentFile: TorrentFile)

//    fun chromecastTorrent(torrentFile: TorrentFile)

//    fun addMagnet(magnet: String)

//    fun addHash(hash: String)
}

sealed class NavigationKey {
    data class OpenTorrentInfo(val context: Context, val infoHash: String) : NavigationKey()
    data class OpenTorrent(val context: Context, val torrentFile: TorrentFile) : NavigationKey()
    data class AddTorrent(val context: Context, val hash: String? = null, val magnet: String? = null, val torrentFilePath: String? = null) : NavigationKey()
}