package com.shwifty.tex.views.torrentfiles.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.openFile
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.base.BasePresenter
import com.shwifty.tex.views.main.mvp.MainContract
import com.shwifty.tex.views.torrentfiles.list.TorrentFilesAdapter
import rx.subscriptions.CompositeSubscription

/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesPresenter : BasePresenter<TorrentFilesContract.View>(), TorrentFilesContract.Presenter {

    lateinit override var torrentHash: String

    lateinit var torrentRepository: ITorrentRepository

    lateinit var mainPresenter: MainContract.Presenter

    override fun setup(arguments: Bundle?) {
        torrentRepository = Confluence.torrentRepository
        mainPresenter = TricklComponent.mainComponent.getMainPresenter()
        if (arguments?.containsKey(TorrentFilesFragment.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(TorrentFilesFragment.ARG_TORRENT_HASH) ?: ""
        }
    }

    override fun loadTorrent(torrentHash: String) {
        torrentRepository.getTorrentInfo(torrentHash)
                .subscribe(object : BaseSubscriber<TorrentInfo>(){
                    override fun onNext(t: TorrentInfo?) {
                        mvpView.setLoading(false)
                        t?.let { mvpView.setupViewFromTorrentInfo(it) }
                    }
                })
                .addSubscription()
    }

    override fun viewClicked(torrentFile: TorrentFile, action: TorrentFilesAdapter.Companion.ClickTypes) {
        when (action) {
            TorrentFilesAdapter.Companion.ClickTypes.DOWNLOAD -> {
                mainPresenter.checkStatusForDownload(torrentFile)
                mvpView.dismiss()
            }
            TorrentFilesAdapter.Companion.ClickTypes.OPEN -> {
                mvpView.openTorrentFile(torrentFile, torrentRepository)
            }
            TorrentFilesAdapter.Companion.ClickTypes.CHROMECAST -> {
                mainPresenter.startChromecast(torrentFile)
            }

        }
    }
}