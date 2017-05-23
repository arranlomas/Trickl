package com.schiwfty.tex.utils

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * Created by arran on 30/04/2017.
 */
fun <T> Observable<T>.composeIoWithRetry(): Observable<T> = compose<T>({
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retryWhen(RetryAfterTimeoutWithDelay(3, 2))
})

fun <T> Observable<T>.composeIo(): Observable<T> = compose<T>({
    it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
})

private class RetryAfterTimeoutWithDelay(val maxRetries: Int, var delay: Long)
    : Func1<Observable<out Throwable>, Observable<*>> {

    internal var retryCount = 0

    override fun call(attempts: Observable<out Throwable>): Observable<*> {
        return attempts.flatMap({
            if (++retryCount < maxRetries && it is SocketTimeoutException) {
                Observable.timer(delay, TimeUnit.MILLISECONDS)
            } else {
                Observable.error(it as Throwable)
            }
        })
    }
}