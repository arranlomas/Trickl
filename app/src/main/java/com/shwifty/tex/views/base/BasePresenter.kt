package com.shwifty.tex.views.base

import rx.Subscriber
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by arran on 11/07/2017.
 */
open class BasePresenter<T : BaseContract.MvpView> : BaseContract.Presenter<T> {
    
    lateinit var mvpView: T
        private set

    val subscriptions = CompositeSubscription()

    override fun attachView(mvpView: T) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        subscriptions.unsubscribe()
    }

    fun Subscription.addSubscription() {
        subscriptions.add(this)
    }

    abstract inner class BaseSubscriber<T> : Subscriber<T>() {
        override fun onError(e: Throwable?) {
            e?.localizedMessage?.let { mvpView.showError(e.localizedMessage) }
        }

        override fun onCompleted() {
        }

        override fun onStart() {
            super.onStart()
        }
    }
}