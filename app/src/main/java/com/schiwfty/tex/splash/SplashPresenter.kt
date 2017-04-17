package com.schiwfty.tex.splash

import android.content.Context
import android.content.Intent
import com.schiwfty.tex.R
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.confluence.ConfluenceDaemonService
import com.schiwfty.tex.dagger.context.ContextModule
import com.schiwfty.tex.dagger.network.DaggerNetworkComponent
import com.schiwfty.tex.dagger.network.NetworkModule
import com.schiwfty.tex.dagger.utilities.composeIoWithRetry
import com.schiwfty.tex.retrofit.HttpController
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by arran on 16/04/2017.
 */
class SplashPresenter : SplashContract.Presenter {
    lateinit var view: SplashContract.View

    @Inject
    lateinit var httpController: HttpController

    override fun setup(context: Context, view: SplashContract.View) {
        this.view = view

        Confluence.setClientAddress()

        val networkComponent = DaggerNetworkComponent.builder()
                .networkModule(NetworkModule())
                .contextModule(ContextModule(context))
                .build()
        networkComponent.injectSplashPresenter(this)

    }

    override fun startConfluenceDaemon(context: Context) {
        val daemonIntent = Intent(context, ConfluenceDaemonService::class.java)
        daemonIntent.addCategory(ConfluenceDaemonService.TAG)
        context.startService(daemonIntent)
        listenForDaemon()
    }


    fun listenForDaemon() {
        httpController.status.subscribeOn(Schedulers.io())
                .composeIoWithRetry()
                .subscribe({
                    view.showSuccess(R.string.splash_start_confluence_success)
                    view.progressToMain()
                }, {
                    view.showError(R.string.splash_start_confluence_error)
                })

    }

}