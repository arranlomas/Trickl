package com.shwifty.tex.views.browse.searchHistory

import android.support.v7.widget.RecyclerView
import android.view.View
import com.shwifty.tex.models.SearchHistoryItem
import kotlinx.android.synthetic.main.list_item_search_history.view.*

/**
 * Created by arran on 19/04/2017.
 */
class SearchHistoryCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: SearchHistoryItem) {
        itemView.searchQuery.text = item.searchTerm
    }
}