package com.shwifty.tex.utils

import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import rx.functions.Func1
import java.util.concurrent.TimeUnit


/**
 * Created by arran on 30/04/2017.
 */
fun <T> Observable<T>.composeIo(): Observable<T> = compose<T>({
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
})

fun <T> createObservable(event: (Emitter<T>) -> Unit): Observable<T> {
    return Observable.create<T> { emitter ->
        event.invoke(emitter)
    }
}

fun <T> Observable<T>.composeIoWithRetryXTimes(times: Int): Observable<T> = compose<T>({
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retryWhen({
                var retryCount = 0
                if (++retryCount >= times) Observable.timer(0, TimeUnit.MILLISECONDS)
                else error(it)
            })
})

internal class ObservableV1ToObservableV2<T>(val source: rx.Observable<T>) : io.reactivex.Observable<T>() {

    override fun subscribeActual(s: io.reactivex.Observer<in T>) {
        val parent = ObservableSubscriber(s)
        s.onSubscribe(parent)

        source.unsafeSubscribe(parent)
    }

    internal class ObservableSubscriber<T>(val actual: io.reactivex.Observer<in T>) : rx.Subscriber<T>(), io.reactivex.disposables.Disposable {

        var done: Boolean = false

        override fun onNext(t: T?) {
            if (done) {
                return
            }
            if (t == null) {
                unsubscribe()
                onError(NullPointerException(
                        "The upstream 1.x Observable signalled a null value which is not supported in 2.x"))
            } else {
                actual.onNext(t)
            }
        }

        override fun onError(e: Throwable) {
            if (done) {
                io.reactivex.plugins.RxJavaPlugins.onError(e)
                return
            }
            done = true
            actual.onError(e)
            unsubscribe() // v1 expects an unsubscribe call when terminated
        }

        override fun onCompleted() {
            if (done) {
                return
            }
            done = true
            actual.onComplete()
            unsubscribe() // v1 expects an unsubscribe call when terminated
        }

        override fun dispose() {
            unsubscribe()
        }

        override fun isDisposed(): Boolean {
            return isUnsubscribed
        }
    }
}
