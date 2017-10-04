package com.shwifty.tex.chromecast

import android.content.Context
import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.utils.getMimeType
import com.shwifty.tex.utils.buildMediaInfo

/**
 * Created by arran on 4/06/2017.
 */
class CastHandler {
    private var mCastSession: CastSession? = null
    private var mCastContext: CastContext? = null

    fun initializeCastContext(context: Context) {
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
                Log.v("CHROMECAST", "session STARTED")
            }

            private fun onApplicationDisconnected() {
                mCastSession = null
                Log.v("CHROMECAST", "session ENDED")
            }
        }
    }

    fun loadRemoteMedia(torrentFile: TorrentFile): Boolean {
        if (mCastSession == null) {
            return false
        }
        val remoteMediaClient = mCastSession?.remoteMediaClient ?: return false
        remoteMediaClient.load(torrentFile.buildMediaInfo(torrentFile.getMimeType()), true)
        return true
    }

    fun addSessionListener() {
        mCastContext?.sessionManager?.addSessionManagerListener(
                mSessionManagerListener, CastSession::class.java)
    }

    fun removeSessionListener() {
        mCastContext?.sessionManager?.removeSessionManagerListener(
                mSessionManagerListener, CastSession::class.java)
    }

    fun getmCastContext(): CastContext? {
        return mCastContext
    }

    fun getApproximatePosition(): Int{
        val position = mCastSession?.remoteMediaClient?.approximateStreamPosition
        val duration = mCastSession?.remoteMediaClient?.streamDuration
        Log.v("CHROMECAST", "position: $position")
        Log.v("CHROMECAST", "duration: $duration")
        return 0
    }

    companion object {
        private var mSessionManagerListener: SessionManagerListener<CastSession>? = null
    }
}