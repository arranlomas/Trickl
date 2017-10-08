package com.shwifty.tex.views.all.di


import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.all.mvp.AllContract
import com.shwifty.tex.views.all.mvp.AllFragment
import com.shwifty.tex.views.all.mvp.AllPresenter
import com.shwifty.tex.views.base.PresenterScope
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */
@PresenterScope
@Component(modules = arrayOf(AllTorrentsModule::class), dependencies = arrayOf(TricklComponent::class))
interface AllTorrentsComponent {
    fun inject(allFragment: AllFragment)
}

@Module
class AllTorrentsModule {
    @Provides
    @PresenterScope
    internal fun providesAllTorrentsPresenter(torrentRepository: ITorrentRepository): AllContract.Presenter {
        return AllPresenter(torrentRepository)
    }

}

