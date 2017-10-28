package com.shwifty.tex.dialogs

import android.content.Context
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 10/05/2017.
 */
interface IDialogManager {
    var torrentRepository: ITorrentRepository
    fun showAddMagnetDialog(context: Context)
    fun showAddHashDialog(context: Context)
    fun showDeleteTorrentDialog(context: Context, torrentInfo: TorrentInfo, onError: () -> Unit)
    fun showDeleteFileDialog(context: Context, torrentFile: TorrentFile)
    fun showNoWifiDialog(context: Context, torrentFile: TorrentFile)
    fun showExitAppDialog(context: Context, onExit: () -> Unit)
    fun showBrowseFilterDialog(context: Context,
                               defaultSortType: TorrentSearchSortType,
                               defaultCategory: TorrentSearchCategory,
                               onConfirm: (TorrentSearchSortType, TorrentSearchCategory) -> Unit)
}