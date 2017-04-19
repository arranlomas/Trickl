package com.schiwfty.tex.views.all.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.schiwfty.tex.R
import com.schiwfty.tex.models.Torrent

/**
 * Created by arran on 19/04/2017.
 */
class AllAdapter : RecyclerView.Adapter<TorrentItemCardHolder>() {
    var torrents: MutableList<Torrent> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentItemCardHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_download, parent, false)
        return TorrentItemCardHolder(itemView)
    }

    override fun onBindViewHolder(holder: TorrentItemCardHolder, position: Int) {
        holder.bind(torrents[position])
    }

    override fun getItemCount(): Int {
        return torrents.size
    }
}