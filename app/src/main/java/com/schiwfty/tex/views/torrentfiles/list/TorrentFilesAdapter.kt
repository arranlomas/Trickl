package com.schiwfty.tex.views.torrentfiles.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.schiwfty.tex.R
import com.schiwfty.tex.models.TorrentFile

/**
 * Created by arran on 19/04/2017.
 */
class TorrentFilesAdapter : RecyclerView.Adapter<TorrentFileCardHolder>() {
    var torrentFiles: List<TorrentFile> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentFileCardHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_torrent_file, parent, false)
        return TorrentFileCardHolder(itemView)
    }

    override fun onBindViewHolder(holder: TorrentFileCardHolder, position: Int) {
        holder.bind(torrentFiles[position])
    }

    override fun getItemCount(): Int {
        return torrentFiles.size
    }
}