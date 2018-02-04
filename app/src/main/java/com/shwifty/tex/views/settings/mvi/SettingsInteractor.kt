package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.views.base.mvi.BaseMviInteractor

/**
 * Created by arran on 7/05/2017.
 */
class SettingsInteractor(private val preferencesRepository: IPreferenceRepository)
    : SettingsContract.Interactor, BaseMviInteractor<SettingsViewState, SettingsIntents>() {
    init {
        super.processor = { intents ->
            intents
                    .map { intent -> actionFromIntent(intent) }
                    .compose(settingsActionProcessor(preferencesRepository))
                    .scan(SettingsViewState.default(), settingsReducer)
                    .map(postProcessor())
        }
    }
}

private fun postProcessor(): Function1<SettingsViewState, SettingsViewState> = {
    var settingsChanged = false
    if (it.originalWifiOnly != it.wifiOnly) settingsChanged = true
    if (it.originalTheme != it.theme) settingsChanged = true
    if (it.originalWorkingDirectory != it.currentWorkingDirectory) settingsChanged = true
    it.copy(settingsChanged = settingsChanged)
}
