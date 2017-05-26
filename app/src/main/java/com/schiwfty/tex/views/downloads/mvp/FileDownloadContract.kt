package com.schiwfty.tex.views.downloads.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.views.downloads.list.FileDownloadAdapter

/**
 * Created by arran on 7/05/2017.
 */
interface FileDownloadContract {
    interface View {
        fun setupViewFromTorrentInfo(torrentFiles: List<TorrentFile>)
        fun showDeleteFileDialog(torrentHash: String, torrentName: String, fileName: String)
    }

    interface Presenter {
        fun setup(context: Context, view: FileDownloadContract.View, arguments: Bundle?)
        fun destroy()
        fun refresh()
        fun viewClicked(torrentFile: TorrentFile, action: FileDownloadAdapter.Companion.ClickTypes)
    }
}