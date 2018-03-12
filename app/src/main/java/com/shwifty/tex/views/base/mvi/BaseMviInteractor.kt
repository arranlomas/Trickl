package com.shwifty.tex.views.base.mvi

import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.kontent_android_viewmodel.commons.objects.KontentAndroidViewModel
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

/**
 * Created by arran on 8/11/2017.
 */
abstract class BaseMviViewModel<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>(
        intentToAction: (I) -> A,
        actionProcessor: ObservableTransformer<A, R>,
        defaultState: S,
        reducer: BiFunction<S, R, S>,
        postProcessor: (Function1<S, S>)? = null,
        private val initialIntent: I? = null) :
        KontentAndroidViewModel<I, A, R, S>(intentToAction, actionProcessor, defaultState, reducer, postProcessor, initialIntent)