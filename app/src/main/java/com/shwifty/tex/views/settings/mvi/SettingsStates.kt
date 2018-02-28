package com.shwifty.tex.views.settings.mvi

import com.arranlomas.kontent.commons.objects.KontentViewState
import com.shwifty.tex.models.AppTheme
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */

data class SettingsViewState(
        val originalWorkingDirectory: File? = null,
        val originalWifiOnly: Boolean? = null,
        val originalTheme: AppTheme? = null,
        val settingsChanged: Boolean = false,

        val currentWorkingDirectory: File? = null,
        val workingDirectoryLoading: Boolean = false,
        val workingDirectoryErrorString: String? = null,

        val wifiOnly: Boolean? = null,
        val wifiOnlyLoading: Boolean = false,
        val wifiOnlyErrorString: String? = null,

        val theme: AppTheme? = null,
        val themeLoading: Boolean = false,
        val themeErrorString: String? = null
) : KontentViewState() {
    companion object Factory {
        fun default(): SettingsViewState {
            return SettingsViewState()
        }
    }
}