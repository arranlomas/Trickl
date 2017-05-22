package com.schiwfty.tex.views.downloads.mvp

import android.content.Context
import android.os.Bundle
import com.pawegio.kandroid.v
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.utils.getFullPath
import com.schiwfty.tex.views.downloads.list.FileDownloadAdapter
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class FileDownloadPresenter : FileDownloadContract.Presenter {

    lateinit var view: FileDownloadContract.View
    lateinit var context: Context

    @Inject
    lateinit var torrentRepository: ITorrentRepository
    private val compositeSubscription = CompositeSubscription()

    override fun setup(context: Context, view: FileDownloadContract.View, arguments: Bundle?) {
        TricklComponent.networkComponent.inject(this)
        this.view = view
        this.context = context

        compositeSubscription.add(
                torrentRepository.torrentFileProgressSource
                        .subscribe({
                            it.forEach {
                                v { "HASH: ${it.torrentHash}" }
                                v { "PATH: ${it.getFullPath()}" }
                                v { "PERC: ${it.percComplete}" }
                            }
                            //TODO update view with new percentages
                        }, {
                            //swallow error
                            it.printStackTrace()
                        })
        )
    }

    override fun destroy() {
        compositeSubscription.unsubscribe()
    }

    override fun viewClicked(torrentFile: TorrentFile, action: FileDownloadAdapter.Companion.ClickTypes) {

    }

    override fun getDownloadingTorrents() {
        torrentRepository.getDownloadingFilesFromPersistence()
                .subscribe { view.setupViewFromTorrentInfo(it) }
    }
}