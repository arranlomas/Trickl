package com.shwifty.tex.views.base.mvi

import com.arranlomas.kontent.commons.objects.*
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

/**
 * Created by arran on 8/11/2017.
 */
abstract class BaseMviInteractor<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>(
        intentToAction: (I) -> A,
        actionProcessor: ObservableTransformer<A, R>,
        defaultState: S,
        reducer: BiFunction<S, R, S>,
        postProcessor: (Function1<S, S>)? = null) :
        KontentInteractor<I, A, R, S>(intentToAction, actionProcessor, defaultState, reducer, postProcessor)