package com.shwifty.tex.dagger.main


import com.shwifty.tex.dagger.network.NetworkModule
import com.shwifty.tex.dialogs.*
import com.shwifty.tex.repositories.ITorrentRepository
import com.shwifty.tex.views.addtorrent.AddTorrentPresenter
import com.shwifty.tex.views.all.mvp.AllFragment
import com.shwifty.tex.views.all.mvp.AllPresenter
import com.shwifty.tex.views.downloads.mvp.FileDownloadPresenter
import com.shwifty.tex.views.main.DialogManager
import com.shwifty.tex.views.main.mvp.MainActivity
import com.shwifty.tex.views.main.mvp.MainContract
import com.shwifty.tex.views.main.mvp.MainPresenter
import com.shwifty.tex.views.showtorrent.TorrentInfoPresenter
import com.shwifty.tex.views.splash.mvp.SplashPresenter
import com.shwifty.tex.views.torrentdetails.mvp.TorrentDetailsPresenter
import com.shwifty.tex.views.torrentfiles.mvp.TorrentFilesPresenter
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
    fun getTorrentRepository(): ITorrentRepository
    fun getMainPresenter(): MainContract.Presenter
}


