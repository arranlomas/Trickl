package com.schiwfty.tex.views.all.mvp

import android.content.Context

/**
 * Created by arran on 16/04/2017.
 */
class AllPresenter : AllContract.Presenter {

    lateinit var view: AllContract.View
    lateinit var context: Context

    override fun setup(context: Context, view: AllContract.View) {
        this.view = view
        this.context = context
    }


}