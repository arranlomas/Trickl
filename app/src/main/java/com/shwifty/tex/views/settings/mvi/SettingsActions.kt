package com.shwifty.tex.views.settings.mvi

import android.content.Context
import com.arranlomas.kontent.commons.objects.KontentAction
import com.shwifty.tex.models.AppTheme
import java.io.File

/**
 * Created by arran on 14/02/2018.
 */
sealed class SettingsActions : KontentAction() {
    data class UpdateWorkingDirectory(val context: Context, val newDirectory: File, val moveFiles: Boolean) : SettingsActions()
    data class LoadPreferencesForFirstTime(val context: Context) : SettingsActions()
    data class UpdateWifiOnly(val context: Context, val selected: Boolean) : SettingsActions()
    data class ChangeTheme(val context: Context, val theme: AppTheme) : SettingsActions()
}
