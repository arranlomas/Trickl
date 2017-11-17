package com.shwifty.tex.views.settings.mvi

import android.content.Context
import android.support.annotation.StringRes
import com.shwifty.tex.views.base.mvi.BaseMviContract
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */
sealed class SettingsIntents : BaseMviContract.Intent {
    data class NewWorkingDirectorySelected(val context: Context, val previousDirectory: File, val newDirectory: File, val moveFiles: Boolean) : SettingsIntents()
    class RestartClient : SettingsIntents()
}

sealed class SettingsActions: