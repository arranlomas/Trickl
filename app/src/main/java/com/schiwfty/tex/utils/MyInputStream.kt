package com.schiwfty.tex.utils

/**
 * Created by arran on 24/05/2017.
 */
class MyInputStream(fileSize: Long) {
    private var offset: Long = 0
    private var bytesLeft: Long = fileSize
    private val BUFFER = (128 shl 10).toLong()
    private var available: Long = fileSize



}