package com.schiwfty.tex.views.downloads.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.schiwfty.tex.R
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.utils.onClick

/**
 * Created by arran on 19/04/2017.
 */
class FileDownloadAdapter(val itemClickListener: (TorrentFile, ClickTypes) -> Unit) : RecyclerView.Adapter<FileDownloadCardHolder>() {

    var torrentFiles: List<TorrentFile> = mutableListOf()

    companion object{
        enum class ClickTypes{
            OPEN,
            CHANGE_DOWNLOAD_STATE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDownloadCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_file_download, parent, false)
        val holder = FileDownloadCardHolder(itemView)
        holder.onClick { _, position, type ->
           itemClickListener(torrentFiles[position], ClickTypes.OPEN)
        }
        return holder
    }

    override fun onBindViewHolder(holder: FileDownloadCardHolder, position: Int) {
        holder.bind(torrentFiles[position], {file -> itemClickListener(file, ClickTypes.CHANGE_DOWNLOAD_STATE)})
    }

    override fun getItemCount(): Int {
        return torrentFiles.size
    }
}