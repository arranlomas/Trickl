package com.shwifty.tex.views.downloads.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.base.mvp.BaseContract
import com.shwifty.tex.views.downloads.list.FileDownloadAdapter

/**
 * Created by arran on 7/05/2017.
 */
interface FileDownloadContract {
    interface View: BaseContract.MvpView {
        fun setupViewFromTorrentInfo(torrentFiles: List<TorrentFile>)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun setup(arguments: Bundle?)
        fun refresh()
        fun viewClicked(context: Context, torrentFile: TorrentFile, action: FileDownloadAdapter.Companion.ClickTypes)
    }
}