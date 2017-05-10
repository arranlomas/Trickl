package com.schiwfty.tex.utils

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by arran on 10/05/2017.
 */
fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(it, adapterPosition, itemViewType)
    }
    return this
}