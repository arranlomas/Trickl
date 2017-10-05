package com.shwifty.tex.views.chromecast.mvp

import com.shwifty.tex.chromecast.CastHandler
import com.shwifty.tex.views.base.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface ChromecastContract {
    interface View : BaseContract.MvpView {
        fun updatePlayPauseButton(state: CastHandler.PlayerState)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun setup()
        fun togglePlayback()
    }
}