//package com.shwifty.tex.views.base.mvi
//
//import io.reactivex.Observable
//import io.reactivex.subjects.PublishSubject
//
///**
// * Created by arran on 8/11/2017.
// */
////open class BaseMviInteractor<S : BaseMviContract.ViewState, E : BaseMviContract.Intent> : BaseMviContract.Interactor<S, E> {
////
////
////    override fun attachView(intents: Observable<E>): Observable<S> {
////        intents.subscribe(intentsSubject)
////        return stateSubject
////    }
//}