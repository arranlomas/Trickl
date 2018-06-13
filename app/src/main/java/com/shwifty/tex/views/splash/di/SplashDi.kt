package com.shwifty.tex.views.splash.di


import android.arch.lifecycle.ViewModel
import com.arranlomas.daggerviewmodelhelper.ViewModelKey
import com.shwifty.tex.views.splash.mvi.SplashActivity
import com.shwifty.tex.views.splash.mvi.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by arran on 15/02/2017.
 */
@Module
abstract class SplashActivityBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindsSplashActivity(): SplashActivity


    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel
}



