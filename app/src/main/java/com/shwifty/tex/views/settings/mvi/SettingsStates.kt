package com.shwifty.tex.views.settings.mvi

import com.schiwfty.torrentwrapper.confluence.Confluence
import java.io.File

/**
 * Created by arran on 11/11/2017.
 */

data class SettingsViewState(
        val workingDirectoryState: WorkingDirectoryState = WorkingDirectoryState(),
        val restartAppState: RestartAppState = RestartAppState()
){
    companion object Factory{
        fun default() = SettingsViewState()
    }
}

data class WorkingDirectoryState(
        val currentWorkingDirectory: File = Confluence.workingDir,
        val errorRes: Int? = null,
        val errorString: String? = null,
        val isLoading: Boolean = false
)

data class RestartAppState(
        val restart: Boolean = false
)