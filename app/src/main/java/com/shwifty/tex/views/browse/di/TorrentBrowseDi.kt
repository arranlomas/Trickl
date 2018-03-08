package com.shwifty.tex.views.browse.di

import android.arch.lifecycle.ViewModel
import com.arranlomas.daggerviewmodelhelper.ViewModelKey
import com.shwifty.tex.di.ViewModelFactoryModule
import com.shwifty.tex.views.browse.mvp.TorrentBrowseFragment
import com.shwifty.tex.views.browse.mvp.TorrentBrowseViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by arran on 28/10/2017.
 */
@Module(includes = arrayOf(ViewModelFactoryModule::class))
abstract class TorrentBrowseFragmentBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindsTorrentBrowseFragment(): TorrentBrowseFragment

    @Binds
    @IntoMap
    @ViewModelKey(TorrentBrowseViewModel::class)
    abstract fun bindTorrentBrowseViewModel(viewModel: TorrentBrowseViewModel): ViewModel
}
