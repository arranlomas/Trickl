package com.schiwfty.tex.confluence

import android.os.Environment
import java.io.File
import java.io.IOException
import java.net.ServerSocket


/**
 * Created by arran on 11/04/2017.
 */
object Confluence {
    lateinit var fullUrl: String
    val localhostUrl = "localhost:"
    lateinit var daemonPort: String
    val workingDir: File = File(Environment.getExternalStorageDirectory().path + File.separator + "Trickl")
    val torrentRepo: File = File(workingDir.absolutePath + File.separator + "torrents")
    var announceList: Array<String> = arrayOf(
            "http://182.176.139.129:6969/announce",
            "http://atrack.pow7.com/announce",
            "http://p4p.arenabg.com:1337/announce",
            "http://tracker.kicks-ass.net/announce",
            "http://tracker.thepiratebay.org/announce",
            "http://bttracker.crunchbanglinux.org:6969/announce",
            "http://tracker.aletorrenty.pl:2710/announce",
            "http://tracker.tfile.me/announce",
            "http://tracker.trackerfix.com/announce")


    fun startConfluence(): Boolean {
        daemonPort = "8080"
        fullUrl = "$localhostUrl$daemonPort"
        val mainThread = Thread {
            trickl.Trickl.androidMain(workingDir.absolutePath)
        }
        mainThread.start()
        return true
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