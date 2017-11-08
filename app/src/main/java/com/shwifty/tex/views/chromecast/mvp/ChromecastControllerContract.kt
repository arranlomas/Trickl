package com.shwifty.tex.views.chromecast.mvp

import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.views.base.mvp.BaseContract

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
        fun togglePlayback()
        fun seek(perc: Long)
    }
}