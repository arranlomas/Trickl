package com.shwifty.tex.views.splash.mvi

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.R
import com.shwifty.tex.views.base.mvi.BaseDaggerMviActivity
import com.shwifty.tex.views.main.mvp.MainActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class SplashActivity : BaseDaggerMviActivity<SplashActions, SplashResult, SplashViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var rxPermissions: RxPermissions

    private val requestPermissionSubject = PublishSubject.create<SplashActions.RequestPermissions>()
    private val startConfluenceSubject = PublishSubject.create<SplashActions.StartConfluence>()

    companion object {
        const val TAG_MAGNET_FROM_INTENT = "arg_magnet_from_intent"
        const val TAG_TORRENT_FILE_FROM_INTENT = "arg_torrent_file_from_intent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        rxPermissions = RxPermissions(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)

        super.setup(viewModel, { error ->
            Toasty.error(this, error.localizedMessage).show()
        })
        super.attachActions(actions(), SplashActions.HandleIntent::class.java)
    }

    private fun actions() = Observable.merge(observables())

    private fun observables() = listOf(
            handleIntentAction(),
            requestPermissionSubject,
            startConfluenceSubject
    )

    private fun handleIntentAction(): Observable<SplashActions> = Observable.just(SplashActions.HandleIntent(intent, this))

    override fun onStart() {
        super.onStart()
        requestPermissionSubject.onNext(SplashActions.RequestPermissions(rxPermissions))
    }

    private fun progressToMain() {
        finish()
        intent = Intent(this, MainActivity::class.java)
        if (viewModel.getLastState().magnet != null) intent.putExtra(TAG_MAGNET_FROM_INTENT, viewModel.getLastState().magnet)
        if (viewModel.getLastState().torrentFile != null) intent.putExtra(TAG_TORRENT_FILE_FROM_INTENT, viewModel.getLastState().torrentFile)
        startActivity(intent)
    }


    override fun render(state: SplashViewState) {
        if (state.permissionGranted == true && state.waitingForConfluenceToStart == null) {
            startConfluenceSubject.onNext(SplashActions.StartConfluence(this))
        }

        if (state.confluenceStarted == true && state.waitingForConfluenceToStart == false) {
            progressToMain()
        }
    }
}
