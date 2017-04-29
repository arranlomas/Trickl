package com.schiwfty.tex.dagger.network


import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.dagger.context.ContextModule
import com.schiwfty.tex.retrofit.ClientAPI
import com.schiwfty.tex.views.splash.mvp.SplashPresenter
import dagger.Component

/**
 * Created by arran on 15/02/2017.
 */

@NetworkScope
@Component(modules = arrayOf(NetworkModule::class, ContextModule::class))
interface NetworkComponent {
    fun getClientApi(): ClientAPI
    fun injectSplashPresenter(splashPresenter: SplashPresenter)
}




