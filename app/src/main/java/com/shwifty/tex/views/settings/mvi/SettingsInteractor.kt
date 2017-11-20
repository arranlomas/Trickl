package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.repository.preferences.IPreferenceRepository
//import com.shwifty.tex.views.base.mvi.BaseMviInteractor
import com.shwifty.tex.views.settings.mvp.SettingsContract
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by arran on 7/05/2017.
 */
class SettingsInteractor(val preferencesRepository: IPreferenceRepository) : SettingsContract.Interactor {

    override fun attachView(intents: Observable<SettingsIntents>): Observable<SettingsViewState> {
        return intents
                .map { intent -> actionFromIntent(intent) }
                .compose(settingsActionProcessor(preferencesRepository))
                .scan(SettingsViewState.default(), reducer)
    }
}

