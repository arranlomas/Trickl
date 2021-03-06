package com.shwifty.tex.views.settings.di

import android.arch.lifecycle.ViewModel
import com.arranlomas.daggerviewmodelhelper.ViewModelKey
import com.shwifty.tex.di.ViewModelFactoryModule
import com.shwifty.tex.views.settings.mvi.SettingsActivity
import com.shwifty.tex.views.settings.mvi.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by arran on 28/10/2017.
 */
@Module(includes = arrayOf(ViewModelFactoryModule::class))
abstract class SettingsActivityBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): SettingsActivity

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: SettingsViewModel): ViewModel
}
