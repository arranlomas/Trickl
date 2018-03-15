package com.shwifty.tex.views.torrentdetails.mvp

import android.os.Bundle
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.ParseTorrentResult
import com.shwifty.tex.utils.logTorrentParseError
import com.shwifty.tex.views.base.mvp.BasePresenter

/**
 * Created by arran on 7/05/2017.
 */
class TorrentDetailsPresenter(val torrentRepository: ITorrentRepository) : BasePresenter<TorrentDetailsContract.View>(), TorrentDetailsContract.Presenter {

    lateinit override var torrentHash: String

    override fun setup(arguments: Bundle?) {
        if (arguments?.containsKey(TorrentDetailsFragment.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(TorrentDetailsFragment.ARG_TORRENT_HASH) ?: ""
        }

    }

    override fun loadTorrent(torrentHash: String) {
        torrentRepository.downloadTorrentInfo(torrentHash)
                .subscribeWith(object : BaseObserver<ParseTorrentResult>() {
                    override fun onNext(result: ParseTorrentResult) {
                        mvpView.setLoading(false)
                        result.unwrapIfSuccess {
                            mvpView.setupViewFromTorrentInfo(it)
                        } ?: let { result.logTorrentParseError() }
                    }
                })
                .addObserver()
    }
}