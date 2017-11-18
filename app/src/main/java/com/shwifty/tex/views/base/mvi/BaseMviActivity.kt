package com.shwifty.tex.views.base.mvi

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import es.dmoral.toasty.Toasty
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by arran on 11/07/2017.
 */
open class BaseMviActivity : AppCompatActivity() {

    val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    fun Disposable.addDisposable() {
        subscriptions.add(this)
    }


    abstract inner class BaseSubscriber<T>(val showLoading: Boolean = true) : DisposableObserver<T>() {
        override fun onError(e: Throwable) {
            Crashlytics.logException(e)
            runOnUiThread { Toasty.error(this@BaseMviActivity, e.localizedMessage, Toast.LENGTH_LONG).show() }
        }

        override fun onStart() {g

        }

        override fun onComplete() {

        }
    }
}