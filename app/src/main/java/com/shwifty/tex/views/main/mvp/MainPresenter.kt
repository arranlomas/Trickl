package com.shwifty.tex.views.main.mvp

import android.content.Context
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.R
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.utils.isChromecastAvailable
import com.shwifty.tex.views.base.mvp.BasePresenter

/**
 * Created by arran on 16/04/2017.
 */
class MainPresenter(val torrentRepository: ITorrentRepository,
                    val castHandler: ICastHandler,
                    val navigation: INavigation) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun attachView(mvpView: MainContract.View) {
        super.attachView(mvpView)
        castHandler.stateListener
                .subscribe(object : BaseSubscriber<ICastHandler.PlayerState>() {
                    override fun onNext(state: ICastHandler.PlayerState?) {
                        when (state) {
                            ICastHandler.PlayerState.DISCONNECTED, ICastHandler.PlayerState.OTHER, null -> mvpView.showChromecastController(false)
                            else -> mvpView.showChromecastController(true)
                        }
                    }
                })
                .addSubscription()
    }

    override fun initializeCastContext(context: Context) {
        if(context.isChromecastAvailable()){
            castHandler.initializeCastContext(context)
            castHandler.addSessionListener()
        }
        else mvpView.showError(R.string.error_chromecast_not_available)
    }

    override fun addSessionListener() {
        castHandler.addSessionListener()
    }

    override fun removeSessionListener() {
        castHandler.removeSessionListener()
    }
}