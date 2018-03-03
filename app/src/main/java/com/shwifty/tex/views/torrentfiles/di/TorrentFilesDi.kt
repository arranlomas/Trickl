package com.shwifty.tex.views.torrentfiles.di


import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.torrentfiles.mvp.TorrentFilesContract
import com.shwifty.tex.views.torrentfiles.mvp.TorrentFilesFragment
import com.shwifty.tex.views.torrentfiles.mvp.TorrentFilesPresenter
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 15/02/2017.
 */

@Module
class TorrentFilesModule {
    @Provides
    internal fun providesTorrentFilesPresenter(torrentRepository: ITorrentRepository): TorrentFilesContract.Presenter {
        return TorrentFilesPresenter(torrentRepository)
    }
}


@Module
abstract class TorrentFileFragmentBuilder {
    @ContributesAndroidInjector(modules = arrayOf(TorrentFilesModule::class))
    internal abstract fun torrentFilesFragment(): TorrentFilesFragment
}


