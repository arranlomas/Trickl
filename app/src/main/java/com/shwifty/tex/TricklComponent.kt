package com.shwifty.tex


import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.dagger.network.TorrentRepositoryComponent
import com.shwifty.tex.views.main.DialogManager

/**
 * Created by arran on 29/04/2017.
 */
object TricklComponent {
    val dialogManager = DialogManager()
    lateinit var torrentRepositoryComponent: TorrentRepositoryComponent

    fun install(torrentRepositoryComponent: TorrentRepositoryComponent) {
        this.torrentRepositoryComponent = torrentRepositoryComponent
        dialogManager.torrentRepository = Confluence.torrentRepository
    }
}