package com.schiwfty.tex.tools.dagger.main

import com.schiwfty.tex.tools.dagger.network.NetworkComponent
import com.schiwfty.tex.views.main.MainActivity

import dagger.Component

/**
 * Created by arran on 16/02/2017.
 */

@MainScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(NetworkComponent::class))
interface MainComponent {
    fun injectMain(mainActivity: MainActivity)
}
