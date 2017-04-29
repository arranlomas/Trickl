package com.schiwfty.tex.views.main.mvp

import android.content.Context
import com.schiwfty.tex.views.all.mvp.AllContract

/**
 * Created by arran on 16/04/2017.
 */
class AllPresenter : AllContract.Presenter {

    lateinit var view: AllContract.View

    override fun setup(context: Context, view: AllContract.View) {
        this.view = view
    }

    override fun getTorrentInfo(hash: String) {

    }
}