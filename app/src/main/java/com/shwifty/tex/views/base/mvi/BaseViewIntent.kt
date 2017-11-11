package com.shwifty.tex.views.base.mvi

/**
 * Created by arran on 8/11/2017.
 */
open class BaseViewIntent : BaseMviContract.Intent {
    data class ShowLoading(val loading: Boolean) : BaseViewIntent()
    data class ShowError(val message: String? = null, val stringId: Int? = null) : BaseViewIntent()
    data class ShowInfo(val message: String? = null, val stringId: Int? = null) : BaseViewIntent()
    data class ShowSuccess(val message: String? = null, val stringId: Int? = null) : BaseViewIntent()
}