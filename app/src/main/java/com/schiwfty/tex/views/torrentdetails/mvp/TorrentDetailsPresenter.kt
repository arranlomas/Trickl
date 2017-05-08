package com.schiwfty.tex.views.torrentdetails.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentDetailsPresenter: TorrentDetailsContract.Presenter {

    lateinit var view: TorrentDetailsContract.View
    lateinit override var torrentHash: String

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    override fun setup(context: Context, view: TorrentDetailsContract.View, arguments: Bundle?) {
        TricklComponent.networkComponent.inject(this)
        this.view = view
        if(arguments?.containsKey(TorrentDetailsFragment.ARG_TORRENT_HASH) ?: false){
            torrentHash = arguments?.getString(TorrentDetailsFragment.ARG_TORRENT_HASH) ?: ""
        }

    }

    override fun getTorrent(torrentHash: String) {
        torrentRepository.getTorrentInfoFromCache(torrentHash)
                .subscribe ({
                    view.setupViewFromTorrentInfo(it)
                },{
                    TODO("ERROR HANDLING")
                })


    }
}