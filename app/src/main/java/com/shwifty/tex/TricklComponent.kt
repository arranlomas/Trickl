package com.shwifty.tex


import com.shwifty.tex.dagger.main.DaggerMainComponent
import com.shwifty.tex.dagger.main.MainComponent
import com.shwifty.tex.dagger.network.NetworkModule
import com.shwifty.tex.views.main.DialogManager

/**
 * Created by arran on 29/04/2017.
 */
object TricklComponent {
    lateinit var mainComponent: MainComponent
    val dialogManager = DialogManager()


    fun install() {
        mainComponent = DaggerMainComponent.builder()
                .networkModule(NetworkModule())
                .build()
        //TODO set client address here!
    }
}