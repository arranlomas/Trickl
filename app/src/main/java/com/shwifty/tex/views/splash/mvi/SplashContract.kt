package com.shwifty.tex.views.splash.mvi

import com.arranlomas.kontent.commons.objects.KontentContract

/**
 * Created by arran on 16/04/2017.
 */
interface SplashContract {
    interface ViewModel : KontentContract.ViewModel<SplashActions, SplashResult, SplashViewState>
}