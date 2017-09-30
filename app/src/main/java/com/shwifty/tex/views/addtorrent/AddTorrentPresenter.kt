package com.shwifty.tex.views.addtorrent

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findNameFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.shwifty.tex.R
import java.net.URLDecoder

/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentPresenter : AddTorrentContract.Presenter {

    lateinit var torrentRepository: ITorrentRepository
    lateinit var view: AddTorrentContract.View
    override var torrentHash: String? = null
    override var torrentMagnet: String? = null
    override var torrentName: String? = null
    override var torrentTrackers: List<String>? = null

    override fun setup(context: Context, view: AddTorrentContract.View, arguments: Bundle?) {
        this.view = view
        torrentRepository = Confluence.torrentRepository
        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(AddTorrentActivity.ARG_TORRENT_HASH) ?: ""
        }

        if (arguments?.containsKey(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: false) {
            torrentMagnet = arguments?.getString(AddTorrentActivity.ARG_TORRENT_MAGNET) ?: ""
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
                .subscribe({
                    view.notifyTorrentAdded()
                }, {
                    view.showError(R.string.error_get_torrent_info)
                })
    }

}