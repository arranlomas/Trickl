package com.shwifty.tex.views.all.mvp

import android.content.Context
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.ParseTorrentResult
import com.shwifty.tex.utils.logTorrentParseError
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 16/04/2017.
 */
class AllPresenter(val torrentRepository: ITorrentRepository) : BasePresenter<AllContract.View>(), AllContract.Presenter {

    lateinit var context: Context

    override fun attachView(mvpView: AllContract.View) {
        super.attachView(mvpView)
        torrentRepository.torrentInfoDeleteListener
                .subscribe(object : BaseSubscriber<TorrentInfo>() {
                    override fun onNext(result: TorrentInfo?) {
                        mvpView.setLoading(false)
                        refresh()
                    }
                })
                .addSubscription()
    }

    override fun refresh() {
        torrentRepository.getAllTorrentsFromStorage()
                .subscribe(object : BaseSubscriber<List<ParseTorrentResult>>() {
                    override fun onNext(results: List<ParseTorrentResult>) {
                        val success = results.filter { it is ParseTorrentResult.Success }
                        val error = results.filter { it is ParseTorrentResult.Error }
                        if (error.isNotEmpty()) {
                            mvpView.showSomeTorrentsCouldNotBeLoaded(error.size)
                            error.forEach { it.logTorrentParseError() }
                        }
                        mvpView.setLoading(false)
                        val torrentInfos = mutableListOf<TorrentInfo>()
                        success.forEach { result ->
                            result.unwrapIfSuccess { torrentInfos.add(it) } ?: let { result.logTorrentParseError() }
                        }
                        mvpView.showAllTorrents(torrentInfos.toList())
                    }
                })
    }
}