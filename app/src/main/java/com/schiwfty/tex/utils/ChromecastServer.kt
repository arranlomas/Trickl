package com.schiwfty.tex.utils

import android.util.Log
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import fi.iki.elonen.NanoHTTPD
import java.io.IOException
import javax.inject.Inject


/**
 * Created by arran on 27/05/2017.
 */
class ChromecastServer @Throws(IOException::class)
constructor() : NanoHTTPD(9090) {

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    init {
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false)
        TricklComponent.mainComponent.inject(this)
        Log.v("Server","Running! Point your browsers to http://localhost:9090/ ")
    }

    override fun serve(session: IHTTPSession): NanoHTTPD.Response {

        var msg = "<html><body><h1>Hello server</h1>\n"
        val parms = session.parms
        if (parms["username"] == null) {
            msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n"
        } else {
            msg += "<p>Hello, " + parms["username"] + "!</p>"
        }
        return newFixedLengthResponse(msg + "</body></html>\n")
    }
}