package com.shwifty.tex.views.torrentSearch.list

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.shwifty.tex.R
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.utils.onClick
import com.shwifty.tex.views.torrentfiles.list.TorrentFileCardHolder

/**
 * Created by arran on 19/04/2017.
 */
class TorrentSearchAdapter(val itemClickListener: (TorrentSearchResult, ClickTypes) -> Unit) : RecyclerView.Adapter<TorrentSearchCardHolder>() {
    var torrentSearchResults: List<TorrentSearchResult> = mutableListOf()

    companion object {
        enum class ClickTypes {
            DOWNLOAD,
            OPEN,
            CHROMECAST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentSearchCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_torrent_search_result, parent, false)
        val holder = TorrentSearchCardHolder(itemView)
        holder.onClick { _, position, _ ->
            val popup = PopupMenu(itemView.context, itemView)
            popup.menuInflater.inflate(R.menu.popup_view_file, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_open -> itemClickListener(torrentSearchResults[position], ClickTypes.OPEN)
                    R.id.menu_item_download -> itemClickListener(torrentSearchResults[position], ClickTypes.DOWNLOAD)
                    R.id.menu_item_chromecast -> itemClickListener(torrentSearchResults[position], ClickTypes.CHROMECAST)
                }
                true
            }
            popup.show()
        }
        return holder
    }

    override fun onBindViewHolder(holder: TorrentSearchCardHolder, position: Int) {
        holder.bind(torrentSearchResults[position])
    }

    override fun getItemCount(): Int {
        return torrentSearchResults.size
    }

    fun updateSearchResults(torrentSearchResults: List<TorrentSearchResult>) {
        this.torrentSearchResults = torrentSearchResults
    }
}