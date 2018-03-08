package com.shwifty.tex.views.addtorrent.di

import android.arch.lifecycle.ViewModel
import com.arranlomas.daggerviewmodelhelper.ViewModelKey
import com.shwifty.tex.di.ViewModelFactoryModule
import com.shwifty.tex.views.addtorrent.mvi.AddTorrentActivity
import com.shwifty.tex.views.addtorrent.mvi.AddTorrentViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by arran on 15/02/2017.
 */
@Module(includes = arrayOf(ViewModelFactoryModule::class))
abstract class AddTorrentActivityBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindsAddTorrentActivity(): AddTorrentActivity

    @Binds
    @IntoMap
    @ViewModelKey(AddTorrentViewModel::class)
    abstract fun bindAddTorrentViewModel(viewModel: AddTorrentViewModel): ViewModel
}
