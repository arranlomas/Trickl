package com.shwifty.tex.views.browse.di

import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.browse.mvp.TorrentBrowseContract
import com.shwifty.tex.views.browse.mvp.TorrentBrowseFragment
import com.shwifty.tex.views.browse.mvp.TorrentBrowseInteractor
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 28/10/2017.
 */
@Module
class TorrentBrowseModule {
    @Provides
    internal fun providesTorrentBrowseInteractor(torrentSearchRepository: ITorrentSearchRepository): TorrentBrowseContract.Interactor {
        return TorrentBrowseInteractor(torrentSearchRepository)
    }
}

@Module
abstract class TorrentBroseFragmentBuilder {
    @ContributesAndroidInjector(modules = arrayOf(TorrentBrowseModule::class))
    internal abstract fun bindsTorrentBrowseFragment(): TorrentBrowseFragment
}
