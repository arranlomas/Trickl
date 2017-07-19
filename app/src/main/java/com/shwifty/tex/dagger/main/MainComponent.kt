package com.shwifty.tex.dagger.main


import com.shwifty.tex.views.main.mvp.MainContract
import dagger.Component
import javax.inject.Singleton

/**
 * Created by arran on 15/02/2017.
 */
@Singleton
@Component(modules = arrayOf(MainModule::class))
interface MainComponent {
    fun getMainPresenter(): MainContract.Presenter
}


