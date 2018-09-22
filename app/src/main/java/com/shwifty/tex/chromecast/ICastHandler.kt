package com.shwifty.tex.chromecast

import android.content.Context
import com.schiwfty.torrentwrapper.models.TorrentFile
import rx.Observable
import rx.subjects.BehaviorSubject

/**
 * Created by arran on 8/10/2017.
 */
interface ICastHandler {
    val stateListener: BehaviorSubject<PlayerState>
    val progressUpdateListener: BehaviorSubject<Pair<Long, Long>>
    val onMetadataChangedListener: BehaviorSubject<String>

    enum class PlayerState {
        CONNECTED,
        DISCONNECTED,
        PLAYING,
        PAUSED,
        BUFFERING,
        LIVE_STREAM,
        PLAYING_AD,
        LOADING_NEXT_ITEM,
        OTHER
    }

    fun addListener()

    fun initializeCastContext(context: Context)

    fun loadRemoteMedia(torrentFile: TorrentFile, result: (Int) -> Unit)

    fun addSessionListener()

    fun removeSessionListener()

    fun seek(seekPosition: Long): Observable<Pair<Long, Long>>

    fun togglePlayback(): Observable<PlayerState>

    fun pause(): Observable<Boolean>

    fun play(): Observable<Boolean>

    fun getStatus(): Observable<PlayerState>

    fun getPosition(): Observable<Pair<Long, Long>>

    fun getTitle(): Observable<String>

}