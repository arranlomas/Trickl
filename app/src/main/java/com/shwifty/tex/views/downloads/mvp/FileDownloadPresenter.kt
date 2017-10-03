package com.shwifty.tex.views.downloads.mvp

import android.os.Bundle
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.base.BasePresenter
import com.shwifty.tex.views.downloads.list.FileDownloadAdapter
import rx.Subscriber

/**
 * Created by arran on 7/05/2017.
 */
class FileDownloadPresenter(val torrentRepository: ITorrentRepository) : BasePresenter<FileDownloadContract.View>(), FileDownloadContract.Presenter {

    override fun setup(arguments: Bundle?) {
        torrentRepository.torrentFileProgressSource
                .flatMap { torrentRepository.getDownloadingFilesFromPersistence() }
                .subscribe(object : Subscriber<List<TorrentFile>>() {
                    override fun onError(e: Throwable?) {
                        e?.localizedMessage?.let { mvpView.showError(e.localizedMessage) }
                        e?.printStackTrace()
                    }

                    override fun onCompleted() {}

                    override fun onNext(torrentFiles: List<TorrentFile>) {
                        mvpView.setupViewFromTorrentInfo(torrentFiles)
                    }
                })
                .addSubscription()

        torrentRepository.torrentFileDeleteListener
                .subscribe(object : BaseSubscriber<TorrentFile>() {
                    override fun onNext(pair: TorrentFile?) {
                        mvpView.setLoading(false)
                        refresh()
                    }
                })
                .addSubscription()
    }

    override fun refresh() {
        torrentRepository.getDownloadingFilesFromPersistence()
                .subscribe(object : BaseSubscriber<List<TorrentFile>>() {
                    override fun onNext(torrentFiles: List<TorrentFile>) {
                        mvpView.setupViewFromTorrentInfo(torrentFiles)
                    }
                })
                .addSubscription()
    }

    override fun viewClicked(torrentFile: TorrentFile, action: FileDownloadAdapter.Companion.ClickTypes) {
        mvpView.torrentFileClicked(action, torrentFile, torrentRepository)
    }
}