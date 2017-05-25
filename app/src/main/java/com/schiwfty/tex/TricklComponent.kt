package com.schiwfty.tex


import com.schiwfty.tex.dagger.main.DaggerMainComponent
import com.schiwfty.tex.dagger.main.MainComponent
import com.schiwfty.tex.dagger.network.NetworkModule

/**
 * Created by arran on 29/04/2017.
 */
object TricklComponent {
    lateinit var mainComponent: MainComponent

    fun install() {
        mainComponent = DaggerMainComponent.builder()
                .networkModule(NetworkModule())
                .build()
        //TODO set client address here!
    }
}