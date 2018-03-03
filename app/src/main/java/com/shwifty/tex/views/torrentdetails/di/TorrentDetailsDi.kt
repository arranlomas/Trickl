package com.shwifty.tex.views.torrentdetails.di


import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.torrentdetails.mvp.TorrentDetailsContract
import com.shwifty.tex.views.torrentdetails.mvp.TorrentDetailsFragment
import com.shwifty.tex.views.torrentdetails.mvp.TorrentDetailsPresenter
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 15/02/2017.
 */
@Module
class TorrentDetailsModule {
    @Provides
    internal fun providesTorrentDetailsPresenter(torrentRepository: ITorrentRepository): TorrentDetailsContract.Presenter {
        return TorrentDetailsPresenter(torrentRepository)
    }
}

@Module()
abstract class TorrentDetailsFragmentBuilder {
    @ContributesAndroidInjector(modules = arrayOf(TorrentDetailsModule::class))
    internal abstract fun bindsTorrentDetailsFragment(): TorrentDetailsFragment
}


