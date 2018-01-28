package com.shwifty.tex.repository.preferences

import android.content.Context
import com.shwifty.tex.models.AppTheme
import io.reactivex.Observable
import java.io.File

/**
 * Created by arran on 27/10/2017.
 */
interface IPreferenceRepository {
    fun getWorkingDirectoryPreference(context: Context): Observable<File>
    fun saveWorkingDirectoryPreference(context: Context, file: File): Observable<Boolean>
    fun saveWifiOnlyPreference(context: Context, selected: Boolean): Observable<Boolean>
    fun getWifiOnlyPreference(context: Context): Observable<Boolean>
    fun saveThemePreference(context: Context, theme: AppTheme): Observable<AppTheme>
    fun getThemPreference(context: Context): Observable<AppTheme>
}