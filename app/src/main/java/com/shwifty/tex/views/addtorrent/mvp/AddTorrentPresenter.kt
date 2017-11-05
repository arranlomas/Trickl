package com.shwifty.tex.views.addtorrent.mvp

import android.os.Bundle
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.ParseTorrentResult
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findNameFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.shwifty.tex.utils.logTorrentParseError
import com.shwifty.tex.views.base.BasePresenter
import java.net.URLDecoder

/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentPresenter(val torrentRepository: ITorrentRepository) : BasePresenter<AddTorrentContract.View>(), AddTorrentContract.Presenter {

    private var alreadyExisted = false

    override var torrentHash: String? = null
    override var torrentMagnet: String? = null
    override var torrentName: String? = null
    override var torrentTrackers: List<String>? = null

    override fun setup(arguments: Bundle?) {
        if (arguments?.containsKey(AddTorrentActivity.Companion.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(AddTorrentActivity.Companion.ARG_TORRENT_HASH) ?: ""
        }

        if (arguments?.containsKey(AddTorrentActivity.Companion.ARG_TORRENT_MAGNET) ?: false) {
            torrentMagnet = arguments?.getString(AddTorrentActivity.Companion.ARG_TORRENT_MAGNET) ?: ""
            torrentHash = torrentMagnet?.findHashFromMagnet()
            torrentMagnet?.findNameFromMagnet()?.let {
                torrentName = URLDecoder.decode(it, "UTF-8")
            }
            torrentTrackers = torrentMagnet?.findTrackersFromMagnet()
        }

        torrentRepository.getAllTorrentsFromStorage()
                .subscribe(object : BaseSubscriber<List<ParseTorrentResult>>() {
                    override fun onCompleted() {
                    }

                    override fun onStart() {
                    }

                    override fun onNext(results: List<ParseTorrentResult>) {
                        var alreadyExists = false
                        results.forEach { result ->
                            result.unwrapIfSuccess {
                                if (it.info_hash == torrentHash) alreadyExists = true
                            } ?: let { result.logTorrentParseError() }
                        }
                        this@AddTorrentPresenter.alreadyExisted = alreadyExists
                        fetchTorrent()
                    }
                })
    }

    override fun notifyBackPressed() {
        if (!alreadyExisted) {
            torrentRepository.getAllTorrentsFromStorage()
                    .subscribe(object : BaseSubscriber<List<ParseTorrentResult>>() {
                        override fun onNext(torrents: List<ParseTorrentResult>) {
                            torrents.forEach { result ->
                                result.unwrapIfSuccess {
                                    if (it.info_hash == torrentHash) torrentRepository.deleteTorrentInfoFromStorage(it)
                                } ?: let { result.logTorrentParseError() }
                            }
                        }
                    })
        }
    }

    private fun fetchTorrent() {
        val hash = torrentHash ?: return
        torrentRepository.downloadTorrentInfo(hash)
                .subscribe(object : BaseSubscriber<ParseTorrentResult>() {
                    override fun onNext(result: ParseTorrentResult) {
                        mvpView.setLoading(false)
                        mvpView.notifyTorrentAdded()
                        if (result is ParseTorrentResult.Error) super.onError(result.exception)
                    }
                })
                .addSubscription()
    }

}