package com.schiwfty.tex

import android.content.Context
import com.schiwfty.tex.dagger.context.ContextModule
import com.schiwfty.tex.dagger.network.DaggerNetworkComponent
import com.schiwfty.tex.dagger.network.NetworkComponent
import com.schiwfty.tex.dagger.network.NetworkModule

/**
 * Created by arran on 29/04/2017.
 */
object TricklComponent {
    lateinit var networkComponent: NetworkComponent

    fun install(context: Context) {
        networkComponent = DaggerNetworkComponent.builder()
                .networkModule(NetworkModule())
                .contextModule(ContextModule(context))
                .build()
        //TODO set client address here!
    }
}