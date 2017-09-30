package com.shwifty.tex.views.downloads.mvp

import android.os.Bundle
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.base.BasePresenter
import com.shwifty.tex.views.downloads.list.FileDownloadAdapter
import com.shwifty.tex.views.main.mvp.MainContract

/**
 * Created by arran on 7/05/2017.
 */
class FileDownloadPresenter : BasePresenter<FileDownloadContract.View>(), FileDownloadContract.Presenter {

    lateinit var torrentRepository: ITorrentRepository

    lateinit var mainPresenter: MainContract.Presenter

    override fun setup(arguments: Bundle?) {
        mainPresenter = TricklComponent.mainComponent.getMainPresenter()
        torrentRepository = Confluence.torrentRepository

        torrentRepository.torrentFileProgressSource
                .subscribe(object : BaseSubscriber<List<TorrentFile>>() {
                    override fun onNext(t: List<TorrentFile>?) {
                        mvpView.setLoading(false)
                        refresh()
                    }
                })
                .addSubscription()



        torrentRepository.torrentFileDeleteListener
                .subscribe(object : BaseSubscriber<TorrentFile>() {
                    override fun onNext(t: TorrentFile?) {
                        mvpView.setLoading(false)
                        refresh()
                    }
                })
                .addSubscription()
    }

    override fun refresh() {
        torrentRepository.getDownloadingFilesFromPersistence()
                .subscribe({ mvpView.setupViewFromTorrentInfo(it) }, {
                    it.printStackTrace()
                })
    }

    override fun viewClicked(torrentFile: TorrentFile, action: FileDownloadAdapter.Companion.ClickTypes) {
        when (action) {
            FileDownloadAdapter.Companion.ClickTypes.DOWNLOAD -> {
                mainPresenter.checkStatusForDownload(torrentFile)
            }
            FileDownloadAdapter.Companion.ClickTypes.OPEN -> {
                mvpView.openTorrentFile(torrentFile, torrentRepository)
            }
            FileDownloadAdapter.Companion.ClickTypes.DELETE -> {
                mvpView.showDeleteFileDialog(torrentFile.torrentHash, torrentFile)
            }
            FileDownloadAdapter.Companion.ClickTypes.CHROMECAST -> {
                mainPresenter.startChromecast(torrentFile)
            }

        }
    }
}