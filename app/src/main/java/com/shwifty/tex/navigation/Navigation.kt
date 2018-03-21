package com.shwifty.tex.navigation

import com.shwifty.tex.views.addtorrent.mvi.AddTorrentActivity
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoActivity

/**
 * Created by arran on 1/10/2017.
 */
class Navigation : INavigation {
    override fun goTo(navigationKey: NavigationKey) {
        when (navigationKey) {
            is NavigationKey.OpenTorrentInfo -> TorrentInfoActivity.open(navigationKey.context, navigationKey.infoHash)
            is NavigationKey.OpenTorrent -> TODO()
            is NavigationKey.AddTorrent -> AddTorrentActivity.startActivity(navigationKey.context, navigationKey.hash, navigationKey.magnet, navigationKey.torrentFilePath)
        }
    }
}