package com.schiwfty.tex.dagger.network


import com.schiwfty.tex.dagger.context.ContextModule
import com.schiwfty.tex.views.addtorrent.AddTorrentPresenter
import com.schiwfty.tex.views.all.mvp.AllPresenter
import com.schiwfty.tex.views.downloads.mvp.FileDownloadPresenter
import com.schiwfty.tex.views.main.mvp.MainPresenter
import com.schiwfty.tex.views.showtorrent.ShowTorrentPresenter
import com.schiwfty.tex.views.splash.mvp.SplashPresenter
import com.schiwfty.tex.views.torrentdetails.mvp.TorrentDetailsPresenter
import com.schiwfty.tex.views.torrentfiles.mvp.TorrentFilesPresenter
import dagger.Component

/**
 * Created by arran on 15/02/2017.
 */

@NetworkScope
@Component(modules = arrayOf(NetworkModule::class, ContextModule::class))
interface NetworkComponent {
    fun inject(splashPresenter: SplashPresenter)
    fun inject(addTorrentPresenter: AddTorrentPresenter)
    fun inject(showTorrentPresenter: ShowTorrentPresenter)
    fun inject(mainPresenter: MainPresenter)
    fun inject(torrentDetailsPresenter: TorrentDetailsPresenter)
    fun inject(torrentFilesPresenter: TorrentFilesPresenter)
    fun inject(allPresenter: AllPresenter)
    fun inject(fileDownloadPresenter: FileDownloadPresenter)
}




