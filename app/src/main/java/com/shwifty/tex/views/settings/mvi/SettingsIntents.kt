package com.shwifty.tex.views.settings.mvi

import android.content.Context
import android.support.annotation.StringRes
import com.shwifty.tex.views.base.mvi.BaseMviContract
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */
sealed class SettingsIntents : BaseMviContract.Intent {
    data class UpdateWorkingDirectory(val context: Context, val previousDirectory: File, val newDirectory: File, val moveFiles: Boolean) : SettingsIntents()
    data class UpdateWorkingDirectoryShowLoading(val loading: Boolean) : SettingsIntents()
    data class UpdateWorkingDirectoryShowError(@StringRes val message: Int) : SettingsIntents()
    data class UpdateWorkingDirectoryShowErrorString(val message: String) : SettingsIntents()
    class UpdateWorkingDirectoryClearErrors: SettingsIntents()

    class RestartClient : SettingsIntents()
}