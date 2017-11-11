package com.shwifty.tex.views.settings.state

import android.content.Context
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.views.base.mvi.BaseReducer
import com.shwifty.tex.views.base.mvi.BaseViewEvent
import com.shwifty.tex.views.base.mvi.BaseViewState
import com.shwifty.tex.views.settings.mvp.SettingsContract
import java.io.File

/**
 * Created by arran on 31/10/2017.
 */
data class SettingsViewState(
        val currentWorkingDirectory: File = Confluence.workingDir,
        val restartApp: Boolean = false
) : BaseViewState()

sealed class SettingsViewEvent : BaseViewEvent() {
    data class UpdateWorkingDirectory(val context: Context, val previousDirectory: File, val newDirectory: File, val moveFiles: Boolean) : SettingsViewEvent()
    data class RestartClient(val restart: Boolean) : SettingsViewEvent()
}

class SettingsReducer : SettingsContract.Reducer, BaseReducer<SettingsViewState, SettingsViewEvent>(SettingsViewState()) {

    override fun reduce(event: SettingsViewEvent) {
        val newState: SettingsViewState = when (event) {
            is SettingsViewEvent.UpdateWorkingDirectory -> getState().copy(currentWorkingDirectory = event.newDirectory)
            is SettingsViewEvent.RestartClient -> getState().copy(restartApp = event.restart)
        }
        super.saveState(newState)
    }
}