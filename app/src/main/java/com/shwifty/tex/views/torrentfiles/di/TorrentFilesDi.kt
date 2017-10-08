package com.shwifty.tex.views.torrentfiles.di


import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.torrentfiles.mvp.TorrentFilesContract
import com.shwifty.tex.views.torrentfiles.mvp.TorrentFilesFragment
import com.shwifty.tex.views.torrentfiles.mvp.TorrentFilesPresenter
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */
@PresenterScope
@Component(modules = arrayOf(TorrentFilesModule::class), dependencies = arrayOf(TricklComponent::class))
interface TorrentFilesComponent {
    fun inject(torrentFilesFragment: TorrentFilesFragment)
}

@Module
class TorrentFilesModule {
    @Provides
    @PresenterScope
    internal fun providesTorrentFilesPresenter(torrentRepository: ITorrentRepository): TorrentFilesContract.Presenter {
        return TorrentFilesPresenter(torrentRepository)
    }

}

