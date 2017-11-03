package com.shwifty.tex.views.all.list

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.schiwfty.kotlinfilebrowser.onLongClick
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.R
import com.shwifty.tex.utils.onClick

/**
 * Created by arran on 19/04/2017.
 */
class AllTorrentsAdapter(val itemClickListener: (View, Int, Int) -> Unit, val onDeleteClick: (View, Int, Int) -> Unit) : RecyclerView.Adapter<AllTorrentsCardHolder>() {
    var torrentFiles: List<TorrentInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTorrentsCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_torrent_all, parent, false)
        val holder = AllTorrentsCardHolder(itemView)
        holder.onClick(itemClickListener)
        holder.onLongClick { view, position, type ->
            val popup = PopupMenu(itemView.context, itemView)
            popup.menuInflater.inflate(R.menu.popup_view_torrent, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_item_delete -> onDeleteClick(view, position, type)
                }
                true
            }
            popup.show()
        }
        return holder
    }

    override fun onBindViewHolder(holder: AllTorrentsCardHolder, position: Int) {
        holder.bind(torrentFiles[position])
    }

    override fun getItemCount(): Int {
        return torrentFiles.size
    }
}