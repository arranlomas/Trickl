package com.schiwfty.tex.confluence

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.schiwfty.tex.R
import com.schiwfty.tex.views.main.mvp.MainActivity

/**
 * Created by arran on 17/04/2017.
 */
class ConfluenceDaemonService : Service() {
    private val NOTIFICATION_ID = 12345
    var STOP_STRING = "STOP"

    companion object {
        val TAG = "DAEMON_SERVICE_TAG"
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Confluence.startConfluence()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var action: String? = null
        if (intent != null) {
            action = intent.action
        }
        if (intent != null && action != null && action == STOP_STRING) {
            stopForeground(true)
        } else if (intent != null) {
            val exitIntent = Intent(this, ConfluenceDaemonService::class.java)
            exitIntent.action = STOP_STRING
            val pendingExit = PendingIntent.getService(this, 0, exitIntent, 0)

            val targetIntent = Intent(this, MainActivity::class.java)
            val pIntent = PendingIntent.getActivity(this, 0, targetIntent, 0)
            val builder = NotificationCompat.Builder(this)
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.trickl_notification)
                    .setContentText("Daemon running")
                    .addAction(R.drawable.trickl_notification, STOP_STRING, pendingExit)
                    .setContentIntent(pIntent)

            startForeground(NOTIFICATION_ID, builder.build())
        }

        return super.onStartCommand(intent, flags, startId)
    }

}