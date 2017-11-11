package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.views.base.mvi.BaseReducer
import com.shwifty.tex.views.settings.mvp.SettingsContract

/**
 * Created by arran on 11/11/2017.
 */
class SettingsReducer : SettingsContract.Reducer, BaseReducer<SettingsViewState, SettingsIntents>(SettingsViewState()) {

    override fun reduce(event: SettingsIntents) {
        val newState: SettingsViewState = when (event) {
            is SettingsIntents.UpdateWorkingDirectory -> getState().copy(workingDirectoryState = getState().workingDirectoryState.copy(currentWorkingDirectory = event.newDirectory))
            is SettingsIntents.UpdateWorkingDirectoryShowLoading -> getState().copy(workingDirectoryState = getState().workingDirectoryState.copy(isLoading = event.loading))
            is SettingsIntents.UpdateWorkingDirectoryShowError -> getState().copy(workingDirectoryState = getState().workingDirectoryState.copy(errorRes = event.message))
            is SettingsIntents.UpdateWorkingDirectoryShowErrorString -> getState().copy(workingDirectoryState = getState().workingDirectoryState.copy(errorString = event.message))
            is SettingsIntents.UpdateWorkingDirectoryClearErrors -> getState().copy(workingDirectoryState = getState().workingDirectoryState.copy(errorRes = null, errorString = null))
            is SettingsIntents.RestartClient -> getState().copy(restartAppState = getState().restartAppState.copy(restart = true))
        }
        super.saveState(newState)
    }
}