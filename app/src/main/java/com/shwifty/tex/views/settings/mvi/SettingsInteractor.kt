package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.views.base.mvi.BaseMviInteractor

/**
 * Created by arran on 7/05/2017.
 */
class SettingsInteractor(preferencesRepository: IPreferenceRepository)
    : SettingsContract.Interactor, BaseMviInteractor<SettingsIntents, SettingsActions, SettingsResult, SettingsViewState>(
        intentToAction = { settingsIntentToAction(it) },
        actionProcessor = settingsActionProcessor(preferencesRepository),
        reducer = settingsReducer,
        defaultState = SettingsViewState.default(),
        postProcessor = postProcessor()
)