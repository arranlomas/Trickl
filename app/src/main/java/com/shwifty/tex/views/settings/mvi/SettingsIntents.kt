package com.shwifty.tex.views.settings.mvi

import android.content.Context
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.shwifty.tex.models.AppTheme
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */
sealed class SettingsIntents : KontentIntent() {
    data class InitialIntent(val context: Context) : SettingsIntents()
    data class NewWorkingDirectorySelected(val context: Context, val newDirectory: File, val moveFiles: Boolean) : SettingsIntents()
    data class ToggleWifiOnly(val context: Context, val selected: Boolean) : SettingsIntents()
    data class ChangeTheme(val context: Context, val newTheme: AppTheme) : SettingsIntents()
}