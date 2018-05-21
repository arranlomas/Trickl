package com.shwifty.tex.views.browse.searchHistory

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.shwifty.tex.R
import com.shwifty.tex.models.SearchHistoryItem
import com.shwifty.tex.utils.onClick

/**
 * Created by arran on 19/04/2017.
 */
class SearchHistoryAdapter(private val itemClickListener: (SearchHistoryItem) -> Unit) : RecyclerView.Adapter<SearchHistoryCardHolder>() {
    private var searchHistoryList: MutableList<SearchHistoryItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_search_history, parent, false)
        val holder = SearchHistoryCardHolder(itemView)
        holder.onClick { _, position, _ ->
            itemClickListener.invoke(searchHistoryList[position])
        }
        return holder
    }

    override fun onBindViewHolder(holder: SearchHistoryCardHolder, position: Int) {
        holder.bind(searchHistoryList[position])
    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }

    fun setResults(torrentSearchResults: List<SearchHistoryItem>) {
        this.searchHistoryList = torrentSearchResults.toMutableList()
        notifyDataSetChanged()
    }
}