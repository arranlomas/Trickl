package com.schiwfty.tex.dagger.main


import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by arran on 15/02/2017.
 */

@Module
class MainModule {

    @Provides
    @Singleton
    internal fun providesMainPresenter(): com.schiwfty.tex.views.main.mvp.MainContract.Presenter {
        return com.schiwfty.tex.views.main.mvp.MainPresenter()
    }

}
