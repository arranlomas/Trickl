package com.shwifty.tex.views.settings.mvi

import com.arranlomas.kontent.commons.objects.mvi.KontentContract
import com.arranlomas.kontent.commons.objects.mvi.KontentInteractor

/**
 * Created by arran on 16/04/2017.
 */
interface SettingsContract {
    interface Interactor : KontentContract.Interactor<SettingsIntents, SettingsViewState>
}