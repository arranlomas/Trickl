package com.schiwfty.tex.views.torrentfiles.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesPresenter : TorrentFilesContract.Presenter {

    lateinit var view: TorrentFilesContract.View
    lateinit override var torrentHash: String

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    override fun setup(context: Context, view: TorrentFilesContract.View, arguments: Bundle?) {
        TricklComponent.networkComponent.inject(this)
        this.view = view
        if (arguments?.containsKey(TorrentFilesFragment.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(TorrentFilesFragment.ARG_TORRENT_HASH) ?: ""
        }
    }

    override fun loadTorrent(torrentHash: String) {
        torrentRepository.getTorrentInfo(torrentHash)
                .subscribe({
                    if(it!=null)view.setupViewFromTorrentInfo(it)
                }, {
                    TODO("ERROR HANDLING")
                })


    }
}