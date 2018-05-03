package com.shwifty.tex.views.search.di

import android.arch.lifecycle.ViewModel
import com.arranlomas.daggerviewmodelhelper.ViewModelKey
import com.shwifty.tex.di.ViewModelFactoryModule
import com.shwifty.tex.views.search.mvi.TorrentSearchFragment
import com.shwifty.tex.views.search.mvi.TorrentSearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by arran on 28/10/2017.
 */
@Module(includes = arrayOf(ViewModelFactoryModule::class))
abstract class TorrentSearchFragmentBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindsTorrentSearchFragment(): TorrentSearchFragment

    @Binds
    @IntoMap
    @ViewModelKey(TorrentSearchViewModel::class)
    abstract fun bindTorrentSearchViewModel(viewModel: TorrentSearchViewModel): ViewModel
}
