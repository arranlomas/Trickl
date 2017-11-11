package com.shwifty.tex.views.base.mvi

/**
 * Created by arran on 8/11/2017.
 */
open class BaseViewEvent : BaseMviContract.Event {
    data class ShowLoading(val loading: Boolean) : BaseViewEvent()
    data class ShowError(val message: String? = null, val stringId: Int? = null) : BaseViewEvent()
    data class ShowInfo(val message: String? = null, val stringId: Int? = null) : BaseViewEvent()
    data class ShowSuccess(val message: String? = null, val stringId: Int? = null) : BaseViewEvent()
}