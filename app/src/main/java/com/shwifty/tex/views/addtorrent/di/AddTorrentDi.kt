package com.shwifty.tex.views.addtorrent.di


import com.schiwfty.torrentwrapper.dagger.network.TorrentRepositoryComponent
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity
import com.shwifty.tex.views.addtorrent.mvp.AddTorrentContract
import com.shwifty.tex.views.addtorrent.mvp.AddTorrentPresenter
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.main.di.MainModule
import com.shwifty.tex.views.main.mvp.MainActivity
import com.shwifty.tex.views.main.mvp.MainContract
import com.shwifty.tex.views.main.mvp.MainPresenter
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */
@PresenterScope
@Component(modules = arrayOf(AddTorrentModule::class), dependencies = arrayOf(TorrentRepositoryComponent::class))
interface AddTorrentComponent {
    fun inject(addTorrentActivity: AddTorrentActivity)
}

@Module
class AddTorrentModule {
    @Provides
    @PresenterScope
    internal fun providesAddTorrentPresenter(torrentRepository: ITorrentRepository): AddTorrentContract.Presenter {
        return AddTorrentPresenter(torrentRepository)
    }

}

