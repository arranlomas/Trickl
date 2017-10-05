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

    inner class PlaybackStateSubscriber: BaseSubscriber<CastHandler.PlayerState>(){
        override fun onNext(state: CastHandler.PlayerState) {
            mvpView.updatePlayPauseButton(state)
        }
    }

    inner class PositionSubscriber : BaseSubscriber<Pair<Long, Long>>() {
        override fun onNext(pair: Pair<Long, Long>) {
            mvpView.updateProgress(pair.first, pair.second)
        }
    }


}