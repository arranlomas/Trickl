package com.shwifty.tex


import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.dagger.network.TorrentRepositoryComponent
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.chromecast.CastHandler
import com.shwifty.tex.dialogs.DialogManager
import com.shwifty.tex.network.di.DaggerNetworkComponent
import com.shwifty.tex.network.di.NetworkComponent

/**
 * Created by arran on 29/04/2017.
 */
object Trickl {
    val dialogManager = DialogManager()
    lateinit var tricklComponent: TricklComponent
    lateinit var torrentRepository: ITorrentRepository
    lateinit var networkComponent: NetworkComponent

    fun install(torrentRepositoryComponent: TorrentRepositoryComponent) {
        torrentRepository = torrentRepositoryComponent.getTorrentRepository()
        tricklComponent = DaggerTricklComponent.create()
        networkComponent = DaggerNetworkComponent.create()
        dialogManager.torrentRepository = Confluence.torrentRepository
    }
}