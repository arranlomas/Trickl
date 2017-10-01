package com.shwifty.tex.views.showtorrent

import android.os.Bundle
import android.view.MenuItem
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findNameFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.shwifty.tex.R
import com.shwifty.tex.views.addtorrent.AddTorrentActivity
import com.shwifty.tex.views.base.BasePresenter
import java.net.URLDecoder

/**
 * Created by arran on 7/05/2017.
 */
class TorrentInfoPresenter : BasePresenter<TorrentInfoContract.View>(), TorrentInfoContract.Presenter {

    lateinit var torrentRepository: ITorrentRepository

    override lateinit var torrentHash: String
    override var torrentMagnet: String? = null
    override var torrentName: String? = null
    override var torrentTrackers: List<String>? = null
    override var torrentInfo: TorrentInfo? = null

    override fun setup(arguments: Bundle?) {
        torrentRepository = Confluence.torrentRepository

        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(AddTorrentActivity.ARG_TORRENT_HASH) ?: ""
        }

        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: false) {
            torrentMagnet = arguments?.getString(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: ""
            torrentMagnet?.findHashFromMagnet()?.let { torrentHash = it }
            torrentName = URLDecoder.decode(torrentMagnet?.findNameFromMagnet(), "UTF-8")
            torrentTrackers = torrentMagnet?.findTrackersFromMagnet()
        }

        torrentRepository.torrentInfoDeleteListener
                .subscribe(object : BaseSubscriber<TorrentInfo>() {
                    override fun onNext(pair: TorrentInfo?) {
                        mvpView.setLoading(false)
                        mvpView.dismiss()
                    }
                })
                .addSubscription()

    }

    override fun fetchTorrent() {
        val hash = torrentHash
        torrentRepository.getTorrentInfo(hash)
                .subscribe(object : BaseSubscriber<TorrentInfo>(){
                    override fun onNext(pair: TorrentInfo?) {
                        mvpView.setLoading(false)
                        torrentInfo = pair
                        torrentName = pair?.name
                        pair?.info_hash?.let { torrentHash = it }
                        torrentTrackers = pair?.announceList
                        mvpView.notifyTorrentAdded()
                    }
                })
                .addSubscription()
    }

    override fun optionsItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.action_delete -> {
                mvpView.notifyTorrentDeleted()
            }
        }
    }
}