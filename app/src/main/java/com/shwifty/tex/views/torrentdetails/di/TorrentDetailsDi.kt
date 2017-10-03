package com.shwifty.tex.views.torrentdetails.di


import com.schiwfty.torrentwrapper.dagger.network.TorrentRepositoryComponent
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.all.mvp.AllFragment
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.torrentdetails.mvp.TorrentDetailsContract
import com.shwifty.tex.views.torrentdetails.mvp.TorrentDetailsFragment
import com.shwifty.tex.views.torrentdetails.mvp.TorrentDetailsPresenter
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */
@PresenterScope
@Component(modules = arrayOf(TorrentDetailsModule::class), dependencies = arrayOf(TorrentRepositoryComponent::class))
interface TorrentDetailsComponent {
    fun inject(torrentDetailsFragment: TorrentDetailsFragment)
}

@Module
class TorrentDetailsModule {
    @Provides
    @PresenterScope
    internal fun providesTorrentDetailsPresenter(torrentRepository: ITorrentRepository): TorrentDetailsContract.Presenter {
        return TorrentDetailsPresenter(torrentRepository)
    }

}

