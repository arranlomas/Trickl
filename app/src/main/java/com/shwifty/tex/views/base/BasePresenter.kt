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
            mvpView.setLoading(false)
            e?.localizedMessage?.let { mvpView.showError(e.localizedMessage) }
            e?.printStackTrace()
        }

        override fun onCompleted() {
            mvpView.setLoading(false)
        }

        override fun onStart() {
            mvpView.setLoading(true)
            super.onStart()
        }
    }
}