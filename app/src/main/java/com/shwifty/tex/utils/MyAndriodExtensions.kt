package com.shwifty.tex.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.crashlytics.android.Crashlytics
import com.shwifty.tex.R
import com.shwifty.tex.models.AppTheme
import com.shwifty.tex.repository.preferences.PreferencesRepository


/**
 * Created by arran on 28/10/2017.
 */

fun View.closeKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.openKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInputFromInputMethod(windowToken, 0)
}

fun Context.forceOpenKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
}

fun Context.forceCloseKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
}

fun SearchView.onSearchSubmitted(onSubmit: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            onSubmit.invoke(query)
            return true
        }
    })
}

fun View.setVisible(visible: Boolean) {
    if (visible) visibility = View.VISIBLE
    else visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return !(visibility == View.INVISIBLE || visibility == View.GONE)
}

fun Context.dpToPx(dp: Int): Int {
    val displayMetrics = resources.displayMetrics
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

fun Context.pxToDp(px: Int): Int {
    val displayMetrics = resources.displayMetrics
    return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

fun View.animateWidthChange(newWidth: Int, onAnimationFinished: (() -> Unit)? = null, duration: Long = 250) {
    val anim = ValueAnimator.ofInt(layoutParams.width, newWidth)
    anim.duration = duration
    anim.addUpdateListener { valueAnimator ->
        val `val` = valueAnimator.animatedValue as Int
        val newLayoutParams = layoutParams
        newLayoutParams.width = `val`
        layoutParams = newLayoutParams
    }

    anim.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {
        }

        override fun onAnimationEnd(p0: Animator?) {
            layoutParams.width = newWidth
            onAnimationFinished?.invoke()
        }

        override fun onAnimationCancel(p0: Animator?) {
        }

        override fun onAnimationStart(p0: Animator?) {
        }
    })
    anim.start()
}

fun EditText.afterTextChanged(afterTextChange: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChange.invoke(editable?.toString() ?: "")
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    })
}

fun Intent?.validateOnActivityResult(requestCode: Int, expectedRequestCode: Int, resultCode: Int, expectedResultCode: Int, onIsValid: (Bundle) -> Unit) {
    if (requestCode == expectedRequestCode) {
        if (resultCode == expectedResultCode) {
            this?.extras?.let {
                onIsValid.invoke(it)
            }
        }
    }
}

fun Activity.oncreateSetThemeAndCallSuper(onLoaded: () -> Unit) = PreferencesRepository().getThemPreference(this)
        .subscribe({
            when (it) {
                AppTheme.LIGHT -> setTheme(R.style.AppTheme_Light)
                AppTheme.DARK -> setTheme(R.style.AppTheme)
                else -> {
                }
            }
        }, {
            Crashlytics.logException(it)
        }, {
            onLoaded.invoke()
        })