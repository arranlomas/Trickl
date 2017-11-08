package com.shwifty.tex.views.base.mvi

import com.crashlytics.android.Crashlytics
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by arran on 8/11/2017.
 */
open class BaseMviPresenter<S : BaseMviContract.State, in E : BaseMviContract.Event, in R : BaseMviContract.Reducer<S, E>>(private val reducer: R) : BaseMviContract.Presenter<S, E> {
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

    abstract inner class BaseSubscriber<T>(val showLoading: Boolean = true) : Subscriber<T>() {

        //TODO work out how to do this with the reducer pattern. Might just have to directly access the view :(
        override fun onError(e: Throwable?) {
            Crashlytics.logException(e)
//            if (showLoading) reducer.reduce(BaseMviContract.Event.UpdateLoading(false))
//            e?.localizedMessage?.let { mvpView.showError(e.localizedMessage) }
//            e?.printStackTrace()
        }

        override fun onCompleted() {
//            if (showLoading) mvpView.setLoading(false)
        }

        override fun onStart() {
//            if (showLoading) mvpView.setLoading(true)
//            super.onStart()
        }
    }
}