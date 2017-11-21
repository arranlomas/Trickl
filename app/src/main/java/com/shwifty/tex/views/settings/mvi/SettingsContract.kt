package com.shwifty.tex.views.settings.mvi

import com.shwifty.tex.views.base.mvi.BaseMviContract
import com.shwifty.tex.views.settings.mvi.SettingsIntents
import com.shwifty.tex.views.settings.mvi.SettingsViewState

/**
 * Created by arran on 16/04/2017.
 */
interface SettingsContract {
    interface Interactor : BaseMviContract.Interactor<SettingsViewState, SettingsIntents>
}