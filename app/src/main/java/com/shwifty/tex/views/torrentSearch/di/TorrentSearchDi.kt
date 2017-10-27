package com.shwifty.tex.views.torrentSearch.di


import com.shwifty.tex.network.di.NetworkComponent
import com.shwifty.tex.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.torrentSearch.TorrentSearchContract
import com.shwifty.tex.views.torrentSearch.TorrentSearchFragment
import com.shwifty.tex.views.torrentSearch.TorrentSearchPresenter
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */
@PresenterScope
@Component(modules = arrayOf(TorrentSearchModule::class), dependencies = arrayOf(NetworkComponent::class))
interface TorrentSearchComponent {
    fun inject(torrentSearchFragment: TorrentSearchFragment)
}

@Module
class TorrentSearchModule {

    @Provides
    @PresenterScope
    internal fun providesTorrentSearchPresenter(torrentSearchRepository: ITorrentSearchRepository): TorrentSearchContract.Presenter {
        return TorrentSearchPresenter(torrentSearchRepository)
    }

}

