package com.shwifty.tex

import android.arch.lifecycle.ViewModelProvider
import com.arranlomas.daggerviewmodelhelper.ViewModelFactory
import com.shwifty.tex.repository.network.di.ApiModule
import com.shwifty.tex.repository.network.di.UnscopedRepositoryModule
import com.shwifty.tex.views.addtorrent.di.AddTorrentActivityBuilder
import com.shwifty.tex.views.settings.di.SettingsActivityBuilder
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by arran on 8/10/2017.
 */
@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        SettingsActivityBuilder::class,
        ApiModule::class,
        UnscopedRepositoryModule::class,
        AddTorrentActivityBuilder::class))
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: MyApplication)
}


@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}


