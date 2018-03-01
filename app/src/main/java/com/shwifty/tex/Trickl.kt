package com.shwifty.tex


import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.dialogs.DialogManager
import com.shwifty.tex.repository.network.di.DaggerRepositoryComponent
import com.shwifty.tex.repository.network.di.RepositoryComponent

/**
 * Created by arran on 29/04/2017.
 */
object Trickl {
    val dialogManager = DialogManager()
    lateinit var repositoryComponent: RepositoryComponent
    lateinit var clientPrefs: ClientPrefs

    fun install(clientPrefs: ClientPrefs) {
        this.clientPrefs = clientPrefs
        repositoryComponent = DaggerRepositoryComponent.create()
        dialogManager.torrentRepository = Confluence.torrentRepository
    }
}