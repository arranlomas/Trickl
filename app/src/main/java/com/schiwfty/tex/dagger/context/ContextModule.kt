package com.schiwfty.tex.dagger.context

import android.content.Context

import com.schiwfty.tex.dagger.network.NetworkScope

import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */

@Module
class ContextModule(private val context: Context) {

    @Provides
    @NetworkScope
    fun context(): Context {
        return context
    }
}
