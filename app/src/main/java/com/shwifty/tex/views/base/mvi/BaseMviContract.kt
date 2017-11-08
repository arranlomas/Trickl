package com.shwifty.tex.views.base.mvi

import rx.Observable
import rx.subjects.PublishSubject

/**
 * Created by arran on 11/07/2017.
 */
interface BaseMviContract {

    interface Presenter<S : State, in E : Event> {
        fun getInitialState(): S
        fun getViewStateStream(): Observable<S>
        fun publishEvent(event: E)
    }

    interface State {
        val showLoading: Boolean
        val showErrorMessage: String?
        val showErrorRes: Int?
        val showSuccessMessage: String?
        val showSuccessRes: Int?
        val showInfoMessage: String?
        val showInfoRes: Int?
    }

    interface Event

    interface Reducer<S : State, in E : Event> {
        fun reduce(event: E)
        fun getInitialState(): S
        fun getState(): S
        fun getViewStateChangeStream(): PublishSubject<S>
    }
}