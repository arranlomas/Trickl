package com.shwifty.tex.views.showtorrent

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.models.TorrentInfo
import com.shwifty.tex.repositories.ITorrentRepository
import com.shwifty.tex.utils.findHashFromMagnet
import com.shwifty.tex.utils.findNameFromMagnet
import com.shwifty.tex.utils.findTrackersFromMagnet
import com.shwifty.tex.views.addtorrent.AddTorrentActivity
import com.shwifty.tex.views.main.mvp.MainContract
import rx.subscriptions.CompositeSubscription
import java.net.URLDecoder
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentInfoPresenter : TorrentInfoContract.Presenter {


    @Inject
    lateinit var torrentRepository: ITorrentRepository

    @Inject
    lateinit var mainPresenter: MainContract.Presenter

    lateinit var view: TorrentInfoContract.View
    override lateinit var torrentHash: String
    override var torrentMagnet: String? = null
    override var torrentName: String? = null
    override var torrentTrackers: List<String>? = null
    override var torrentInfo: TorrentInfo? = null

    private val compositeSubscription = CompositeSubscription()

    override fun setup(context: Context, view: TorrentInfoContract.View, arguments: Bundle?) {
        this.view = view
        TricklComponent.mainComponent.inject(this)

        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(AddTorrentActivity.ARG_TORRENT_HASH) ?: ""
        }

        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: false) {
            torrentMagnet = arguments?.getString(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: ""
            torrentMagnet?.findHashFromMagnet()?.let { torrentHash = it }
            torrentName = URLDecoder.decode(torrentMagnet?.findNameFromMagnet(), "UTF-8")
            torrentTrackers = torrentMagnet?.findTrackersFromMagnet()
        }

        compositeSubscription.add(
                torrentRepository.torrentInfoDeleteListener
                        .subscribe({
                            view.dismiss()
                        }, {})
        )

    }

    override fun destroy(){
        compositeSubscription.unsubscribe()
    }

    override fun fetchTorrent() {
        val hash = torrentHash
        torrentRepository.getTorrentInfo(hash)
                .subscribe({
                    torrentInfo = it
                    torrentName = it?.name
                    it?.info_hash?.let { torrentHash = it }
                    torrentTrackers = it?.announceList
                    //SUCCESS
                    view.notifyTorrentAdded()
                }, {
                    view.showError(R.string.error_get_torrent_info)
                    //ERROR
                })
    }

    override fun optionsItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.action_delete -> {
                view.notifyTorrentDeleted()
            }
        }
    }
}