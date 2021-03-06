package com.shwifty.tex.views.downloads.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.utils.formatBytesAsSize
import com.schiwfty.torrentwrapper.utils.getFullPath
import com.shwifty.tex.R
import kotlinx.android.synthetic.main.list_item_file_download.view.*


/**
 * Created by arran on 19/04/2017.
 */
class FileDownloadCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var torrentFile: TorrentFile

    fun bind(torrentFile: TorrentFile) {
        this.torrentFile = torrentFile
        itemView.downloadFileName.text = "${itemView.context.getString(R.string.file_name)}: ${torrentFile.getFullPath()}"
        itemView.parentTorrentFileName.text = "${itemView.context.getString(R.string.torrent_name)}: ${torrentFile.parentTorrentName}"
        itemView.downloadFileSize.text = "${itemView.context.getString(R.string.size)}: ${torrentFile.fileLength?.formatBytesAsSize()}"
        itemView.downloadProgressBar.max = 100
        itemView.downloadProgressBar.progress = torrentFile.percComplete

    }
}