package com.shwifty.tex.views.base.mvi

import com.crashlytics.android.Crashlytics
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by arran on 8/11/2017.
 */
open class BaseMviInteractor<S : BaseMviContract.State, in E : BaseMviContract.Intent, in R : BaseMviContract.Reducer<S, E>>(private val reducer: R) : BaseMviContract.Interactor<S, E> {
    override fun getInitialState(): S {
        return reducer.getState()
    }

    val subscriptions = CompositeSubscription()

    override fun getViewStateStream(): Observable<S> {
        return reducer.getViewStateChangeStream()
    }

    override fun publishEvent(event: E) {
        reducer.reduce(event)
    }

    fun Subscription.addSubscription() {
        subscriptions.add(this)
    }

    inner class BaseSubscriber<T>(val onNextAction: (T) -> Unit, val onErrorAction: (Throwable) -> Unit, val onLoadingEvent: ((Boolean) -> Unit)? = null) : Subscriber<T>() {
        override fun onNext(t: T) {
            onLoadingEvent?.invoke(false)
            onNextAction.invoke(t)
        }

        override fun onError(e: Throwable) {
            Crashlytics.logException(e)
            onLoadingEvent?.invoke(false)
            onErrorAction.invoke(e)
        }

        override fun onCompleted() {
            onLoadingEvent?.invoke(false)
        }

        override fun onStart() {
            onLoadingEvent?.invoke(true)
        }
    }
}