package com.schiwfty.tex.dagger.network


import com.schiwfty.tex.dagger.context.ContextModule
import com.schiwfty.tex.retrofit.HttpController
import com.schiwfty.tex.splash.SplashActivity
import com.schiwfty.tex.splash.SplashPresenter

import dagger.Component

/**
 * Created by arran on 15/02/2017.
 */

@NetworkScope
@Component(modules = arrayOf(NetworkModule::class, ContextModule::class))
interface NetworkComponent {
    fun httpController(): HttpController

    fun injectSplashView(splashActivity: SplashActivity)
    fun injectSplashPresenter(splashPresenter: SplashPresenter)
}



