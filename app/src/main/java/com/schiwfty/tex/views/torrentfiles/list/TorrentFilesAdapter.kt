package com.schiwfty.tex.views.torrentfiles.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.schiwfty.tex.R
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.utils.onClick

/**
 * Created by arran on 19/04/2017.
 */
class TorrentFilesAdapter(val itemClickListener: (View, Int, Int) -> Unit) : RecyclerView.Adapter<TorrentFileCardHolder>() {
    var torrentFiles: List<TorrentFile> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentFileCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_torrent_file, parent, false)
        val holder = TorrentFileCardHolder(itemView)
        holder.onClick (itemClickListener)
        return holder
    }

    override fun onBindViewHolder(holder: TorrentFileCardHolder, position: Int) {
        holder.bind(torrentFiles[position])
    }

    override fun getItemCount(): Int {
        return torrentFiles.size
    }
}