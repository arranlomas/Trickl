package com.shwifty.tex.views.showtorrent.di


import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.showtorrent.mvp.TorrentInfoActivity
import com.shwifty.tex.views.showtorrent.mvp.TorrentInfoContract
import com.shwifty.tex.views.showtorrent.mvp.TorrentInfoPresenter
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 15/02/2017.
 */
@Module
class TorrentInfoModule {
    @Provides
    internal fun providesTorrentInfoPresenter(torrentRepository: ITorrentRepository): TorrentInfoContract.Presenter {
        return TorrentInfoPresenter(torrentRepository)
    }

}

@Module
abstract class TorrentInfoActivityBuilder {
    @ContributesAndroidInjector(modules = arrayOf(TorrentInfoModule::class))
    internal abstract fun bindsTorrentInfoActivity(): TorrentInfoActivity
}



