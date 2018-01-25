package com.shwifty.tex.views.settings.mvi

import android.content.Context
import com.shwifty.tex.views.base.mvi.BaseMviContract
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */
sealed class SettingsIntents : BaseMviContract.Intent {
    data class InitialIntent(val context: Context) : SettingsIntents()
    data class NewWorkingDirectorySelected(val context: Context, val previousDirectory: File, val newDirectory: File, val moveFiles: Boolean) : SettingsIntents()
    data class ToggleWifiOnly(val context: Context, val selected: Boolean) : SettingsIntents()
    class RestartApp : SettingsIntents()
}

sealed class SettingsActions {
    data class ClearErrorsAndUpdateWorkingDirectory(val context: Context, val previousDirectory: File, val newDirectory: File, val moveFiles: Boolean) : SettingsActions()
    object RestartApp : SettingsActions()
    data class LoadPreferences(val context: Context) : SettingsActions()
    data class UpdateWifiOnly(val context: Context, val selected: Boolean) : SettingsActions()
}

sealed class SettingsResult {
    object RestartApp : SettingsResult()

    data class LoadSettingsSuccess(val workingDirectory: File, val wifiOnly: Boolean) : SettingsResult()
    data class LoadSettingsError(val error: Throwable) : SettingsResult()

    object LoadSettingsinFlight : SettingsResult()
    data class UpdateWorkingDirectorySuccess(val newFile: File) : SettingsResult()
    data class UpdateWorkingDirectoryError(val error: Throwable) : SettingsResult()

    object ToggleWifiOnlyInFlight : SettingsResult()
    data class ToggleWifiOnlySuccess(val selected: Boolean) : SettingsResult()
    data class ToggleWifiOnlyError(val error: Throwable) : SettingsResult()
}