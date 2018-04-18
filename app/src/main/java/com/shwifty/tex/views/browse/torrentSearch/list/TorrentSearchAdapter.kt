package com.shwifty.tex.views.browse.torrentSearch.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.shwifty.tex.R
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.utils.onClick

/**
 * Created by arran on 19/04/2017.
 */
class TorrentSearchAdapter(val itemClickListener: (TorrentSearchResult) -> Unit) : RecyclerView.Adapter<TorrentSearchCardHolder>() {
    private var torrentSearchResults: MutableList<TorrentSearchResult> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentSearchCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_torrent_search_result, parent, false)
        val holder = TorrentSearchCardHolder(itemView)
        holder.onClick { _, position, _ ->
            itemClickListener.invoke(torrentSearchResults[position])
        }
        return holder
    }

    override fun onBindViewHolder(holder: TorrentSearchCardHolder, position: Int) {
        holder.bind(torrentSearchResults[position])
    }

    override fun getItemCount(): Int {
        return torrentSearchResults.size
    }

    fun setResults(torrentSearchResults: List<TorrentSearchResult>) {
        this.torrentSearchResults = torrentSearchResults.toMutableList()
        notifyDataSetChanged()
    }
}