package com.shwifty.tex.views.settings.mvi

import android.content.Context
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */
sealed class SettingsIntents {
    data class NewWorkingDirectorySelected(val context: Context, val previousDirectory: File, val newDirectory: File, val moveFiles: Boolean) : SettingsIntents()
    class RestartApp : SettingsIntents()
}

sealed class SettingsActions {
    data class ClearErrorsAndUpdateWorkingDirectory(val context: Context, val previousDirectory: File, val newDirectory: File, val moveFiles: Boolean) : SettingsActions()
    object RestartApp : SettingsActions()
}

sealed class SettingsResult {
    object RestartApp : SettingsResult()
    object UpdateWorkingDirectoryInFlight : SettingsResult()
    data class UpdateWorkingDirectorySuccess(val newFile: File) : SettingsResult()
    data class UpdateWorkingDirectoryError(val error: Throwable) : SettingsResult()
}