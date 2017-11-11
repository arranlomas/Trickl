package com.shwifty.tex.views.base.mvi

import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 11/07/2017.
 */
interface BaseMviContract {

    interface Interactor<S : State, in E : Intent> {
        fun getInitialState(): S
        fun getViewStateStream(): Observable<S>
        fun publishEvent(event: E)
    }

    interface State

    interface Intent

    interface Reducer<S : State, in E : Intent> {
        fun reduce(event: E)
        fun getInitialState(): S
        fun getState(): S
        fun getViewStateChangeStream(): PublishSubject<S>
    }
}