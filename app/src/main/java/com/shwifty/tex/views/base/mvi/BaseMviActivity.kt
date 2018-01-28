package com.shwifty.tex.views.base.mvi

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.shwifty.tex.utils.oncreateSetThemeAndCallSuper
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by arran on 11/07/2017.
 */
open class BaseMviActivity<S : BaseMviContract.ViewState, E : BaseMviContract.Intent> : AppCompatActivity() {

    private val subscriptions = CompositeDisposable()
    private lateinit var interactor: BaseMviContract.Interactor<S, E>
    private lateinit var intents: Observable<E>

    override fun onCreate(savedInstanceState: Bundle?) {
        oncreateSetThemeAndCallSuper { super.onCreate(savedInstanceState) }
    }

    fun setup(interactor: BaseMviContract.Interactor<S, E>) {
        this.interactor = interactor
    }

    fun attachIntents(intents: Observable<E>) {
        this.intents = intents
        interactor.attachView(intents)
                .subscribeWith(object : BaseSubscriber<S>() {
                    override fun onNext(state: S) {
                        render(state)
                    }
                })
                .addDisposable()
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

        override fun onStart() {

        }

        override fun onComplete() {

        }
    }

    open fun render(state: S) {
        TODO("Error! You should not reach hear this should be overridden")
    }
}