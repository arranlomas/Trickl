package com.schiwfty.tex.views.showtorrent

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import com.schiwfty.tex.R
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.utils.findHashFromMagnet
import com.schiwfty.tex.utils.findNameFromMagnet
import com.schiwfty.tex.utils.findTrackersFromMagnet
import com.schiwfty.tex.views.addtorrent.AddTorrentActivity
import com.schiwfty.tex.views.main.DialogManager
import com.schiwfty.tex.views.main.IDialogManager
import com.schiwfty.tex.views.main.mvp.MainContract
import java.net.URLDecoder
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentInfoPresenter : TorrentInfo.Presenter {

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    @Inject
    lateinit var mainPresenter: MainContract.Presenter

    lateinit var view: TorrentInfo.View
    override lateinit var torrentHash: String
    override var torrentMagnet: String? = null
    override var torrentName: String? = null
    override var torrentTrackers: List<String>? = null

    override fun setup(context: Context, view: TorrentInfo.View, arguments: Bundle?) {
        this.view = view
        TricklComponent.mainComponent.inject(this)

        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(AddTorrentActivity.ARG_TORRENT_HASH) ?: ""
        }

        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: false) {
            torrentMagnet = arguments?.getString(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: ""
            torrentMagnet?.findHashFromMagnet()?.let { torrentHash = it  }
            torrentName = URLDecoder.decode(torrentMagnet?.findNameFromMagnet(), "UTF-8")
            torrentTrackers = torrentMagnet?.findTrackersFromMagnet()
        }


    }

    override fun fetchTorrent() {
        val hash = torrentHash
        torrentRepository.getTorrentInfo(hash)
                .subscribe({
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
        when(item.itemId){
            R.id.action_delete -> {
               view.notifyTorrentDeleted()
            }
        }
    }
}