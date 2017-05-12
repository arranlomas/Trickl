package com.schiwfty.tex.views.all.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.schiwfty.tex.R
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.utils.formatBytesAsSize
import kotlinx.android.synthetic.main.list_item_torrent_all.view.*

/**
 * Created by arran on 19/04/2017.
 */
class AllTorrentsCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var torrentInfo: TorrentInfo

    fun bind( torrentInfo: TorrentInfo) {
        this.torrentInfo = torrentInfo
        itemView.torrentInfoName.text = torrentInfo.name
        itemView.torrentInfoSize.text = torrentInfo.totalSize.formatBytesAsSize()
        if(torrentInfo.singleFileTorrent)
            itemView.torrentInfoFileCount.text = itemView.context.getString(R.string.one_file)
            else
        itemView.torrentInfoFileCount.text = itemView.context.getString(R.string.x_files, torrentInfo.fileList.size)
    }
}