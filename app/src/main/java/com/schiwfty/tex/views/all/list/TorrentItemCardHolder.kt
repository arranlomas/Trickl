package com.schiwfty.tex.views.all.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.schiwfty.tex.models.Torrent
import kotlinx.android.synthetic.main.list_item_download.view.*

/**
 * Created by arran on 19/04/2017.
 */
class TorrentItemCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var torrent: Torrent = Torrent("TEST")

    fun bind(torrent: Torrent) {
        this.torrent = torrent
        itemView.torrentName.text = "TEST"
    }
}