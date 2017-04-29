package com.schiwfty.tex.views.splash.mvp

import android.content.Context

/**
 * Created by arran on 16/04/2017.
 */
interface SplashContract {
    interface View {
        fun progressToMain()
        fun showError(stringId: Int)
        fun showInfo(stringId: Int)
        fun showSuccess(stringId: Int)
    }

    interface Presenter {
        fun startConfluenceDaemon(context: Context)
        fun setup(context: Context, view: View)
        fun destroy()
    }
}