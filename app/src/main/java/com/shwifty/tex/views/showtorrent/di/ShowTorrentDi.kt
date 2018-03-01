package com.shwifty.tex.views.showtorrent.di


import com.schiwfty.torrentwrapper.dagger.network.TorrentRepositoryComponent
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.showtorrent.mvp.TorrentInfoActivity
import com.shwifty.tex.views.showtorrent.mvp.TorrentInfoContract
import com.shwifty.tex.views.showtorrent.mvp.TorrentInfoPresenter
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */
@PresenterScope
@Component(modules = arrayOf(TorrentInfoModule::class), dependencies = arrayOf(TorrentRepositoryComponent::class))
interface TorrentInfoComponent {
    fun inject(torrentInfoActivity: TorrentInfoActivity)
}

@Module
class TorrentInfoModule {
    @Provides
    internal fun providesTorrentInfoPresenter(torrentRepository: ITorrentRepository): TorrentInfoContract.Presenter {
        return TorrentInfoPresenter(torrentRepository)
    }

}

