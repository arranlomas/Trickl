package com.shwifty.tex

import android.os.Environment
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.facebook.stetho.Stetho
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.chromecast.CastHandler
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import java.io.File

/**
 * Created by arran on 29/04/2017.
 */
class MyApplication : MultiDexApplication() {
    val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + "Trickl"

    companion object {
        var castHandler: CastHandler = CastHandler()
    }

    override fun onCreate() {
        //be aware of the order of initialisation
        Confluence.install(this, directoryPath)
        TricklComponent.install(Confluence.torrentRepositoryComponent)
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build())

        val arch = System.getProperty("os.arch")
        Log.v("architecture", arch)
    }


}