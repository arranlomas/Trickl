package com.shwifty.tex.views.all.mvp

import android.content.Context
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentInfo
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
                .subscribe(object : BaseSubscriber<TorrentInfo>() {
                    override fun onNext(pair: TorrentInfo?) {
                        mvpView.setLoading(false)
                        refresh()
                    }
                })
                .addSubscription()
    }

    override fun refresh() {
        torrentRepository.getAllTorrentsFromStorage()
                .subscribe(object : BaseSubscriber<List<TorrentInfo>>(){
                    override fun onNext(torrentInfos: List<TorrentInfo>) {
                        mvpView.setLoading(false)
                        mvpView.showAllTorrents(torrentInfos)
                    }
                })
    }
}