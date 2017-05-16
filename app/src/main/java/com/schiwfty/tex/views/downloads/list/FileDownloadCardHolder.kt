package com.schiwfty.tex.views.downloads.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.utils.formatBytesAsSize
import com.schiwfty.tex.utils.getFullPath
import kotlinx.android.synthetic.main.list_item_file_download.view.*

/**
 * Created by arran on 19/04/2017.
 */
class FileDownloadCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var torrentFile: TorrentFile

    fun bind(torrentFile: TorrentFile) {
        this.torrentFile = torrentFile
        itemView.downloadFileName.text = torrentFile.getFullPath()
        itemView.downloadFileSize.text = torrentFile.fileLength?.formatBytesAsSize()
        itemView.downloadProgressBar.max = 100
        itemView.downloadProgressBar.progress = torrentFile.percComplete
    }
}