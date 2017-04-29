package com.schiwfty.tex

import android.app.Application

/**
 * Created by arran on 29/04/2017.
 */
class MyApplication : Application() {

    override fun onCreate() {
        TricklComponent.install(this)
    }
}