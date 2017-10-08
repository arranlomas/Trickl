package com.shwifty.tex.views.chromecast.mvp

import android.content.Context
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.views.base.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface ChromecastControllerContract {
    interface View : BaseContract.MvpView {
        fun updatePlayPauseButton(state: ICastHandler.PlayerState)
        fun updateProgress(position: Long, duration: Long)
        fun setTitle(title: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun setup()
        fun initializeCastContext(context: Context)
        fun addSessionListener()
        fun removeSessionListener()
        fun togglePlayback()
        fun seek(perc: Long)
    }
}