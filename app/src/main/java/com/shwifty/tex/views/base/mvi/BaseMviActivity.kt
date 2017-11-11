package com.shwifty.tex.views.base.mvi

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import es.dmoral.toasty.Toasty
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by arran on 11/07/2017.
 */
open class BaseMviActivity : AppCompatActivity() {

    val subscriptions = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.unsubscribe()
    }

    fun Subscription.addSubscription() {
        subscriptions.add(this)
    }

    abstract inner class BaseSubscriber<T>(val showLoading: Boolean = true) : Subscriber<T>() {

        override fun onError(e: Throwable) {
            Crashlytics.logException(e)
            runOnUiThread { Toasty.error(this@BaseMviActivity, e.localizedMessage, Toast.LENGTH_LONG).show() }
        }

        override fun onCompleted() {
        }

        override fun onStart() {

        }
    }

    fun <T> Observable<T>.subscribeToEventStream(onNext: (T) -> Unit) {
        this.subscribe(object : BaseSubscriber<T>() {
            override fun onNext(t: T) {
                onNext.invoke(t)
            }
        }).addSubscription()
    }
}