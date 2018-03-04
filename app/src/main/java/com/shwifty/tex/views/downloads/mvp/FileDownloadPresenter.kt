package com.shwifty.tex.views.downloads.mvp

import android.content.Context
import android.os.Bundle
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.actions.IActionManager
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.utils.composeIo
import com.shwifty.tex.views.base.mvp.BasePresenter
import com.shwifty.tex.views.downloads.list.FileDownloadAdapter

/**
 * Created by arran on 7/05/2017.
 */
class FileDownloadPresenter(val torrentRepository: ITorrentRepository,
                            val actionManager: IActionManager,
                            val dialogManager: IDialogManager,
                            val navigation: INavigation,
                            val castHandler: ICastHandler) : BasePresenter<FileDownloadContract.View>(), FileDownloadContract.Presenter {

    override fun setup(arguments: Bundle?) {
        torrentRepository.torrentFileProgressSource
                .flatMap { torrentRepository.getDownloadingFilesFromPersistence() }
                .composeIo()
                .subscribe(object : BaseSubscriber<List<TorrentFile>>(false) {
                    override fun onNext(torrentFiles: List<TorrentFile>) {
                        mvpView.setupViewFromTorrentInfo(torrentFiles)
                    }
                })
                .addSubscription()

        torrentRepository.torrentFileDeleteListener
                .subscribe(object : BaseSubscriber<TorrentFile>() {
                    override fun onNext(result: TorrentFile?) {
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

    override fun viewClicked(context: Context, torrentFile: TorrentFile, action: FileDownloadAdapter.Companion.ClickTypes) {
        val onError = { error: String ->
            mvpView.showError(error)
        }
        when (action) {
            FileDownloadAdapter.Companion.ClickTypes.DOWNLOAD -> actionManager.startDownload(context, torrentFile, onError)
            FileDownloadAdapter.Companion.ClickTypes.OPEN -> actionManager.openTorrentFile(context, torrentFile, onError)
            FileDownloadAdapter.Companion.ClickTypes.DELETE -> actionManager.openDeleteTorrentDialog(context, torrentFile)
            FileDownloadAdapter.Companion.ClickTypes.CHROMECAST -> actionManager.startChromecast(context, torrentFile, onError)
        }
    }
}