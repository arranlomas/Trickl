package com.shwifty.tex.views.torrentfiles.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.shwifty.tex.models.TorrentFile
import com.shwifty.tex.utils.formatBytesAsSize
import com.shwifty.tex.utils.getFullPath
import kotlinx.android.synthetic.main.list_item_torrent_file.view.*

/**
 * Created by arran on 19/04/2017.
 */
class TorrentFileCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var torrentFile: TorrentFile

    fun bind(torrentFile: TorrentFile) {
        this.torrentFile = torrentFile
        itemView.torrentFileName.text = torrentFile.getFullPath()
        itemView.torrentFileSize.text = torrentFile.fileLength?.formatBytesAsSize()
    }
}