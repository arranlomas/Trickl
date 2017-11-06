package com.shwifty.tex.views.settings.state

import com.schiwfty.torrentwrapper.confluence.Confluence
import rx.subjects.PublishSubject
import java.io.File

/**
 * Created by arran on 31/10/2017.
 */
data class SettingsViewState(
        val currentWorkingDirectory: File = Confluence.workingDir
)

class SettingsReducer {
    private var state = SettingsViewState()

    private val stateChangeSubject: PublishSubject<SettingsViewState> = PublishSubject.create()

    fun reduce(event: SettingsViewEvent) {
        val newState: SettingsViewState = when (event) {
            is SettingsViewEvent.UpdateWorkingDirectory -> state.copy(currentWorkingDirectory = event.newDirectory)
            else -> state
        }
        state = newState
        stateChangeSubject.onNext(state)
    }

    fun getState(): SettingsViewState {
        return state
    }

    fun getViewStateChangeStream(): PublishSubject<SettingsViewState> {
        return stateChangeSubject
    }
}

sealed class SettingsViewEvent {
    data class UpdateWorkingDirectory(val newDirectory: File) : SettingsViewEvent()
}