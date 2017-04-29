package com.schiwfty.tex.utils

import android.os.Environment
import java.io.File

/**
 * Created by arran on 11/04/2017.
 */
object Constants {
    lateinit var fullUrl: String
    val confluenceFileName = "confluence"
    val localhostUrl = "127.0.0.1:"
    lateinit var daemonPort: String
    val torrentRepo: File = File(Environment.getExternalStorageDirectory().path + File.separator + "cloudburst")
}

