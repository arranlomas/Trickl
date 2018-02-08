package com.shwifty.tex.views.settings.mvi

import android.content.Context
import com.shwifty.tex.models.AppTheme
import com.shwifty.tex.views.base.mvi.BaseMviContract
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */
sealed class SettingsIntents : BaseMviContract.Intent {
    data class InitialIntent(val context: Context) : SettingsIntents()
    data class NewWorkingDirectorySelected(val context: Context, val newDirectory: File, val moveFiles: Boolean) : SettingsIntents()
    data class ToggleWifiOnly(val context: Context, val selected: Boolean) : SettingsIntents()
    data class ChangeTheme(val context: Context, val newTheme: AppTheme) : SettingsIntents()
    data class ResetSettings(val context: Context) : SettingsIntents()
}

sealed class SettingsActions {
    data class ClearErrorsAndUpdateWorkingDirectory(val context: Context, val newDirectory: File, val moveFiles: Boolean) : SettingsActions()
    data class LoadPreferencesForFirstTime(val context: Context) : SettingsActions()
    data class UpdateWifiOnly(val context: Context, val selected: Boolean) : SettingsActions()
    data class ChangeTheme(val context: Context, val theme: AppTheme) : SettingsActions()
    data class ResetSettings(val context: Context) : SettingsActions()
}

sealed class SettingsResult {
    data class LoadSettingsSuccess(val workingDirectory: File, val wifiOnly: Boolean, val theme: AppTheme) : SettingsResult()
    data class LoadSettingsError(val error: Throwable) : SettingsResult()
    object LoadSettingsinFlight : SettingsResult()

    object UpdateworkingDirectoryInFlight : SettingsResult()
    data class UpdateWorkingDirectorySuccess(val newFile: File) : SettingsResult()
    data class UpdateWorkingDirectoryError(val error: Throwable) : SettingsResult()

    object ToggleWifiOnlyInFlight : SettingsResult()
    data class ToggleWifiOnlySuccess(val wifiOnly: Boolean) : SettingsResult()
    data class ToggleWifiOnlyError(val error: Throwable) : SettingsResult()

    object ToggleChangeThemeInFlight : SettingsResult()
    data class ToggleChangeThemeSuccess(val theme: AppTheme) : SettingsResult()
    data class ToggleChangeThemeError(val error: Throwable) : SettingsResult()
}