package com.shwifty.tex.views.torrentfiles.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.openFile
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.main.mvp.MainContract
import com.shwifty.tex.views.torrentfiles.list.TorrentFilesAdapter
import rx.subscriptions.CompositeSubscription

/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesPresenter : TorrentFilesContract.Presenter {

    lateinit var view: TorrentFilesContract.View
    lateinit override var torrentHash: String
    lateinit var context: Context

    lateinit var torrentRepository: ITorrentRepository

    lateinit var mainPresenter: MainContract.Presenter

    private val compositeSubscription = CompositeSubscription()

    override fun setup(context: Context, view: TorrentFilesContract.View, arguments: Bundle?) {
        torrentRepository = Confluence.torrentRepository
        mainPresenter = TricklComponent.mainComponent.getMainPresenter()
        this.view = view
        this.context = context
        if (arguments?.containsKey(TorrentFilesFragment.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(TorrentFilesFragment.ARG_TORRENT_HASH) ?: ""
        }
    }

    override fun destroy() {
        compositeSubscription.unsubscribe()
    }

    override fun loadTorrent(torrentHash: String) {
        torrentRepository.getTorrentInfo(torrentHash)
                .subscribe({
                    if (it != null) view.setupViewFromTorrentInfo(it)
                }, {
                    it.printStackTrace()
                })
    }

    override fun viewClicked(torrentFile: TorrentFile, action: TorrentFilesAdapter.Companion.ClickTypes) {
        when (action) {
            TorrentFilesAdapter.Companion.ClickTypes.DOWNLOAD -> {
                mainPresenter.checkStatusForDownload(torrentFile)
                view.dismiss()
            }
            TorrentFilesAdapter.Companion.ClickTypes.OPEN -> {
                torrentFile.openFile(context, torrentRepository,{
                    view.showError(R.string.error_no_activity)
                })
            }
            TorrentFilesAdapter.Companion.ClickTypes.CHROMECAST -> {
                mainPresenter.startChromecast(torrentFile)
            }

        }
    }
}