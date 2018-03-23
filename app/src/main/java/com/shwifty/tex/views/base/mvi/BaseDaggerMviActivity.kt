package com.shwifty.tex.views.base.mvi

import android.annotation.SuppressLint
import android.os.Bundle
import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.kotentdaggersupport.KontentDaggerSupportActivity
import com.shwifty.tex.utils.onCreateSetThemeAndCallSuper

/**
 * Created by arran on 11/07/2017.
 */
abstract class BaseDaggerMviActivity<A : KontentAction, R : KontentResult, S : KontentViewState> : KontentDaggerSupportActivity<A, R, S>() {

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateSetThemeAndCallSuper { super.onCreate(savedInstanceState) }
    }
}