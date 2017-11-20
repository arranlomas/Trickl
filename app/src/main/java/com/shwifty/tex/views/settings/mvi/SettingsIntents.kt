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
    class RestartApp : SettingsIntents()
}

sealed class SettingsActions {
    data class ClearErrorsAndUpdateWorkingDirectory(val context: Context, val previousDirectory: File, val newDirectory: File, val moveFiles: Boolean) : SettingsActions()
    object RestartApp : SettingsActions()
    data class LoadWorkingDirectory(val context: Context) : SettingsActions()
}

sealed class SettingsResult {
    object RestartApp : SettingsResult()
    object UpdateWorkingDirectoryInFlight : SettingsResult()
    data class UpdateWorkingDirectorySuccess(val newFile: File) : SettingsResult()
    data class UpdateWorkingDirectoryError(val error: Throwable) : SettingsResult()
    data class LoadWorkingDirectorySuccess(val newFile: File) : SettingsResult()
    data class LoadWorkingDirectoryError(val error: Throwable) : SettingsResult()
}