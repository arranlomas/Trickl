package com.shwifty.tex.views.torrentfiles.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.views.torrentfiles.list.TorrentFilesAdapter

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentFilesContract {
    interface View {
        fun setupViewFromTorrentInfo(torrentInfo: TorrentInfo)
        fun updateTorrentPercentages(updatedDetails: List<TorrentFile>)
        fun dismiss()
        fun showError(stringId: Int)
    }

    interface Presenter {
        fun setup(context: Context, view: TorrentFilesContract.View, arguments: Bundle?)
        fun destroy()
        fun loadTorrent(torrentHash: String)
        var torrentHash: String
        fun viewClicked(torrentFile: TorrentFile, action: TorrentFilesAdapter.Companion.ClickTypes)
    }
}