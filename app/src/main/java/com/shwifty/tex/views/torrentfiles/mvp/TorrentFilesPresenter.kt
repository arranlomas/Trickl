package com.shwifty.tex.views.torrentfiles.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.ParseTorrentResult
import com.shwifty.tex.actions.IActionManager
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.utils.logTorrentParseError
import com.shwifty.tex.views.base.mvp.BasePresenter
import com.shwifty.tex.views.torrentfiles.list.TorrentFilesAdapter

/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesPresenter(val torrentRepository: ITorrentRepository,
                            val actionManager: IActionManager) : BasePresenter<TorrentFilesContract.View>(), TorrentFilesContract.Presenter {

    private lateinit var torrentHash: String

    override fun setup(arguments: Bundle?) {
        if (arguments?.containsKey(TorrentFilesFragment.ARG_TORRENT_HASH) == true) {
            torrentHash = arguments?.getString(TorrentFilesFragment.ARG_TORRENT_HASH) ?: ""
        }
    }

    override fun loadTorrent() {
        torrentRepository.downloadTorrentInfo(torrentHash)
                .subscribe(object : BaseSubscriber<ParseTorrentResult>() {
                    override fun onNext(result: ParseTorrentResult) {
                        mvpView.setLoading(false)
                        result.unwrapIfSuccess { mvpView.setupViewFromTorrentInfo(it) }
                                ?: let { result.logTorrentParseError() }
                    }
                })
                .addSubscription()
    }

    override fun viewClicked(context: Context, torrentFile: TorrentFile, action: TorrentFilesAdapter.Companion.ClickTypes) {
        val onError = { error: String ->
            mvpView.showError(error)
        }
        when (action) {
            TorrentFilesAdapter.Companion.ClickTypes.DOWNLOAD -> {
                actionManager.startDownload(context, torrentFile, onError)
                mvpView.dismiss()
            }
            TorrentFilesAdapter.Companion.ClickTypes.OPEN ->
                actionManager.openTorrentFile(context, torrentFile, onError)
            TorrentFilesAdapter.Companion.ClickTypes.CHROMECAST ->
                actionManager.startChromecast(context, torrentFile, onError)
        }
    }
}