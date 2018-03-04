package com.shwifty.tex.views.torrentfiles.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.views.base.mvp.BaseContract
import com.shwifty.tex.views.torrentfiles.list.TorrentFilesAdapter

/**
 * Created by arran on 7/05/2017.
 */
interface TorrentFilesContract {
    interface View: BaseContract.MvpView {
        fun setupViewFromTorrentInfo(torrentInfo: TorrentInfo)
        fun updateTorrentPercentages(updatedDetails: List<TorrentFile>)
        fun dismiss()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun setup(arguments: Bundle?)
        fun loadTorrent()
        fun viewClicked(context: Context, torrentFile: TorrentFile, action: TorrentFilesAdapter.Companion.ClickTypes)
    }
}