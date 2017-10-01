package com.shwifty.tex.views.main

import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import rx.subjects.PublishSubject

/**
 * Created by arran on 1/10/2017.
 */
object MainEventHandler {
    enum class Action{
        DOWNLOAD,
        CHROMECAST,
        OPEN,
        DELETE
    }

    enum class AddTorrentType{
        MAGNET,
        HASH
    }

    val eventPublishSubject: PublishSubject<Pair<Action, TorrentFile>> = PublishSubject.create()
    val showTorrentInfoPublishSubject: PublishSubject<TorrentInfo> = PublishSubject.create()
    val addTorrentPublishSubject: PublishSubject<Pair<AddTorrentType, String>> = PublishSubject.create()

    fun showTorrentInfo(torrentInfo: TorrentInfo){
        showTorrentInfoPublishSubject.onNext(torrentInfo)
    }

    fun downloadTorrent(torrentFile: TorrentFile){
        eventPublishSubject.onNext(Pair(Action.DOWNLOAD, torrentFile))
    }

    fun openTorrent(torrentFile: TorrentFile){
        eventPublishSubject.onNext(Pair(Action.OPEN, torrentFile))
    }

    fun deleteTorrent(torrentFile: TorrentFile){
        eventPublishSubject.onNext(Pair(Action.DELETE, torrentFile))
    }

    fun chromecastTorrent(torrentFile: TorrentFile){
        eventPublishSubject.onNext(Pair(Action.CHROMECAST, torrentFile))
    }

    fun addMagnet(magnet: String){
        addTorrentPublishSubject.onNext(Pair(AddTorrentType.MAGNET, magnet))
    }

    fun addHash(hash: String){
        addTorrentPublishSubject.onNext(Pair(AddTorrentType.HASH, hash))
    }
}