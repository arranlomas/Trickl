package com.shwifty.tex.views.main.di


import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.MyApplication
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.views.chromecast.mvp.ChromecastControllerContract
import com.shwifty.tex.views.chromecast.mvp.ChromecastControllerPresenter
import com.shwifty.tex.views.main.mvp.MainActivity
import com.shwifty.tex.views.main.mvp.MainContract
import com.shwifty.tex.views.main.mvp.MainPresenter
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 15/02/2017.
 */

@Module
class MainModule {
    @Provides
    internal fun providesMainPresenter(torrentRepository: ITorrentRepository, navigation: INavigation): MainContract.Presenter {
        return MainPresenter(torrentRepository, MyApplication.castHandler, navigation)
    }

    @Provides
    internal fun providesChromecastPresenter(torrentRepository: ITorrentRepository): ChromecastControllerContract.Presenter {
        return ChromecastControllerPresenter(torrentRepository, MyApplication.castHandler)
    }
}

@Module
abstract class MainActivityBuilder {
    @ContributesAndroidInjector(modules = arrayOf(MainModule::class))
    internal abstract fun bindsMainActivity(): MainActivity
}


