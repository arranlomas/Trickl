package com.schiwfty.tex.views.main

import android.app.FragmentManager
import com.schiwfty.tex.dialogs.AddHashDialog
import com.schiwfty.tex.dialogs.AddMagnetDialog
import com.schiwfty.tex.dialogs.DeleteFileDialog
import com.schiwfty.tex.dialogs.DeleteTorrentDialog

/**
 * Created by arran on 10/05/2017.
 */
class DialogManager: IDialogManager {

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

    override fun showDeleteTorrentDialog(fragmentManager: FragmentManager, torrentName: String, torrentHash: String) {
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag(TAG_DIALOG)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment = DeleteTorrentDialog.newInstance(torrentName, torrentHash)
        newFragment.show(ft, TAG_DIALOG)
    }

    override fun showDeleteFileDialog(fragmentManager: FragmentManager, torrentHash: String, torrentName: String, fileName: String) {
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag(TAG_DIALOG)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment = DeleteFileDialog.newInstance(torrentHash, torrentName, fileName)
        newFragment.show(ft, TAG_DIALOG)
    }
}