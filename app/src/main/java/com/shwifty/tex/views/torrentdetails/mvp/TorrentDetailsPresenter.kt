package com.shwifty.tex.views.torrentdetails.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository

/**
 * Created by arran on 7/05/2017.
 */
class TorrentDetailsPresenter : TorrentDetailsContract.Presenter {

    lateinit var view: TorrentDetailsContract.View
    lateinit override var torrentHash: String

    lateinit var torrentRepository: ITorrentRepository

    override fun setup(context: Context, view: TorrentDetailsContract.View, arguments: Bundle?) {
        torrentRepository = Confluence.torrentRepository
        this.view = view
        if (arguments?.containsKey(TorrentDetailsFragment.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(TorrentDetailsFragment.ARG_TORRENT_HASH) ?: ""
        }

    }

    override fun loadTorrent(torrentHash: String) {
        torrentRepository.getTorrentInfo(torrentHash)
                .subscribe({
                    if(it!=null) view.setupViewFromTorrentInfo(it)
                }, {
                   // TODO("ERROR HANDLING")
                })


    }
}