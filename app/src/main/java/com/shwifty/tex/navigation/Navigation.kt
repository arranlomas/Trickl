package com.shwifty.tex.navigation

import com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity
import com.shwifty.tex.views.showtorrent.mvp.TorrentInfoActivity

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

    enum class Action {
        DOWNLOAD,
        CHROMECAST,
        OPEN,
        DELETE
    }

    enum class AddTorrentType {
        MAGNET,
        HASH
    }

//    val eventPublishSubject: PublishSubject<Pair<Action, TorrentFile>> = PublishSubject.create()
//    val showTorrentInfoPublishSubject: PublishSubject<TorrentInfo> = PublishSubject.create()
//    val addTorrentPublishSubject: PublishSubject<Pair<AddTorrentType, String>> = PublishSubject.create()

//    override fun showTorrentInfo(torrentInfo: TorrentInfo){
//        showTorrentInfoPublishSubject.onNext(torrentInfo)
//    }
//
//    override fun downloadTorrent(torrentFile: TorrentFile){
//        eventPublishSubject.onNext(Pair(Action.DOWNLOAD, torrentFile))
//    }
//
//    override fun openTorrent(torrentFile: TorrentFile){
//        eventPublishSubject.onNext(Pair(Action.OPEN, torrentFile))
//    }
//
//    override fun deleteTorrent(torrentFile: TorrentFile){
//        eventPublishSubject.onNext(Pair(Action.DELETE, torrentFile))
//    }
//
//    override fun chromecastTorrent(torrentFile: TorrentFile){
//        eventPublishSubject.onNext(Pair(Action.CHROMECAST, torrentFile))
//    }
//
//    override fun addMagnet(magnet: String){
//        addTorrentPublishSubject.onNext(Pair(AddTorrentType.MAGNET, magnet))
//    }
//
//    override fun addHash(hash: String){
//        addTorrentPublishSubject.onNext(Pair(AddTorrentType.HASH, hash))
//    }
}