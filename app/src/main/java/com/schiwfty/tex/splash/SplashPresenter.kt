package com.schiwfty.tex.splash

import android.content.Context
import android.content.Intent
import com.schiwfty.tex.R
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.confluence.ConfluenceDaemonService
import com.schiwfty.tex.dagger.RetryAfterTimeoutWithDelay
import com.schiwfty.tex.dagger.context.ContextModule
import com.schiwfty.tex.dagger.network.DaggerNetworkComponent
import com.schiwfty.tex.dagger.network.NetworkModule
import com.schiwfty.tex.dagger.utilities.composeForIoTasks
import com.schiwfty.tex.retrofit.HttpController
import okhttp3.ResponseBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
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
                .composeForIoTasks()
                .subscribe(object : Subscriber<ResponseBody>() {
                    override fun onCompleted() {
                        view.showSuccess(R.string.splash_start_confluence_success)
                        view.progressToMain()
                    }

                    override fun onError(e: Throwable) {
                        view.showError(R.string.splash_start_confluence_error)
                    }

                    override fun onNext(response: ResponseBody) {
                        view.showSuccess(R.string.splash_start_confluence_success)
                        view.progressToMain()
                    }
                })
    }

}