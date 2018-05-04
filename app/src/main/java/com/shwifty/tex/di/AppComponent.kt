package com.shwifty.tex.di

import com.shwifty.tex.MyApplication
import com.shwifty.tex.actions.ActionManagerModule
import com.shwifty.tex.chromecast.CastHandlerModule
import com.shwifty.tex.dialogs.DialogManagerModule
import com.shwifty.tex.navigation.NavigationModule
import com.shwifty.tex.repository.network.di.ApiModule
import com.shwifty.tex.repository.network.di.RepositoryModule
import com.shwifty.tex.views.addtorrent.di.AddTorrentActivityBuilder
import com.shwifty.tex.views.all.di.AllTorrentsFragmentBuilder
import com.shwifty.tex.views.browse.di.TorrentBrowseFragmentBuilder
import com.shwifty.tex.views.downloads.di.FileFownloadFragmentBuilder
import com.shwifty.tex.views.main.di.MainActivityBuilder
import com.shwifty.tex.views.search.di.TorrentSearchFragmentBuilder
import com.shwifty.tex.views.settings.di.SettingsActivityBuilder
import com.shwifty.tex.views.showtorrent.di.TorrentInfoActivityBuilder
import com.shwifty.tex.views.torrentdetails.TorrentDetailsFragmentBuilder
import com.shwifty.tex.views.torrentfiles.TorrentFileFragmentBuilder
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by arran on 8/10/2017.
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApiModule::class,
    RepositoryModule::class,
    NavigationModule::class,
    DialogManagerModule::class,
    CastHandlerModule::class,
    ActionManagerModule::class,

    //Views
    AddTorrentActivityBuilder::class,
    MainActivityBuilder::class,
    FileFownloadFragmentBuilder::class,
    AllTorrentsFragmentBuilder::class,
    TorrentInfoActivityBuilder::class,
    TorrentFileFragmentBuilder::class,
    TorrentDetailsFragmentBuilder::class,
    SettingsActivityBuilder::class,
    TorrentBrowseFragmentBuilder::class,
    TorrentSearchFragmentBuilder::class])
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: MyApplication)
}