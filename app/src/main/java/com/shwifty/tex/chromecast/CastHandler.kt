package com.shwifty.tex.chromecast

import android.content.Context
import android.util.Log
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.utils.getMimeType
import com.shwifty.tex.utils.buildMediaInfo
import rx.Emitter
import rx.Observable
import rx.subjects.BehaviorSubject

/**
 * Created by arran on 4/06/2017.
 */
class CastHandler : ICastHandler {

    private var mCastSession: CastSession? = null
        private set(value) {
            field = value
            addListener()
        }
    private var mCastContext: CastContext? = null

    override val stateListener: BehaviorSubject<ICastHandler.PlayerState> = BehaviorSubject.create()
    override val progressUpdateListener: BehaviorSubject<Pair<Long, Long>> = BehaviorSubject.create()
    override val onMetadataChangedListener: BehaviorSubject<String> = BehaviorSubject.create()


    init {
        stateListener.onNext(ICastHandler.PlayerState.DISCONNECTED)
    }

    override fun addListener() {
        mCastSession?.remoteMediaClient?.addProgressListener({ progressMs, durationMs ->
            progressUpdateListener.onNext(Pair(progressMs, durationMs))
        }, 1000)
        mCastSession?.remoteMediaClient?.addListener(object : RemoteMediaClient.Listener {
            override fun onPreloadStatusUpdated() {
                stateListener.onNext(getStatusNonObservable())
            }

            override fun onSendingRemoteMediaRequest() {
                stateListener.onNext(getStatusNonObservable())
            }

            override fun onMetadataUpdated() {
                stateListener.onNext(getStatusNonObservable())
                onMetadataChangedListener.onNext(getTitleNonObservable())
            }

            override fun onAdBreakStatusUpdated() {
                stateListener.onNext(getStatusNonObservable())
            }

            override fun onStatusUpdated() {
                stateListener.onNext(getStatusNonObservable())
            }

            override fun onQueueStatusUpdated() {
                stateListener.onNext(getStatusNonObservable())
            }
        })
    }

    override fun initializeCastContext(context: Context) {
        mCastContext = CastContext.getSharedInstance(context)
        setupCastListener()
    }

    private fun setupCastListener() {
        mSessionManagerListener = object : SessionManagerListener<CastSession> {

            override fun onSessionEnded(session: CastSession, error: Int) {
                onApplicationDisconnected()
            }

            override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
                onApplicationConnected(session)
            }

            override fun onSessionResumeFailed(session: CastSession, error: Int) {
                onApplicationDisconnected()
            }

            override fun onSessionStarted(session: CastSession, sessionId: String) {
                onApplicationConnected(session)
            }

            override fun onSessionStartFailed(session: CastSession, error: Int) {
                onApplicationDisconnected()
            }

            override fun onSessionStarting(session: CastSession) {}

            override fun onSessionEnding(session: CastSession) {}

            override fun onSessionResuming(session: CastSession, sessionId: String) {}

            override fun onSessionSuspended(session: CastSession, reason: Int) {}

            private fun onApplicationConnected(castSession: CastSession) {
                mCastSession = castSession
                stateListener.onNext(ICastHandler.PlayerState.CONNECTED)
                Log.v("CHROMECAST", "session STARTED")
            }

            private fun onApplicationDisconnected() {
                mCastSession = null
                stateListener.onNext(ICastHandler.PlayerState.DISCONNECTED)
                Log.v("CHROMECAST", "session ENDED")
            }
        }
    }

    override fun loadRemoteMedia(torrentFile: TorrentFile): Boolean {
        if (mCastSession == null) {
            return false
        }
        val remoteMediaClient = mCastSession?.remoteMediaClient ?: return false
        remoteMediaClient.load(torrentFile.buildMediaInfo(torrentFile.getMimeType()), true)
        return true
    }

    override fun addSessionListener() {
        mCastContext?.sessionManager?.addSessionManagerListener(
                mSessionManagerListener, CastSession::class.java)
    }

    override fun removeSessionListener() {
        mCastContext?.sessionManager?.removeSessionManagerListener(
                mSessionManagerListener, CastSession::class.java)
    }

    override fun seek(seekPosition: Long): Observable<Pair<Long, Long>> {
        return Observable.create<Pair<Long, Long>>({ subscriber ->
            val result = mCastSession?.remoteMediaClient?.seek(seekPosition)
            result?.setResultCallback {
                if (it.status.isSuccess) {
                    val position = getPositionNonObservable()
                    if (position != null) subscriber.onNext(position)
                }
            }
        }, Emitter.BackpressureMode.BUFFER)
    }

    override fun togglePlayback(): Observable<ICastHandler.PlayerState> {
        return getStatus()
                .flatMap {
                    when (it) {
                        ICastHandler.PlayerState.PLAYING -> pause()
                        ICastHandler.PlayerState.PAUSED -> play()
                        else -> Observable.just(false)
                    }
                }
                .flatMap {
                    if (it) getStatus()
                    else Observable.just(ICastHandler.PlayerState.OTHER)
                }
    }

    override fun pause(): Observable<Boolean> {
        return Observable.create<Boolean>({ subscriber ->
            val result = mCastSession?.remoteMediaClient?.pause()
            result?.setResultCallback {
                if (it.status.isSuccess) subscriber.onNext(true)
                else subscriber.onError(IllegalStateException("Error pausing Chromecast"))
            } ?: subscriber.onError(IllegalStateException("Result from pause should not be null"))
        }, Emitter.BackpressureMode.BUFFER)
    }

    override fun play(): Observable<Boolean> {
        return Observable.create<Boolean>({ subscriber ->
            val result = mCastSession?.remoteMediaClient?.play()
            result?.setResultCallback {
                if (it.status.isSuccess) subscriber.onNext(true)
                else subscriber.onError(IllegalStateException("Error playing Chromecast"))
            } ?: subscriber.onError(IllegalStateException("Result from play should not be null"))
        }, Emitter.BackpressureMode.BUFFER)
    }

    private fun getStatusNonObservable(): ICastHandler.PlayerState {
        var state: ICastHandler.PlayerState = ICastHandler.PlayerState.DISCONNECTED
        mCastSession?.remoteMediaClient?.let {
            with(it, {
                if (isPlaying) state = ICastHandler.PlayerState.PLAYING
                else if (isPaused) state = ICastHandler.PlayerState.PAUSED
                else if (isBuffering) state = ICastHandler.PlayerState.BUFFERING
                else if (isLiveStream) state = ICastHandler.PlayerState.LIVE_STREAM
                else if (isPlayingAd) state = ICastHandler.PlayerState.PLAYING_AD
                else if (isLoadingNextItem) state = ICastHandler.PlayerState.LOADING_NEXT_ITEM
                else state = ICastHandler.PlayerState.OTHER
            })
        }
        return state
    }

    override fun getStatus(): Observable<ICastHandler.PlayerState> {
        return Observable.just(getStatusNonObservable())
    }

    private fun getPositionNonObservable(): Pair<Long, Long>? {
        val position = mCastSession?.remoteMediaClient?.approximateStreamPosition
        val duration = mCastSession?.remoteMediaClient?.streamDuration
        if (position != null && duration != null) return Pair(position, duration)
        else return null
    }

    override fun getPosition(): Observable<Pair<Long, Long>> {
        getPositionNonObservable()?.let {
            return Observable.just(it)
        } ?: return Observable.just(Pair(0L, 0L))
    }

    override fun getTitle(): Observable<String> {
        return Observable.just(getTitleNonObservable())
    }

    private fun getTitleNonObservable(): String {
        mCastSession?.remoteMediaClient?.let {
            return it.mediaInfo?.metadata?.getString(MediaMetadata.KEY_TITLE) ?: ""
        } ?: return ""
    }

    companion object {
        private var mSessionManagerListener: SessionManagerListener<CastSession>? = null
    }
}