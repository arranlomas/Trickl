package com.shwifty.tex.views.chromecast.mvp

import android.content.Context
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 7/05/2017.
 */
class ChromecastControllerPresenter(val torrentRepository: ITorrentRepository, val castHandler: ICastHandler)
    : BasePresenter<ChromecastControllerContract.View>(), ChromecastControllerContract.Presenter {

    override fun setup() {
        castHandler.getStatus()
                .subscribe(PlaybackStateSubscriber())
                .addSubscription()

        castHandler.getPosition()
                .subscribe(PositionSubscriber())
                .addSubscription()

        castHandler.getTitle()
                .subscribe(TitleSubscriber())
                .addSubscription()

        castHandler.stateListener
                .subscribe(PlaybackStateSubscriber())
                .addSubscription()

        castHandler.progressUpdateListener
                .subscribe(PositionSubscriber())
                .addSubscription()

        castHandler.onMetadataChangedListener
                .subscribe(TitleSubscriber())
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

    inner class TitleSubscriber : BaseSubscriber<String>() {
        override fun onNext(title: String?) {
            title?.let { mvpView.setTitle(it) }
        }
    }


}