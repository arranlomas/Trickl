package com.shwifty.tex.views.base

/**
 * Created by arran on 11/07/2017.
 */
interface BaseContract {
    interface MvpView {
        fun showError(msg: String)
        fun showError(stringId: Int)
        fun showInfo(stringId: Int)
        fun showSuccess(stringId: Int)
        fun setLoading(loading: Boolean)
    }

    interface Presenter<in V : MvpView> {
        fun attachView(mvpView: V)
        fun detachView()
    }
}