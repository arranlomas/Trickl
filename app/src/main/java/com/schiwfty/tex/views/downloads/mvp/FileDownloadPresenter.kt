package com.schiwfty.tex.views.downloads.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.utils.openFile
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
        TricklComponent.mainComponent.inject(this)
        this.view = view
        this.context = context

        compositeSubscription.add(
                torrentRepository.torrentFileProgressSource
                        .subscribe({
                            getDownloadingTorrents()
                        }, {
                            it.printStackTrace()
                        })
        )
    }

    override fun getDownloadingTorrents() {
        torrentRepository.getDownloadingFilesFromPersistence()
                .subscribe { view.setupViewFromTorrentInfo(it) }
    }

    override fun destroy() {
        compositeSubscription.unsubscribe()
    }

    override fun viewClicked(torrentFile: TorrentFile, action: FileDownloadAdapter.Companion.ClickTypes) {
        when(action){
            FileDownloadAdapter.Companion.ClickTypes.DOWNLOAD ->{torrentRepository.startFileDownloading(torrentFile, context)}
            FileDownloadAdapter.Companion.ClickTypes.OPEN ->{ torrentFile.openFile(context, torrentRepository) }
            FileDownloadAdapter.Companion.ClickTypes.DELETE ->{
                torrentRepository.deleteTorrentFileData(torrentFile.parentTorrentName, torrentFile)
                getDownloadingTorrents()
            }
        }

    }
}