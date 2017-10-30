package com.shwifty.tex.utils

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * Created by arran on 28/10/2017.
 */
fun Fragment.closeKeyboard() {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Fragment.openKeyboard() {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInputFromInputMethod(view.windowToken, 0)
    }
}

fun Activity.closeKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.openKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInputFromInputMethod(view.windowToken, 0)
    }
}

fun SearchView.onSearchSubmitted(onSubmit: (String) -> Unit){
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

fun View.setVisible(visible: Boolean){
    if(visible) visibility = View.VISIBLE
    else visibility = View.GONE
}