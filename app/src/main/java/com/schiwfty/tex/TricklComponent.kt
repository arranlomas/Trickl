package com.schiwfty.tex

import com.schiwfty.tex.dagger.network.DaggerNetworkComponent
import com.schiwfty.tex.dagger.network.NetworkComponent
import com.schiwfty.tex.dagger.network.NetworkModule

/**
 * Created by arran on 29/04/2017.
 */
object TricklComponent {
    lateinit var networkComponent: NetworkComponent

    fun install() {
        networkComponent = DaggerNetworkComponent.builder()
                .networkModule(NetworkModule())
                .build()
        //TODO set client address here!
    }
}