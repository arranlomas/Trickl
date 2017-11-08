package com.shwifty.tex.views.browse.di

import com.shwifty.tex.repository.network.di.RepositoryComponent
import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.browse.mvp.TorrentBrowseContract
import com.shwifty.tex.views.browse.mvp.TorrentBrowsePresenter
import com.shwifty.tex.views.browse.mvp.TorrentBrowseFragment
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 28/10/2017.
 */
@PresenterScope
@Component(modules = arrayOf(TorrentBrowseModule::class), dependencies = arrayOf(RepositoryComponent::class))
interface TorrentBrowseComponent {
    fun inject(torrentBrowseFragment: TorrentBrowseFragment)
}

@Module
class TorrentBrowseModule {

    @Provides
    @PresenterScope
    internal fun providesTorrentSearchPresenter(torrentSearchRepository: ITorrentSearchRepository): TorrentBrowseContract.Presenter {
        return TorrentBrowsePresenter(torrentSearchRepository)
    }
}
