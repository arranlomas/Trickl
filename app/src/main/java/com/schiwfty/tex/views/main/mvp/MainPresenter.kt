package com.schiwfty.tex.views.main.mvp

import android.content.Context

/**
 * Created by arran on 16/04/2017.
 */
class MainPresenter : MainContract.Presenter {
    lateinit var view: MainContract.View

    override fun setup(context: Context, view: MainContract.View) {
        this.view = view
    }


}