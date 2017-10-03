package com.shwifty.tex.views.addtorrent.mvp

import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findNameFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.shwifty.tex.views.base.BasePresenter
import java.net.URLDecoder

/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentPresenter(val torrentRepository: ITorrentRepository) : BasePresenter<AddTorrentContract.View>(), AddTorrentContract.Presenter {

    override var torrentHash: String? = null
    override var torrentMagnet: String? = null
    override var torrentName: String? = null
    override var torrentTrackers: List<String>? = null

    override fun setup(arguments: android.os.Bundle?) {
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
    }

    override fun fetchTorrent() {
        val hash = torrentHash ?: return
        torrentRepository.getTorrentInfo(hash)
                .subscribe(object : BaseSubscriber<TorrentInfo>() {
                    override fun onNext(pair: TorrentInfo?) {
                        mvpView.setLoading(false)
                        mvpView.notifyTorrentAdded()
                    }
                })
                .addSubscription()
    }

}