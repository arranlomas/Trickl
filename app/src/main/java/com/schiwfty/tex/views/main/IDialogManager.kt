package com.schiwfty.tex.views.main

import android.app.FragmentManager

/**
 * Created by arran on 10/05/2017.
 */
interface IDialogManager {
    fun showAddMagnetDialog(fragmentManager: FragmentManager)
    fun showAddHashDialog(fragmentManager: FragmentManager)
    fun showDeleteTorrentDialog(fragmentManager: FragmentManager, torrentName: String, torrentHash: String)
    fun showDeleteFileDialog(fragmentManager: FragmentManager, torrentHash: String, torrentName: String, fileName: String)
}