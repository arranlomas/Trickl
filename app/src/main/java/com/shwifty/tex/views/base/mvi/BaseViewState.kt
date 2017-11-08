package com.shwifty.tex.views.base.mvi

/**
 * Created by arran on 8/11/2017.
 */
open class BaseViewState : BaseMviContract.State {
    override val showLoading: Boolean = false
    override val showErrorMessage: String? = null
    override val showErrorRes: Int? = null
    override val showSuccessMessage: String? = null
    override val showSuccessRes: Int? = null
    override val showInfoMessage: String? = null
    override val showInfoRes: Int? = null
}