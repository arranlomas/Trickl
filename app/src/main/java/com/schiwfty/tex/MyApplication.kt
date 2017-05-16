package com.schiwfty.tex

import android.app.Application
import io.realm.Realm

/**
 * Created by arran on 29/04/2017.
 */
class MyApplication : Application() {

    override fun onCreate() {
        Realm.init(this)
        TricklComponent.install(this)
    }


}