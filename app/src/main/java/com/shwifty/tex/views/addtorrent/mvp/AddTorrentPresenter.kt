package com.shwifty.tex.views.addtorrent.mvp

import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findNameFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet

/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentPresenter : com.shwifty.tex.views.base.BasePresenter<AddTorrentContract.View>(), com.shwifty.tex.views.addtorrent.AddTorrentContract.Presenter {

    lateinit var torrentRepository: com.schiwfty.torrentwrapper.repositories.ITorrentRepository
    override var torrentHash: String? = null
    override var torrentMagnet: String? = null
    override var torrentName: String? = null
    override var torrentTrackers: List<String>? = null

    override fun setup(arguments: android.os.Bundle?) {
        torrentRepository = com.schiwfty.torrentwrapper.confluence.Confluence.torrentRepository
        if (arguments?.containsKey(com.shwifty.tex.views.addtorrent.AddTorrentActivity.Companion.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(com.shwifty.tex.views.addtorrent.AddTorrentActivity.Companion.ARG_TORRENT_HASH) ?: ""
        }

        if (arguments?.containsKey(com.shwifty.tex.views.addtorrent.AddTorrentActivity.Companion.ARG_TORRENT_MAGNET) ?: false) {
            torrentMagnet = arguments?.getString(com.shwifty.tex.views.addtorrent.AddTorrentActivity.Companion.ARG_TORRENT_MAGNET) ?: ""
            torrentHash = torrentMagnet?.findHashFromMagnet()
            torrentMagnet?.findNameFromMagnet()?.let {
                torrentName = java.net.URLDecoder.decode(it, "UTF-8")
            }
            torrentTrackers = torrentMagnet?.findTrackersFromMagnet()
        }
    }

    override fun fetchTorrent() {
        val hash = torrentHash ?: return
        torrentRepository.getTorrentInfo(hash)
                .subscribe(object : com.shwifty.tex.views.base.BasePresenter.BaseSubscriber<TorrentInfo>() {
                    override fun onNext(pair: com.schiwfty.torrentwrapper.models.TorrentInfo?) {
                        mvpView.setLoading(false)
                        mvpView.notifyTorrentAdded()
                    }
                })
                .addSubscription()
    }

}