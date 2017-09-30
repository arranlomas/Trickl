package com.shwifty.tex.views.base

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 * Created by arran on 11/07/2017.
 */
open class BaseActivity: AppCompatActivity(), BaseContract.MvpView {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun showError(msg: String) {
        Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showError(stringId: Int) {
        Toasty.error(this,getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringId))
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(this, getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Info at ${this.javaClass.name}", getString(stringId))
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(this, getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Succes at ${this.javaClass.name}", getString(stringId))
    }
}