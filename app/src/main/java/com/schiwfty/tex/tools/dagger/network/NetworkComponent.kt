package com.schiwfty.tex.tools.dagger.network


import com.schiwfty.tex.tools.dagger.context.ContextModule
import com.schiwfty.tex.tools.retrofit.HttpController
import com.schiwfty.tex.views.splash.mvp.SplashActivity
import com.schiwfty.tex.views.splash.mvp.SplashPresenter

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



