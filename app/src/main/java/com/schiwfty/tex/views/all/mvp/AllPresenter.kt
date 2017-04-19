package com.schiwfty.tex.views.main.mvp

import android.content.Context
import android.content.Intent
import com.schiwfty.tex.R
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.confluence.ConfluenceDaemonService
import com.schiwfty.tex.tools.composeIoWithRetry
import com.schiwfty.tex.tools.dagger.context.ContextModule
import com.schiwfty.tex.tools.dagger.network.DaggerNetworkComponent
import com.schiwfty.tex.tools.dagger.network.NetworkModule
import com.schiwfty.tex.tools.retrofit.HttpController
import com.schiwfty.tex.views.splash.mvp.SplashContract
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by arran on 16/04/2017.
 */
class AllPresenter : AllContract.Presenter {
    lateinit var view: AllContract.View

    override fun setup(context: Context, view: AllContract.View) {
        this.view = view
    }


}