package com.schiwfty.tex.dagger.main

import com.schiwfty.tex.dagger.network.NetworkComponent
import com.schiwfty.tex.main.MainActivity

import dagger.Component

/**
 * Created by arran on 16/02/2017.
 */

@MainScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(NetworkComponent::class))
interface MainComponent {
    fun injectMain(mainActivity: MainActivity)
}
