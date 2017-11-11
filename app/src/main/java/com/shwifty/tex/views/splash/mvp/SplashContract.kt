package com.shwifty.tex.views.splash.mvp

import android.app.Activity
import android.content.Intent
import com.shwifty.tex.views.base.mvp.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface SplashContract {
    interface View : BaseContract.MvpView {
        fun progressToMain()
    }

    interface Presenter : BaseContract.Presenter<View> {
        var magnet: String?
        fun startConfluenceDaemon(activity: Activity)
        fun handleIntent(intent: Intent)
    }
}