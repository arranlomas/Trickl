package com.schiwfty.tex.views.splash.mvp

import android.content.Context
import android.content.Intent

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
        var magnet: String?
        fun startConfluenceDaemon(context: Context)
        fun setup(context: Context, view: View)
        fun handleIntent(intent: Intent)
    }
}