package com.shwifty.tex.views.chromecast.mvp

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.MyApplication
import com.shwifty.tex.chromecast.CastHandler
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 7/05/2017.
 */
class ChromecastPresenter(val torrentRepository: ITorrentRepository) : BasePresenter<ChromecastContract.View>(), ChromecastContract.Presenter {
    val castHandler = MyApplication.castHandler

    override fun setup() {
        castHandler.getStatus()
                .subscribe(object : BaseSubscriber<CastHandler.PlayerState>() {
                    override fun onNext(state: CastHandler.PlayerState) {
                        mvpView.updatePlayPauseButton(state)
                    }
                })
    }

    override fun togglePlayback() {
        castHandler.togglePlayback()
                .subscribe(object : BaseSubscriber<CastHandler.PlayerState>() {
                    override fun onNext(state: CastHandler.PlayerState) {
                        mvpView.updatePlayPauseButton(state)
                    }
    })
    }
}