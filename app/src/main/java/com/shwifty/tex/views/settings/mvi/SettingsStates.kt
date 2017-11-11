package com.shwifty.tex.views.settings.mvi

import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.views.base.mvi.BaseViewState
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */

data class SettingsViewState(
        val workingDirectoryState: WorkingDirectoryState = WorkingDirectoryState(),
        val restartAppState: RestartAppState = RestartAppState()
) : BaseViewState()

data class WorkingDirectoryState(
        val currentWorkingDirectory: File = Confluence.workingDir,
        val errorRes: Int? = null,
        val errorString: String? = null,
        val isLoading: Boolean = false
)

data class RestartAppState(
        val restart: Boolean = false
)