package com.shwifty.tex.views.settings.mvi

import com.arranlomas.kontent.commons.objects.KontentContract

/**
 * Created by arran on 16/04/2017.
 */
interface SettingsContract {
    interface ViewModel : KontentContract.ViewModel<SettingsActions, SettingsResult, SettingsViewState>
}