package com.shwifty.tex.views.settings.mvp

import com.shwifty.tex.views.settings.mvi.SettingsIntents
import com.shwifty.tex.views.settings.mvi.SettingsViewState
import io.reactivex.Observable

/**
 * Created by arran on 16/04/2017.
 */
interface SettingsContract {

    interface Interactor {
        fun attachView(intents: Observable<SettingsIntents>): Observable<SettingsViewState>
    }
}