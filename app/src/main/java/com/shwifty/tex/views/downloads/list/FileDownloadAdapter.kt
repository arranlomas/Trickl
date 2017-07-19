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

    companion object{
        enum class ClickTypes{
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
                when(it.itemId){
                    R.id.menu_item_open -> itemClickListener(torrentFiles[position], ClickTypes.OPEN)
                    R.id.menu_item_download -> itemClickListener(torrentFiles[position], ClickTypes.DOWNLOAD)
                    R.id.menu_item_chromecast -> itemClickListener(torrentFiles[position], ClickTypes.CHROMECAST)
                    R.id.menu_item_delete -> itemClickListener(torrentFiles[position], ClickTypes.DELETE)
                }
                true
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