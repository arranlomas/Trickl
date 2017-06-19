package com.shwifty.tex.views.main

import android.app.FragmentManager
import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.shwifty.tex.R
import com.shwifty.tex.dialogs.AddHashDialog
import com.shwifty.tex.dialogs.AddMagnetDialog
import com.shwifty.tex.dialogs.DeleteFileDialog
import com.shwifty.tex.dialogs.DeleteTorrentDialog
import com.shwifty.tex.models.TorrentFile
import com.shwifty.tex.models.TorrentInfo
import com.shwifty.tex.repositories.ITorrentRepository
import com.shwifty.tex.utils.getFullPath
import com.shwifty.tex.views.main.mvp.MainContract


/**
 * Created by arran on 10/05/2017.
 */
class DialogManager: IDialogManager {
    override lateinit var mainPresenter: MainContract.Presenter
    override lateinit var torrentRepository: ITorrentRepository

    override fun showNoWifiDialog(context: Context,torrentFile: TorrentFile) {
        MaterialDialog.Builder(context)
                .title(R.string.dialog_title_no_wifi)
                .content(R.string.dialog_content_no_wifi)
                .positiveText(R.string.dialog_positive_no_wifi)
                .onPositive  ({ _, _ ->
                    torrentRepository.startFileDownloading(torrentFile, context, false)
                })
                .negativeText(R.string.dialog_negative_no_wifi)
                .show()
    }

    private var TAG_DIALOG = "dialog"

    override fun showAddMagnetDialog(fragmentManager: FragmentManager) {
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag(TAG_DIALOG)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment = AddMagnetDialog.newInstance()
        newFragment.show(ft, TAG_DIALOG)
    }

    override fun showAddHashDialog(fragmentManager: FragmentManager) {
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag(TAG_DIALOG)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment = AddHashDialog.newInstance()
        newFragment.show(ft, TAG_DIALOG)
    }

    override fun showDeleteTorrentDialog(fragmentManager: FragmentManager, torrentInfo: TorrentInfo) {
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag(TAG_DIALOG)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment = DeleteTorrentDialog.newInstance(torrentInfo.name, torrentInfo.info_hash)
        newFragment.show(ft, TAG_DIALOG)
    }

    override fun showDeleteFileDialog(fragmentManager: FragmentManager, torrentFile: TorrentFile) {
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag(TAG_DIALOG)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment = DeleteFileDialog.newInstance(torrentFile.torrentHash, torrentFile.parentTorrentName, torrentFile.getFullPath())
        newFragment.show(ft, TAG_DIALOG)
    }
}