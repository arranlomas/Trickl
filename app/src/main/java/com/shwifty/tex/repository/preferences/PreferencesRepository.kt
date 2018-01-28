package com.shwifty.tex.repository.preferences

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import com.shwifty.tex.models.AppTheme
import io.reactivex.Observable
import java.io.File


/**
 * Created by arran on 27/10/2017.
 */
internal class PreferencesRepository : IPreferenceRepository {

    private val KEY_PREFERENCES_FILE = "key_trickl_pref_file"

    private val KEY_WORKING_DIRECTORY = "pref_working_directory"
    private val KEY_WIFI_ONLY = "pref_wifi_only"
    private val KEY_THEME = "pref_theme"

    private fun getSharedPref(context: Context): SharedPreferences = context.getSharedPreferences(KEY_PREFERENCES_FILE, Context.MODE_PRIVATE)

    override fun saveWorkingDirectoryPreference(context: Context, file: File): Observable<Boolean> {
        val editor = getSharedPref(context).edit()
        editor.putString(KEY_WORKING_DIRECTORY, file.absolutePath)
        editor.apply()
        return Observable.just(true)
    }

    override fun getWorkingDirectoryPreference(context: Context): Observable<File> {
        val defaultWorkingDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + "Trickl"
        val wd = getSharedPref(context).getString(KEY_WORKING_DIRECTORY, defaultWorkingDirectory)
        return Observable.just(File(wd))
    }

    override fun saveWifiOnlyPreference(context: Context, selected: Boolean): Observable<Boolean> {
        val editor = getSharedPref(context).edit()
        editor.putBoolean(KEY_WIFI_ONLY, selected)
        editor.apply()
        return Observable.just(selected)
    }

    override fun getWifiOnlyPreference(context: Context): Observable<Boolean> {
        val wifiOnly = getSharedPref(context).getBoolean(KEY_WIFI_ONLY, true)
        return Observable.just(wifiOnly)
    }

    override fun saveThemePreference(context: Context, theme: AppTheme): Observable<AppTheme> {
        val editor = getSharedPref(context).edit()
        editor.putInt(KEY_THEME, theme.ordinal)
        editor.apply()
        return Observable.just(theme)
    }

    override fun getThemPreference(context: Context): Observable<AppTheme> {
        val theme = getSharedPref(context).getInt(KEY_THEME, AppTheme.DARK.ordinal)
        return Observable.just(AppTheme.values()[theme])
    }


}