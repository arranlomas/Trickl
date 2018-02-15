package com.shwifty.tex.views.base.mvi

import android.annotation.SuppressLint
import android.os.Bundle
import com.arranlomas.kontent.commons.objects.android.KontentActivity
import com.arranlomas.kontent.commons.objects.android.KontentFragment
import com.arranlomas.kontent.commons.objects.mvi.KontentIntent
import com.arranlomas.kontent.commons.objects.mvi.KontentViewState

/**
 * Created by arran on 11/07/2017.
 */
abstract class BaseMviFragment<I : KontentIntent, S : KontentViewState> : KontentFragment<I, S>()