package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.views.base.mvi.BaseMviViewModel
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class SettingsViewModel @Inject constructor(preferencesRepository: IPreferenceRepository)
    : SettingsContract.ViewModel, BaseMviViewModel<SettingsActions, SettingsResult, SettingsViewState>(
        actionProcessor = settingsActionProcessor(preferencesRepository),
        reducer = settingsReducer,
        defaultState = SettingsViewState.default(),
        postProcessor = postProcessor()
)