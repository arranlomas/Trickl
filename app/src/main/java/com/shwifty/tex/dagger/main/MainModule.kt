package com.shwifty.tex.dagger.main


import com.shwifty.tex.views.main.mvp.MainContract
import com.shwifty.tex.views.main.mvp.MainPresenter
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
    internal fun providesMainPresenter(): MainContract.Presenter {
        return MainPresenter()
    }

}
