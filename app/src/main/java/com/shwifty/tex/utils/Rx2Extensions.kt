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
