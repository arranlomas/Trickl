package com.schiwfty.tex.views.main.mvp

import android.content.Context
import android.util.Log
import com.schiwfty.tex.R
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.repositories.ITorrentRepository
import java.io.File
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

}