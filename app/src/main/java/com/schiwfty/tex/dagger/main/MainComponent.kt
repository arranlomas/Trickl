package com.schiwfty.tex.dagger.main


import com.schiwfty.tex.dagger.network.NetworkModule
import com.schiwfty.tex.dialogs.AddHashDialog
import com.schiwfty.tex.dialogs.AddMagnetDialog
import com.schiwfty.tex.dialogs.DeleteFileDialog
import com.schiwfty.tex.dialogs.DeleteTorrentDialog
import com.schiwfty.tex.views.addtorrent.AddTorrentPresenter
import com.schiwfty.tex.views.all.mvp.AllFragment
import com.schiwfty.tex.views.all.mvp.AllPresenter
import com.schiwfty.tex.views.downloads.mvp.FileDownloadPresenter
import com.schiwfty.tex.views.main.mvp.MainActivity
import com.schiwfty.tex.views.main.mvp.MainPresenter
import com.schiwfty.tex.views.showtorrent.TorrentInfoPresenter
import com.schiwfty.tex.views.splash.mvp.SplashPresenter
import com.schiwfty.tex.views.torrentdetails.mvp.TorrentDetailsPresenter
import com.schiwfty.tex.views.torrentfiles.mvp.TorrentFilesPresenter
import dagger.Component
import javax.inject.Singleton

/**
 * Created by arran on 15/02/2017.
 */
@Singleton
@Component(modules = arrayOf(MainModule::class, NetworkModule::class))
interface MainComponent {
    fun inject(allFragment: AllFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(splashPresenter: SplashPresenter)
    fun inject(addTorrentPresenter: AddTorrentPresenter)
    fun inject(torrentInfoPresenter: TorrentInfoPresenter)
    fun inject(mainPresenter: MainPresenter)
    fun inject(torrentDetailsPresenter: TorrentDetailsPresenter)
    fun inject(torrentFilesPresenter: TorrentFilesPresenter)
    fun inject(addMagnetDialog: AddMagnetDialog)
    fun inject(deleteFileDialog: DeleteFileDialog)
    fun inject(allPresenter: AllPresenter)
    fun inject(fileDownloadPresenter: FileDownloadPresenter)
    fun inject(deleteTorrentDialog: DeleteTorrentDialog)
    fun inject(addHashDialog: AddHashDialog)
}


