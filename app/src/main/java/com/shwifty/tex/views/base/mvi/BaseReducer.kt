package com.shwifty.tex.views.base.mvi

import rx.subjects.PublishSubject

/**
 * Created by arran on 8/11/2017.
 */
open class BaseReducer<S : BaseMviContract.State, in E : BaseMviContract.Event>(private val initialState: S) : BaseMviContract.Reducer<S, E> {
    override fun getInitialState(): S {
        return initialState
    }

    private var savedState: S = initialState

    private val stateChangeSubject: PublishSubject<S> = PublishSubject.create()

    override fun getState(): S = savedState

    override fun getViewStateChangeStream(): PublishSubject<S> = stateChangeSubject

    override fun reduce(event: E) {
        TODO("You messed up. This method should always be overridden. You should never of come here!")
    }

    fun saveState(state: S) {
        savedState = state
        stateChangeSubject.onNext(state)
    }

    init {
        stateChangeSubject.onNext(savedState)
    }
}