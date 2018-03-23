package com.shwifty.tex.views.base.mvi

import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.kotentdaggersupport.KontentDaggerSupportFragment

/**
 * Created by arran on 11/07/2017.
 */
abstract class BaseDaggerMviFragment<A : KontentAction, R : KontentResult, S : KontentViewState>
    : KontentDaggerSupportFragment<A, R, S>()