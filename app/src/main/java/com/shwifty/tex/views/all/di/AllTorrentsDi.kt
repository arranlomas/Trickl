package com.shwifty.tex.views.all.di


import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.all.mvp.AllContract
import com.shwifty.tex.views.all.mvp.AllFragment
import com.shwifty.tex.views.all.mvp.AllPresenter
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 15/02/2017.
 */

@Module
class AllTorrentsModule {
    @Provides
    internal fun providesAllTorrentsPresenter(torrentRepository: ITorrentRepository): AllContract.Presenter {
        return AllPresenter(torrentRepository)
    }

}

@Module
abstract class AllTorrentsFragmentBuilder {
    @ContributesAndroidInjector(modules = arrayOf(AllTorrentsModule::class))
    internal abstract fun bindsAllTorrentsFragment(): AllFragment
}



