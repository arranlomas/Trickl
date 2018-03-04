package com.shwifty.tex.views.main.mvp

import android.content.Context
import android.content.Intent
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.utils.CONNECTIVITY_STATUS
import com.shwifty.tex.views.base.mvp.BaseContract

/**
 * Created by arran on 16/04/2017.
 */
interface MainContract {
    interface View : BaseContract.MvpView {
        fun showChromecastController(show: Boolean)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun initializeCastContext(context: Context)
        fun addSessionListener()
        fun removeSessionListener()
    }
}