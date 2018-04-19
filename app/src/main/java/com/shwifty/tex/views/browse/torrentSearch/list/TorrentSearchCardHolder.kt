package com.shwifty.tex.views.browse.torrentSearch.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.shwifty.tex.models.TorrentSearchResult
import kotlinx.android.synthetic.main.list_item_torrent_search_result.view.*

/**
 * Created by arran on 19/04/2017.
 */
class TorrentSearchCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var torrentSearchResult: TorrentSearchResult

    fun bind(torrentSearchResult: TorrentSearchResult) {
        this.torrentSearchResult = torrentSearchResult
        itemView.torrentName.text = torrentSearchResult.name
        itemView.uploader.text = torrentSearchResult.uled
        itemView.category.text = torrentSearchResult.category?.toHumanFriendlyString() ?: torrentSearchResult.categoryParent
        itemView.seeders.text = "Seeders: ${torrentSearchResult.seeds}"
        itemView.leechers.text = "Leechers: ${torrentSearchResult.leechers}"
        itemView.size.text = torrentSearchResult.size
        itemView.date.text = torrentSearchResult.uploaded
    }
}