package com.shwifty.tex.views.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 * Created by arran on 11/07/2017.
 */
open class BaseFragment : Fragment(), BaseContract.MvpView {

    fun getActivityContext(): Context = activity as Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun showError(msg: String) {
        Toasty.error(getActivityContext(), msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showError(stringId: Int) {
        Toasty.error(getActivityContext() , getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", getString(stringId))
    }


    override fun showInfo(stringId: Int) {
        Toasty.info(getActivityContext(), getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Info at ${this.javaClass.name}", getString(stringId))
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(getActivityContext(), getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Success at ${this.javaClass.name}", getString(stringId))
    }
}