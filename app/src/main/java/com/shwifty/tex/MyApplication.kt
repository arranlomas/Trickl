package com.shwifty.tex

import android.support.multidex.MultiDexApplication
import android.util.Log
import com.facebook.stetho.Stetho
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.chromecast.CastHandler
import com.shwifty.tex.repository.preferences.PreferencesRepository
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import es.dmoral.toasty.Toasty

/**
 * Created by arran on 29/04/2017.
 */
class MyApplication : MultiDexApplication() {

    companion object {
        val castHandler = CastHandler()
    }

    override fun onCreate() {
        PreferencesRepository().getWorkingDirectoryPreference(this)
                .subscribe({
                    Confluence.install(this, it.absolutePath)
                    Trickl.install(Confluence.torrentRepositoryComponent.getTorrentRepository())
                }, {
                    Toasty.error(this, getString(R.string.error_loading_working_directory_prefs))
                })
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build())

        val arch = System.getProperty("os.arch")
        Log.v("architecture", arch)
    }
}