package com.shwifty.tex.views.showtorrent.mvp

import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findNameFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet

/**
 * Created by arran on 7/05/2017.
 */
class TorrentInfoPresenter : com.shwifty.tex.views.base.BasePresenter<TorrentInfoContract.View>(), com.shwifty.tex.views.showtorrent.TorrentInfoContract.Presenter {

    lateinit var torrentRepository: com.schiwfty.torrentwrapper.repositories.ITorrentRepository

    override lateinit var torrentHash: String
    override var torrentMagnet: String? = null
    override var torrentName: String? = null
    override var torrentTrackers: List<String>? = null
    override var torrentInfo: com.schiwfty.torrentwrapper.models.TorrentInfo? = null

    override fun setup(arguments: android.os.Bundle?) {
        torrentRepository = com.schiwfty.torrentwrapper.confluence.Confluence.torrentRepository

        if (arguments?.containsKey(com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity.Companion.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity.Companion.ARG_TORRENT_HASH) ?: ""
        }

        if (arguments?.containsKey(com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity.Companion.ARG_TORRENT_MAGNET) ?: false) {
            torrentMagnet = arguments?.getString(com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity.Companion.ARG_TORRENT_MAGNET) ?: ""
            torrentMagnet?.findHashFromMagnet()?.let { torrentHash = it }
            torrentName = java.net.URLDecoder.decode(torrentMagnet?.findNameFromMagnet(), "UTF-8")
            torrentTrackers = torrentMagnet?.findTrackersFromMagnet()
        }

        torrentRepository.torrentInfoDeleteListener
                .subscribe(object : com.shwifty.tex.views.base.BasePresenter.BaseSubscriber<TorrentInfo>() {
                    override fun onNext(pair: com.schiwfty.torrentwrapper.models.TorrentInfo?) {
                        mvpView.setLoading(false)
                        mvpView.dismiss()
                    }
                })
                .addSubscription()

    }

    override fun fetchTorrent() {
        val hash = torrentHash
        torrentRepository.getTorrentInfo(hash)
                .subscribe(object : com.shwifty.tex.views.base.BasePresenter.BaseSubscriber<TorrentInfo>(){
                    override fun onNext(pair: com.schiwfty.torrentwrapper.models.TorrentInfo?) {
                        mvpView.setLoading(false)
                        torrentInfo = pair
                        torrentName = pair?.name
                        pair?.info_hash?.let { torrentHash = it }
                        torrentTrackers = pair?.announceList
                        mvpView.notifyTorrentAdded()
                    }
                })
                .addSubscription()
    }

    override fun optionsItemSelected(item: android.view.MenuItem) {
        when (item.itemId) {
            com.shwifty.tex.R.id.action_delete -> {
                mvpView.notifyTorrentDeleted()
            }
        }
    }
}