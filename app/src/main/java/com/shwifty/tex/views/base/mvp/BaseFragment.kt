package com.shwifty.tex.views.base.mvp

import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 * Created by arran on 11/07/2017.
 */
open class BaseFragment : Fragment(), BaseContract.MvpView {

    override fun showError(msg: String) {
        context?.let {
            Toasty.error(it, msg, Toast.LENGTH_SHORT, true).show()
            Log.v("Error at ${this.javaClass.name}", msg)
        }
    }

    override fun showError(stringId: Int) {
        context?.let {
            Toasty.error(it, getString(stringId), Toast.LENGTH_SHORT, true).show()
            Log.v("Error at ${this.javaClass.name}", getString(stringId))
        }
    }


    override fun showInfo(stringId: Int) {
        context?.let {
            Toasty.info(it, getString(stringId), Toast.LENGTH_SHORT, true).show()
            Log.v("Info at ${this.javaClass.name}", getString(stringId))
        }
    }

    override fun showSuccess(stringId: Int) {
        context?.let {
            Toasty.success(it, getString(stringId), Toast.LENGTH_SHORT, true).show()
            Log.v("Success at ${this.javaClass.name}", getString(stringId))
        }
    }

    override fun setLoading(loading: Boolean) {
        //do nothing, override in other activities if you want to use
    }

}