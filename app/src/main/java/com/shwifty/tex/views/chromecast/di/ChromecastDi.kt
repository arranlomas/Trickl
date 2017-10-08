package com.shwifty.tex.views.chromecast.di

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.chromecast.mvp.ChromecastBottomSheet
import com.shwifty.tex.views.chromecast.mvp.ChromecastContract
import com.shwifty.tex.views.chromecast.mvp.ChromecastPresenter
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

/**
 * Created by arran on 15/02/2017.
 */
@PresenterScope
@Component(modules = arrayOf(ChromecastModule::class), dependencies = arrayOf(TricklComponent::class))
interface ChromecastComponent {
    fun inject(chromecastBottomSheet: ChromecastBottomSheet)
}

@Module
class ChromecastModule {
    @Provides
    @PresenterScope
    internal fun providesChromecastPresenter(torrentRepository: ITorrentRepository, castHandler: ICastHandler): ChromecastContract.Presenter {
        return ChromecastPresenter(torrentRepository, castHandler)
    }

}

