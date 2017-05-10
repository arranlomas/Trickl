package com.schiwfty.tex.views.main.mvp

import android.content.Context
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.utils.composeIo
import com.schiwfty.tex.utils.getAsTorrent
import rx.Observable
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
        TricklComponent.networkComponent.inject(this)
    }


}