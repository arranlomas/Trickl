package com.shwifty.tex.views.base.mvp

import com.crashlytics.android.Crashlytics
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import rx.Observer
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
    val disposables = CompositeDisposable()

    override fun attachView(mvpView: T) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        subscriptions.unsubscribe()
        disposables.dispose()
    }

    fun Subscription.addSubscription() {
        subscriptions.add(this)
    }

    fun Disposable.addObserver() {
        disposables.add(this)
    }

    abstract inner class BaseObserver<T>(private val showLoading: Boolean = true) : DisposableObserver<T>() {

        override fun onError(e: Throwable) {
            Crashlytics.logException(e)
            if(showLoading) mvpView.setLoading(false)
            e?.localizedMessage?.let { mvpView.showError(e.localizedMessage) }
            e?.printStackTrace()
        }

        override fun onStart() {
            super.onStart()
            if(showLoading) mvpView.setLoading(true)
        }


        override fun onComplete() {
            if(showLoading) mvpView.setLoading(false)
        }
    }

    abstract inner class BaseSubscriber<T>(val showLoading: Boolean = true) : Subscriber<T>() {

        override fun onError(e: Throwable?) {
            Crashlytics.logException(e)
            if(showLoading) mvpView.setLoading(false)
            e?.localizedMessage?.let { mvpView.showError(e.localizedMessage) }
            e?.printStackTrace()
        }

        override fun onCompleted() {
            if(showLoading) mvpView.setLoading(false)
        }

        override fun onStart() {
            if(showLoading) mvpView.setLoading(true)
            super.onStart()
        }
    }
}