package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.views.base.mvi.BaseMviContract
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */

data class SettingsViewState(
        val currentWorkingDirectory: File? = null,
        val wifiOnly: Boolean = false,
        val loadSettingsErrorString: String? = null,
        val workingDirectoryErrorString: String? = null,
        val wifiOnlyErrorString: String? = null,
        val isLoading: Boolean = false,
        val restart: Boolean = false
) : BaseMviContract.ViewState {
    companion object Factory {
        fun default(): SettingsViewState {
            return SettingsViewState()
        }
    }
}