package com.shwifty.tex.views.base.mvi

import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.kotentdaggersupport.KontentDaggerSupportFragment

/**
 * Created by arran on 11/07/2017.
 */
abstract class BaseDaggerMviFragment<I : KontentIntent, A : KontentAction, R : KontentResult, S : KontentViewState>
    : KontentDaggerSupportFragment<I, A, R, S>()