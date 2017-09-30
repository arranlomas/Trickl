package com.shwifty.tex.views.all.mvp

import android.content.Context
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 16/04/2017.
 */
class AllPresenter : BasePresenter<AllContract.View>(), AllContract.Presenter {

    lateinit var torrentRepository: ITorrentRepository

    lateinit var context: Context

    override fun attachView(mvpView: AllContract.View) {
        super.attachView(mvpView)
        torrentRepository = Confluence.torrentRepository

        torrentRepository.torrentInfoDeleteListener
                .subscribe({ refresh() }, { it.printStackTrace() })
                .addSubscription()
    }

    override fun refresh() {
        torrentRepository.getAllTorrentsFromStorage()
                .subscribe({
                    mvpView.showAllTorrents(it)
                }, {
                    it.printStackTrace()
                })
    }
}