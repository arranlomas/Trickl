package com.shwifty.tex.views.main

import android.app.FragmentManager
import android.content.Context
import com.shwifty.tex.models.TorrentFile
import com.shwifty.tex.models.TorrentInfo
import com.shwifty.tex.repositories.ITorrentRepository
import com.shwifty.tex.views.main.mvp.MainContract
import javax.inject.Inject

/**
 * Created by arran on 10/05/2017.
 */
interface IDialogManager {
    var mainPresenter: MainContract.Presenter
    var torrentRepository: ITorrentRepository
    fun showAddMagnetDialog(fragmentManager: FragmentManager)
    fun showAddHashDialog(fragmentManager: FragmentManager)
    fun showDeleteTorrentDialog(fragmentManager: FragmentManager,  torrentInfo: TorrentInfo)
    fun showDeleteFileDialog(fragmentManager: FragmentManager,  torrentFile: TorrentFile)
    fun showNoWifiDialog(context: Context, torrentFile: TorrentFile)
}