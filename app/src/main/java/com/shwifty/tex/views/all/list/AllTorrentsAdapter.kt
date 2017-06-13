package com.schiwfty.tex.views.all.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.schiwfty.tex.R
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.utils.onClick

/**
 * Created by arran on 19/04/2017.
 */
class AllTorrentsAdapter(val itemClickListener: (View, Int, Int) -> Unit) : RecyclerView.Adapter<AllTorrentsCardHolder>() {
    var torrentFiles: List<TorrentInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTorrentsCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_torrent_all, parent, false)
        val holder = AllTorrentsCardHolder(itemView)
        holder.onClick (itemClickListener)
        return holder
    }

    override fun onBindViewHolder(holder: AllTorrentsCardHolder, position: Int) {
        holder.bind(torrentFiles[position])
    }

    override fun getItemCount(): Int {
        return torrentFiles.size
    }
}