package com.shwifty.tex.views.downloads.mvp

import android.content.Context
import android.os.Bundle
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.models.TorrentFile
import com.shwifty.tex.repositories.ITorrentRepository
import com.shwifty.tex.utils.getFullPath
import com.shwifty.tex.utils.openFile
import com.shwifty.tex.views.downloads.list.FileDownloadAdapter
import com.shwifty.tex.views.main.mvp.MainContract
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

    @Inject
    lateinit var mainPresenter: MainContract.Presenter

    private val compositeSubscription = CompositeSubscription()

    override fun setup(context: Context, view: FileDownloadContract.View, arguments: Bundle?) {
        TricklComponent.mainComponent.inject(this)
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
                torrentFile.openFile(context, torrentRepository)
            }
            FileDownloadAdapter.Companion.ClickTypes.DELETE -> {
                view.showDeleteFileDialog(torrentFile.torrentHash, torrentFile.parentTorrentName, torrentFile.getFullPath())
            }
            FileDownloadAdapter.Companion.ClickTypes.CHROMECAST -> {
                mainPresenter.startChromecast(torrentFile)
            }

        }
    }
}