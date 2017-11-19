package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.views.base.mvi.BaseMviInteractor
import com.shwifty.tex.views.settings.mvp.SettingsContract

/**
 * Created by arran on 7/05/2017.
 */
class SettingsInteractor(preferencesRepository: IPreferenceRepository) : BaseMviInteractor<SettingsViewState, SettingsIntents>(), SettingsContract.Interactor {
    init {
        intentsSubject
                .map { intent -> actionFromIntent(intent) }
                .compose(settingsActionProcessor(preferencesRepository))
                .scan(SettingsViewState.default(), reducer)
    }
}

