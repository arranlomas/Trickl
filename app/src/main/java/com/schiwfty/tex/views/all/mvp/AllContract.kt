package com.schiwfty.tex.views.all.mvp

import android.content.Context

/**
 * Created by arran on 16/04/2017.
 */
interface AllContract {
    interface View {
        fun showError(stringId: Int)
        fun showInfo(stringId: Int)
        fun showSuccess(stringId: Int)
        fun updateStatus(string: String)
    }

    interface Presenter {
        fun setup(context: Context, view: View)
        fun testGetInfo(hash: String)
        fun getTorrentInfo(hash: String)
        fun updateStatus()
    }
}