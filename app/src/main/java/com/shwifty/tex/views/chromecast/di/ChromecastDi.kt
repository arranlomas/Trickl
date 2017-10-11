package com.shwifty.tex.views.chromecast.di

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.MyApplication
import com.shwifty.tex.Trickl
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.chromecast.mvp.ChromecastBottomSheet
import com.shwifty.tex.views.chromecast.mvp.ChromecastControllerContract
import com.shwifty.tex.views.chromecast.mvp.ChromecastControllerPresenter
import dagger.Component
import dagger.Module
import dagger.Provides

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
    internal fun providesChromecastPresenter(torrentRepository: ITorrentRepository): ChromecastControllerContract.Presenter {
        return ChromecastControllerPresenter(torrentRepository, MyApplication.castHandler)
    }

}

