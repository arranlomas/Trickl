package com.shwifty.tex.views.torrentfiles.mvp

import android.os.Bundle
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.ParseTorrentResult
import com.shwifty.tex.utils.logTorrentParseError
import com.shwifty.tex.views.base.BasePresenter
import com.shwifty.tex.views.main.MainEventHandler
import com.shwifty.tex.views.torrentfiles.list.TorrentFilesAdapter

/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesPresenter(val torrentRepository: ITorrentRepository) : BasePresenter<TorrentFilesContract.View>(), TorrentFilesContract.Presenter {

    lateinit override var torrentHash: String

    override fun setup(arguments: Bundle?) {
        if (arguments?.containsKey(TorrentFilesFragment.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(TorrentFilesFragment.ARG_TORRENT_HASH) ?: ""
        }
    }

    override fun loadTorrent(torrentHash: String) {
        torrentRepository.downloadTorrentInfo(torrentHash)
                .subscribe(object : BaseSubscriber<ParseTorrentResult>() {
                    override fun onNext(result: ParseTorrentResult) {
                        mvpView.setLoading(false)
                        result.unwrapIfSuccess { mvpView.setupViewFromTorrentInfo(it) } ?: let { result.logTorrentParseError() }
                    }
                })
                .addSubscription()
    }

    override fun viewClicked(torrentFile: TorrentFile, action: TorrentFilesAdapter.Companion.ClickTypes) {
        when (action) {
            TorrentFilesAdapter.Companion.ClickTypes.DOWNLOAD -> {
                MainEventHandler.downloadTorrent(torrentFile)
                mvpView.dismiss()
            }
            TorrentFilesAdapter.Companion.ClickTypes.OPEN -> MainEventHandler.openTorrent(torrentFile)
            TorrentFilesAdapter.Companion.ClickTypes.CHROMECAST -> MainEventHandler.chromecastTorrent(torrentFile)
        }
    }
}