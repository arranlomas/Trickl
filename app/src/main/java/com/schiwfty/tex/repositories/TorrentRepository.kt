package com.schiwfty.tex.repositories

import com.schiwfty.tex.retrofit.ConfluenceApi
import com.schiwfty.tex.utils.composeIoWithRetry
import rx.Observable


/**
 * Created by arran on 29/04/2017.
 */
class TorrentRepository(val confluenceApi: ConfluenceApi) : ITorrentRepository {
    var runningHeartbeat = true

//    override fun getClientHeartbeat(): Observable<Boolean> {
////        return Observable.fromCallable<Any> { confluenceApi.getGetStatus }
////                .repeatWhen { o -> o.concatMap<Any> { getGetStatus -> Observable.timer(20, TimeUnit.SECONDS) } }
//        Observable.interval(60, TimeUnit.SECONDS)
//                .flatMap<Any> { n -> confluenceApi.getGetStatus}
//                .retryWhen { runningHeartbeat == true }
////                .repeatWhen { runningHeartbeat == true }
////                .map { it.toString() }
//
//    }

    override fun getStatus(): Observable<String> {
        return confluenceApi.getStatus
                .composeIoWithRetry()
                .map { it.toString() }
    }
}