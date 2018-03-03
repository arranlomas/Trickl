package com.shwifty.tex.views.base.mvi

import com.arranlomas.daggerviewmodelhelper.Injectable
import com.arranlomas.kontent.commons.objects.KontentFragment
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.kontent.commons.objects.KontentViewState

/**
 * Created by arran on 11/07/2017.
 */
abstract class BaseDaggerMviFragment<I : KontentIntent, S : KontentViewState> : KontentFragment<I, S>(), Injectable