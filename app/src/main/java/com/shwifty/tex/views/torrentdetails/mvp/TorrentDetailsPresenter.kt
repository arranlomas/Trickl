package com.shwifty.tex.views.torrentdetails.mvp

import android.os.Bundle
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 7/05/2017.
 */
class TorrentDetailsPresenter : BasePresenter<TorrentDetailsContract.View>(), TorrentDetailsContract.Presenter {

    lateinit override var torrentHash: String

    lateinit var torrentRepository: ITorrentRepository

    override fun setup(arguments: Bundle?) {
        torrentRepository = Confluence.torrentRepository
        if (arguments?.containsKey(TorrentDetailsFragment.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(TorrentDetailsFragment.ARG_TORRENT_HASH) ?: ""
        }

    }

    override fun loadTorrent(torrentHash: String) {
        torrentRepository.getTorrentInfo(torrentHash)
                .subscribe(object : BaseSubscriber<TorrentInfo>() {
                    override fun onNext(pair: TorrentInfo?) {
                        mvpView.setLoading(false)
                        pair?.let { mvpView.setupViewFromTorrentInfo(it) }
                    }
                })
                .addSubscription()
    }
}