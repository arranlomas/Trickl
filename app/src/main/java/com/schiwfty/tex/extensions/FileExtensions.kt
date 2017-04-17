package com.schiwfty.tex.extensions

import java.io.File
import java.io.InputStream

/**
 * Created by arran on 17/04/2017.
 */
fun File.copyInputStreamToFile(inputStream: InputStream) {
    inputStream.use { input ->
        this.outputStream().use { fileOut ->
            input.copyTo(fileOut)
        }
    }
}