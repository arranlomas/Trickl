package com.shwifty.tex.views.downloads.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.openFile
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.downloads.list.FileDownloadAdapter
import com.shwifty.tex.views.main.mvp.MainContract
import rx.subscriptions.CompositeSubscription

/**
 * Created by arran on 7/05/2017.
 */
class FileDownloadPresenter : FileDownloadContract.Presenter {


    lateinit var view: FileDownloadContract.View
    lateinit var context: Context

    lateinit var torrentRepository: ITorrentRepository

    lateinit var mainPresenter: MainContract.Presenter

    private val compositeSubscription = CompositeSubscription()

    override fun setup(context: Context, view: FileDownloadContract.View, arguments: Bundle?) {
        mainPresenter = TricklComponent.mainComponent.getMainPresenter()
        torrentRepository = Confluence.torrentRepository
        this.view = view
        this.context = context

        compositeSubscription.add(
                torrentRepository.torrentFileProgressSource
                        .subscribe({
                            refresh()
                        }, {
                            it.printStackTrace()
                        })
        )

        compositeSubscription.add(
                torrentRepository.torrentFileDeleteListener
                        .subscribe({
                            refresh()
                        }, {
                            it.printStackTrace()
                        })
        )
    }

    override fun refresh() {
        torrentRepository.getDownloadingFilesFromPersistence()
                .subscribe { view.setupViewFromTorrentInfo(it) }
    }

    override fun destroy() {
        compositeSubscription.unsubscribe()
    }

    override fun viewClicked(torrentFile: TorrentFile, action: FileDownloadAdapter.Companion.ClickTypes) {
        when (action) {
            FileDownloadAdapter.Companion.ClickTypes.DOWNLOAD -> {
                mainPresenter.checkStatusForDownload(torrentFile)
            }
            FileDownloadAdapter.Companion.ClickTypes.OPEN -> {
                torrentFile.openFile(context, torrentRepository,{
                    view.showError(R.string.error_no_activity)
                })
            }
            FileDownloadAdapter.Companion.ClickTypes.DELETE -> {
                view.showDeleteFileDialog(torrentFile.torrentHash, torrentFile)
            }
            FileDownloadAdapter.Companion.ClickTypes.CHROMECAST -> {
                mainPresenter.startChromecast(torrentFile)
            }

        }
    }
}