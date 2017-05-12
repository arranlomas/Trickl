package com.schiwfty.tex.views.addtorrent

import android.content.Context
import android.os.Bundle
import com.schiwfty.tex.R
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.utils.findHashFromMagnet
import com.schiwfty.tex.utils.findNameFromMagnet
import com.schiwfty.tex.utils.findTrackersFromMagnet
import java.net.URLDecoder
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentPresenter : AddTorrentContract.Presenter {

    @Inject
    lateinit var torrentRepository: ITorrentRepository
    lateinit var view: AddTorrentContract.View
    override var torrentHash: String? = null
    override var torrentMagnet: String? = null
    override var torrentName: String? = null
    override var torrentTrackers: List<String>? = null

    override fun setup(context: Context, view: AddTorrentContract.View, arguments: Bundle?) {
        this.view = view
        TricklComponent.networkComponent.inject(this)
        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(AddTorrentActivity.ARG_TORRENT_HASH) ?: ""
        }

        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: false) {
            torrentMagnet = arguments?.getString(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: ""
            torrentHash = torrentMagnet?.findHashFromMagnet()
            torrentName = URLDecoder.decode(torrentMagnet?.findNameFromMagnet(), "UTF-8")
            torrentTrackers = torrentMagnet?.findTrackersFromMagnet()
        }


    }

    override fun fetchTorrent() {
        val hash = torrentHash ?: return
        torrentRepository.getTorrentInfo(hash)
                .subscribe({
                    //SUCCESS
                    view.notifyTorrentAdded()
                }, {
                    view.showError(R.string.error_get_torrent_info)
                    //ERROR
                })
    }


}