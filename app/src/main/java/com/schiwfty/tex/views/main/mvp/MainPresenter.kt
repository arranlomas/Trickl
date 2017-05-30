package com.schiwfty.tex.views.main.mvp

import android.content.Context
import android.content.Intent
import android.util.Log
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.views.splash.mvp.SplashActivity
import javax.inject.Inject

/**
 * Created by arran on 16/04/2017.
 */
class MainPresenter : MainContract.Presenter {

    lateinit var view: MainContract.View

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    override fun setup(context: Context, view: MainContract.View) {
        this.view = view
        TricklComponent.mainComponent.inject(this)
    }

    override fun showAddTorrentActivity(hash: String?, magnet: String?, torrentFilePath: String?) {
        view.showAddTorrentActivity(hash, magnet, torrentFilePath)
    }

    override fun showTorrentInfoActivity(infoHash: String) {
        view.showTorrentInfoActivity(infoHash)
    }

    override fun handleIntent(intent: Intent) {
        if(intent.hasExtra(SplashActivity.TAG_MAGNET_FROM_INTENT)){
            view.showAddTorrentActivity(magnet = intent.getStringExtra(SplashActivity.TAG_MAGNET_FROM_INTENT))
        }
    }
}