package com.shwifty.tex.views.base.mvi

import android.annotation.SuppressLint
import android.os.Bundle
import com.arranlomas.kontent.commons.objects.KontentActivity
import com.arranlomas.kontent.commons.objects.KontentIntent
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.shwifty.tex.utils.onCreateSetThemeAndCallSuper

/**
 * Created by arran on 11/07/2017.
 */
abstract class BaseDaggerMviActivity<I : KontentIntent, S : KontentViewState> : KontentActivity<I, S>() {
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateSetThemeAndCallSuper { super.onCreate(savedInstanceState) }
    }
}