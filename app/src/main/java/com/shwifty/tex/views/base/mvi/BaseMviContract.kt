package com.shwifty.tex.views.base.mvi

import io.reactivex.Observable

/**
 * Created by arran on 11/07/2017.
 */
interface BaseMviContract {

    interface Interactor<S : ViewState, E : Intent> {
        fun attachView(intents: Observable<E>): Observable<S>
    }

    interface ViewState

    interface Intent
}