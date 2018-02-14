package com.shwifty.tex.views.settings.mvi

import com.arranlomas.kontent.commons.objects.mvi.KontentResult
import com.shwifty.tex.models.AppTheme
import java.io.File

/**
 * Created by arran on 14/02/2018.
 */
sealed class SettingsResult : KontentResult() {
    data class LoadSettingsSuccess(val workingDirectory: File, val wifiOnly: Boolean, val theme: AppTheme) : SettingsResult()
    data class LoadSettingsError(val error: Throwable) : SettingsResult()
    object LoadSettingsinFlight : SettingsResult()

    object UpdateWorkingDirectoryInFlight : SettingsResult()
    data class UpdateWorkingDirectorySuccess(val newFile: File) : SettingsResult()
    data class UpdateWorkingDirectoryError(val error: Throwable) : SettingsResult()

    object ToggleWifiOnlyInFlight : SettingsResult()
    data class ToggleWifiOnlySuccess(val wifiOnly: Boolean) : SettingsResult()
    data class ToggleWifiOnlyError(val error: Throwable) : SettingsResult()

    object ChangeThemeInFlight : SettingsResult()
    data class ChangeThemeSuccess(val theme: AppTheme) : SettingsResult()
    data class ChangeThemeError(val error: Throwable) : SettingsResult()
}