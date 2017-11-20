package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.views.base.mvi.BaseMviInteractor

/**
 * Created by arran on 7/05/2017.
 */
class SettingsInteractor(val preferencesRepository: IPreferenceRepository)
    : SettingsContract.Interactor, BaseMviInteractor<SettingsViewState, SettingsIntents>() {
    init {
        super.processor = { intents ->
            intents
                    .map { intent -> actionFromIntent(intent) }
                    .compose(settingsActionProcessor(preferencesRepository))
                    .scan(SettingsViewState.default(), reducer)
        }
    }
}
