package com.shwifty.tex.views.chromecast.di

import com.schiwfty.torrentwrapper.dagger.network.TorrentRepositoryComponent
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.chromecast.mvp.ChromecastActivity
import com.shwifty.tex.views.chromecast.mvp.ChromecastContract
import com.shwifty.tex.views.chromecast.mvp.ChromecastPresenter
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */
@PresenterScope
@Component(modules = arrayOf(ChromecastModule::class), dependencies = arrayOf(TorrentRepositoryComponent::class))
interface ChromecastComponent {
    fun inject(chromecastActivity: ChromecastActivity)
}

@Module
class ChromecastModule {
    @Provides
    @PresenterScope
    internal fun providesChromecastPresenter(torrentRepository: ITorrentRepository): ChromecastContract.Presenter {
        return ChromecastPresenter(torrentRepository)
    }

}

