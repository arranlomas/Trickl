package com.schiwfty.tex.confluence

import android.content.Context
import android.util.Log
import com.schiwfty.tex.utils.Constants
import com.schiwfty.tex.utils.captureOutput
import rx.Observable
import java.io.IOException
import java.io.InputStream
import java.net.ServerSocket


/**
 * Created by arran on 11/04/2017.
 */
object Confluence {

    fun startConfluence(): Boolean {
        val mainThread = Thread{
            balls.Balls.main()
        }
        mainThread.start()
        return true
    }
    fun setClientAddress() {
        var port = getAvailablePort().toString()
        port = "8080"
        val addr = "http://" + Constants.localhostUrl + port
        Log.v("AVAILABLE ADDR", addr)
        Constants.daemonPort = port
        Constants.fullUrl = addr
    }

    private fun getAvailablePort(): Int {
        var s: ServerSocket? = null
        var streamPort = -1
        try {
            s = ServerSocket(0)
            streamPort = s.localPort
            return streamPort
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (s != null)
                try {
                    s.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

        }
        return streamPort
    }


}