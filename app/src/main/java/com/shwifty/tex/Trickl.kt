package com.shwifty.tex


import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.dialogs.DialogManager

/**
 * Created by arran on 29/04/2017.
 */
object Trickl {
    val dialogManager = DialogManager()
    lateinit var clientPrefs: ClientPrefs

    fun install(clientPrefs: ClientPrefs) {
        this.clientPrefs = clientPrefs
        dialogManager.torrentRepository = Confluence.torrentRepository
    }
}