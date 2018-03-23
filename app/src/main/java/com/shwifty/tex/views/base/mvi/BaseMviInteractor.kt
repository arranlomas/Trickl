package com.shwifty.tex.views.base.mvi

import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.kontent_android_viewmodel.commons.objects.KontentAndroidViewModel
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

/**
 * Created by arran on 8/11/2017.
 */
abstract class BaseMviViewModel<A : KontentAction, R : KontentResult, S : KontentViewState>(
        actionProcessor: ObservableTransformer<A, R>,
        defaultState: S,
        reducer: BiFunction<S, R, S>,
        postProcessor: (Function1<S, S>)? = null) :
        KontentAndroidViewModel<A, R, S>(actionProcessor, defaultState, reducer, postProcessor)