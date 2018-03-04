package com.shwifty.tex


/**
 * Created by arran on 29/04/2017.
 */
object Trickl {
    lateinit var clientPrefs: ClientPrefs

    fun install(clientPrefs: ClientPrefs) {
        this.clientPrefs = clientPrefs
    }
}