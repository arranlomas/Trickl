package com.shwifty.tex.views.base

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import es.dmoral.toasty.Toasty

/**
 * Created by arran on 8/10/2017.
 */
open class BaseNestedScrollView : NestedScrollView, BaseContract.MvpView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun showError(msg: String) {
        Toasty.error(context, msg, Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", msg)
    }

    override fun showError(stringId: Int) {
        Toasty.error(context, context.getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Error at ${this.javaClass.name}", context.getString(stringId))
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(context, context.getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Info at ${this.javaClass.name}", context.getString(stringId))
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(context, context.getString(stringId), Toast.LENGTH_SHORT, true).show()
        Log.v("Succes at ${this.javaClass.name}", context.getString(stringId))
    }

    override fun setLoading(loading: Boolean) {
        //do nothing, override in other activities if you want to use
    }
}