package com.shwifty.tex

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.multidex.MultiDex
import android.util.Log
import com.arranlomas.daggerviewmodelhelper.AppInjector
import com.facebook.stetho.Stetho
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.chromecast.CastHandler
import com.shwifty.tex.repository.preferences.PreferencesRepository
import com.shwifty.tex.views.splash.mvp.SplashActivity
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import es.dmoral.toasty.Toasty


/**x
 * Created by arran on 29/04/2017.
 */
class MyApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        AppInjector.init(this)
        val appComponent = DaggerAppComponent.create()
        appComponent.inject(this)
        return appComponent
    }

    companion object {
        val castHandler = CastHandler()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        PreferencesRepository().getWorkingDirectoryPreference(this)
                .subscribe({
                    Confluence.install(this, it.absolutePath)
                    val torrentRepo = Confluence.torrentRepository
                    Trickl.install(ClientPrefs(it, true, true))
                }, {
                    Toasty.error(this, getString(R.string.error_loading_working_directory_prefs)).show()
                })
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build())

        val arch = System.getProperty("os.arch")
        Log.v("architecture", arch)
    }

    //TODO this is SUPER HACKYYY!!!
    fun restart() {
        Confluence.stop()
        val splasshActivity = Intent(this, SplashActivity::class.java)
        val pendingIntentId = 123456
        val pendingIntent = PendingIntent.getActivity(this, pendingIntentId, splasshActivity, PendingIntent.FLAG_CANCEL_CURRENT)
        (this.getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)
        System.exit(0)
    }
}