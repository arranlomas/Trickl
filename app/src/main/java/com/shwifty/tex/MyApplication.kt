package com.shwifty.tex

import android.app.Application
import com.facebook.stetho.Stetho
import com.shwifty.tex.chromecast.CastHandler
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm

/**
 * Created by arran on 29/04/2017.
 */
class MyApplication : Application() {

    companion object{
        var castHandler: CastHandler = CastHandler()
    }

    override fun onCreate() {
        TricklComponent.install()
        Realm.init(this)
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build())


    }


}