package com.schiwfty.tex.tools

/**
 * Created by arran on 17/04/2017.
 */
import android.util.Log
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Shorthand to set [subscribeOn] and [observeOn] thread for observables
 */
fun <T> Observable<T>.composeIoWithRetry(): Observable<T> = compose<T>( {
    it.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryAfterTimeoutWithDelay(3, 2))
})

fun <T> Observable<T>.composeIo(): Observable<T> = compose<T>( {
    it.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryAfterTimeoutWithDelay(3, 2))
})

fun Process.captureOutput() {
    val t = Thread{
        errorStream.bufferedReader().use {
            Log.v("ERROR", "value: " + it.readText())
        }

        inputStream.bufferedReader().use {
            Log.v("STD OUT", "value: " + it.readText())
        }
    }
    t.start()
}