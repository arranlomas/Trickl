package com.shwifty.tex.views.settings.mvp

import com.shwifty.tex.views.base.mvi.BaseMviContract
import com.shwifty.tex.views.settings.state.SettingsViewEvent
import com.shwifty.tex.views.settings.state.SettingsViewState

/**
 * Created by arran on 16/04/2017.
 */
interface SettingsContract {

    interface Interactor : BaseMviContract.Interactor<SettingsViewState, SettingsViewEvent>

    interface Reducer : BaseMviContract.Reducer<SettingsViewState, SettingsViewEvent>
}