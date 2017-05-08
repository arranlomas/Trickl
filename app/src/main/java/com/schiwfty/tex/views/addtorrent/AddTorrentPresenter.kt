package com.schiwfty.tex.views.addtorrent

import android.content.Context
import android.os.Bundle
import com.schiwfty.tex.R
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentPresenter : AddTorrentContract.Presenter {

    @Inject
    lateinit var torrentRepository: ITorrentRepository
    lateinit var view: AddTorrentContract.View
    override lateinit var torrentHash: String

    override fun setup(context: Context, view: AddTorrentContract.View, arguments: Bundle?) {
        this.view = view
        TricklComponent.networkComponent.inject(this)
        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(AddTorrentActivity.ARG_TORRENT_HASH) ?: ""
        }
    }

    override fun fetchTorrent(hash: String) {
        torrentRepository.getTorrentInfoFromCache(hash)
                .subscribe({
                    //SUCCESS
                    view.notifyTorrentAdded()
                }, {
                    view.showError(R.string.error_get_torrent_info)
                    //ERROR
                })
    }

    override fun notifyAddTorrentClicked(hash: String) {

    }


}