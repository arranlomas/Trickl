package com.schiwfty.tex.dagger.utilities

/**
 * Created by arran on 17/04/2017.
 */
import com.schiwfty.tex.dagger.RetryAfterTimeoutWithDelay
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Shorthand to set [subscribeOn] and [observeOn] thread for observables
 */
fun <T> Observable<T>.composeForIoTasks(): Observable<T> = compose<T>( {
    it.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .retryWhen(RetryAfterTimeoutWithDelay(3, 2))
})