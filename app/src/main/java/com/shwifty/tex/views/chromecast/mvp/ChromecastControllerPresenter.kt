package com.shwifty.tex.views.chromecast.mvp

import android.content.Context
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 7/05/2017.
 */
class ChromecastControllerPresenter(val torrentRepository: ITorrentRepository, val castHandler: ICastHandler) : BasePresenter<ChromecastControllerContract.View>(), ChromecastControllerContract.Presenter {

    override fun setup() {
        castHandler.getStatus()
                .subscribe(PlaybackStateSubscriber())
                .addSubscription()

        castHandler.getPosition()
                .subscribe(PositionSubscriber())
                .addSubscription()

        castHandler.stateListener
                .subscribe(PlaybackStateSubscriber())
                .addSubscription()

        castHandler.progressUpdateListener
                .subscribe(PositionSubscriber())
                .addSubscription()
    }

    override fun initializeCastContext(context: Context) {
        castHandler.initializeCastContext(context)
        castHandler.addSessionListener()
    }

    override fun addSessionListener() {
        castHandler.addListener()
    }

    override fun removeSessionListener() {
        castHandler.removeSessionListener()
    }


    override fun togglePlayback() {
        castHandler.togglePlayback()
                .subscribe(PlaybackStateSubscriber())
                .addSubscription()
    }

    override fun seek(perc: Long) {
        castHandler.seek(perc)
                .subscribe(PositionSubscriber())
                .addSubscription()
    }

    inner class PlaybackStateSubscriber : BaseSubscriber<ICastHandler.PlayerState>() {
        override fun onNext(state: ICastHandler.PlayerState) {
            mvpView.updatePlayPauseButton(state)
        }
    }

    inner class PositionSubscriber : BaseSubscriber<Pair<Long, Long>>() {
        override fun onNext(pair: Pair<Long, Long>) {
            mvpView.updateProgress(pair.first, pair.second)
        }
    }


}