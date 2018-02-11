package com.shwifty.tex.views.downloads.list

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.shwifty.tex.R
import com.shwifty.tex.utils.onClick

/**
 * Created by arran on 19/04/2017.
 */
class FileDownloadAdapter(val itemClickListener: (TorrentFile, ClickTypes) -> Unit) : RecyclerView.Adapter<FileDownloadCardHolder>() {

    var torrentFiles: List<TorrentFile> = mutableListOf()

    companion object {
        enum class ClickTypes {
            OPEN,
            DOWNLOAD,
            CHROMECAST,
            DELETE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileDownloadCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_file_download, parent, false)
        val holder = FileDownloadCardHolder(itemView)
        holder.onClick { _, position, _ ->
            val popup = PopupMenu(itemView.context, itemView)
            popup.menuInflater.inflate(R.menu.popup_download_file, popup.menu)
            popup.setOnMenuItemClickListener {
                val clickType = when (it.itemId) {
                    R.id.menu_item_open -> ClickTypes.OPEN
                    R.id.menu_item_download -> ClickTypes.DOWNLOAD
                    R.id.menu_item_chromecast -> ClickTypes.CHROMECAST
                    R.id.menu_item_delete -> ClickTypes.DELETE
                    else -> null
                }
                val torrentFile = torrentFiles.getOrNull(position)
                if (clickType != null && torrentFile != null) {
                    itemClickListener(torrentFile, clickType)
                    true
                } else
                    false
            }
            popup.show()
        }
        return holder
    }

    override fun onBindViewHolder(holder: FileDownloadCardHolder, position: Int) {
        holder.bind(torrentFiles[position])

    }

    override fun getItemCount(): Int {
        return torrentFiles.size
    }
}