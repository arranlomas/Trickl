package com.shwifty.tex.views.showtorrent.di


import android.arch.lifecycle.ViewModel
import com.arranlomas.daggerviewmodelhelper.ViewModelKey
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoViewModel
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by arran on 15/02/2017.
 */
@Module
abstract class TorrentInfoActivityBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindsTorrentInfoActivity(): TorrentInfoActivity


    @Binds
    @IntoMap
    @ViewModelKey(TorrentInfoViewModel::class)
    abstract fun bindTorrentInfoViewModel(viewModel: TorrentInfoViewModel): ViewModel
}



