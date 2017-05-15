package com.schiwfty.tex.views.torrentfiles.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.utils.getFullPath
import com.schiwfty.tex.utils.openTorrent
import com.schiwfty.tex.views.torrentfiles.list.TorrentFilesAdapter
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesPresenter : TorrentFilesContract.Presenter {

    lateinit var view: TorrentFilesContract.View
    lateinit override var torrentHash: String
    lateinit var context: Context

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    override fun setup(context: Context, view: TorrentFilesContract.View, arguments: Bundle?) {
        TricklComponent.networkComponent.inject(this)
        this.view = view
        this.context = context
        if (arguments?.containsKey(TorrentFilesFragment.ARG_TORRENT_HASH) ?: false) {
            torrentHash = arguments?.getString(TorrentFilesFragment.ARG_TORRENT_HASH) ?: ""
        }
    }

    override fun loadTorrent(torrentHash: String) {
        torrentRepository.getTorrentInfo(torrentHash)
                .subscribe({
                    if(it!=null)view.setupViewFromTorrentInfo(it)
                }, {
                    TODO("ERROR HANDLING")
                })


    }

    override fun viewClicked(torrentFile: TorrentFile, action: TorrentFilesAdapter.Companion.ClickTypes) {
        when(action){
            TorrentFilesAdapter.Companion.ClickTypes.DOWNLOAD -> {torrentRepository.getTorrentFileData(torrentHash, torrentFile.getFullPath())}
            TorrentFilesAdapter.Companion.ClickTypes.PLAY -> {context.openTorrent(torrentHash, torrentFile.getFullPath())}
        }
    }
}