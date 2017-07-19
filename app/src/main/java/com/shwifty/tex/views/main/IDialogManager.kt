package com.shwifty.tex.views.main

import android.app.FragmentManager
import android.content.Context
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.main.mvp.MainContract

/**
 * Created by arran on 10/05/2017.
 */
interface IDialogManager {
    var mainPresenter: MainContract.Presenter
    var torrentRepository: ITorrentRepository
    fun showAddMagnetDialog(fragmentManager: FragmentManager)
    fun showAddHashDialog(fragmentManager: FragmentManager)
    fun showDeleteTorrentDialog(context: Context, torrentInfo: TorrentInfo, onError: () -> Unit)
    fun showDeleteFileDialog(fragmentManager: FragmentManager,  torrentFile: TorrentFile)
    fun showNoWifiDialog(context: Context, torrentFile: TorrentFile)
}