package com.shwifty.tex

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.facebook.stetho.Stetho
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.chromecast.CastHandler
import com.shwifty.tex.repository.preferences.PreferencesRepository
import com.shwifty.tex.views.splash.mvp.SplashActivity
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
        super.onCreate()
        PreferencesRepository().getWorkingDirectoryPreference(this)
                .subscribe({
                    Confluence.install(this, it.absolutePath)
                    Trickl.install(ClientPrefs(it, true, true), Confluence.torrentRepositoryComponent.getTorrentRepository())
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
        val mStartActivity = Intent(this, SplashActivity::class.java)
        val mPendingIntentId = 123456
        val mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT)
        (this.getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
        System.exit(0)
    }
}