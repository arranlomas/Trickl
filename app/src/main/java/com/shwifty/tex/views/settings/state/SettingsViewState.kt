package com.shwifty.tex.views.settings.state

import android.content.Context
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.shwifty.tex.views.base.mvi.BaseReducer
import com.shwifty.tex.views.base.mvi.BaseViewEvent
import com.shwifty.tex.views.base.mvi.BaseViewState
import com.shwifty.tex.views.settings.mvp.SettingsContract
import rx.subjects.PublishSubject
import java.io.File

/**
 * Created by arran on 31/10/2017.
 */
data class SettingsViewState(
        val currentWorkingDirectory: File = Confluence.workingDir,
        val workingDirectoryUpdated: Boolean = false,
        val selectNewWorkingDirectory: Boolean = false
) : BaseViewState()

class SettingsReducer : SettingsContract.Reducer, BaseReducer<SettingsViewState, SettingsViewEvent>(SettingsViewState()) {

    private val stateChangeSubject: PublishSubject<SettingsViewState> = PublishSubject.create()

    override fun reduce(event: SettingsViewEvent) {
        val newState: SettingsViewState = when (event) {
            is SettingsViewEvent.UpdateWorkingDirectory -> getState().copy(currentWorkingDirectory = event.newDirectory)
            is SettingsViewEvent.SelectNewWorkingDirectory -> getState().copy(selectNewWorkingDirectory = event.selecting)
            is SettingsViewEvent.WorkingDirectoryUpdated -> getState().copy(workingDirectoryUpdated = event.updated)
            else -> getState()
        }
        super.saveState(newState)
    }
}

sealed class SettingsViewEvent : BaseViewEvent() {
    data class UpdateWorkingDirectory(val context: Context, val newDirectory: File) : SettingsViewEvent()
    data class WorkingDirectoryUpdated(val updated: Boolean) : SettingsViewEvent()
    data class SelectNewWorkingDirectory(val selecting: Boolean) : SettingsViewEvent()
}