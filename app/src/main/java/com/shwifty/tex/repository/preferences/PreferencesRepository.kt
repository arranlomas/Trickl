package com.shwifty.tex.repository.preferences

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import rx.Observable
import java.io.File


/**
 * Created by arran on 27/10/2017.
 */
internal class PreferencesRepository : IPreferenceRepository {
    private val KEY_PREFERENCES_FILE = "key_trickl_pref_file"

    private val KEY_WORKING_DIRECTORY = "pref_working_directory"

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

}