package com.shwifty.tex.repository.preferences

import android.content.Context
import rx.Observable
import java.io.File

/**
 * Created by arran on 27/10/2017.
 */
interface IPreferenceRepository {
    fun getWorkingDirectoryPreference(context: Context): Observable<File>
    fun saveWorkingDirectoryPreference(context: Context, file: File): Observable<Boolean>
}