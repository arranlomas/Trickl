package com.shwifty.tex.views.addtorrent.di

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.ViewModelFactoryModule
import com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity
import com.shwifty.tex.views.addtorrent.mvp.AddTorrentContract
import com.shwifty.tex.views.addtorrent.mvp.AddTorrentPresenter
import com.shwifty.tex.views.settings.mvi.SettingsActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 15/02/2017.
 */

@Module
class AddTorrentModule {
    @Provides
    internal fun providesAddTorrentPresenter(torrentRepository: ITorrentRepository): AddTorrentContract.Presenter {
        return AddTorrentPresenter(torrentRepository)
    }

}

@Module(includes = arrayOf(ViewModelFactoryModule::class))
abstract class AddTorrentActivityBuilder {
    @ContributesAndroidInjector(modules = arrayOf(AddTorrentModule::class))
    internal abstract fun bindSAddTorrentActivity(): AddTorrentActivity
}
