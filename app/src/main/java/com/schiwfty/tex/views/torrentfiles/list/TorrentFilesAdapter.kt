package com.schiwfty.tex.views.torrentfiles.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.schiwfty.tex.R
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.utils.getFullPath
import com.schiwfty.tex.utils.onClick
import kotlinx.android.synthetic.main.list_item_torrent_file.view.*

/**
 * Created by arran on 19/04/2017.
 */
class TorrentFilesAdapter(val itemClickListener: (TorrentFile, ClickTypes) -> Unit) : RecyclerView.Adapter<TorrentFileCardHolder>() {
    var torrentFiles: List<TorrentFile> = mutableListOf()

    companion object{
        enum class ClickTypes{
            DOWNLOAD,
            PLAY
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentFileCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_torrent_file, parent, false)
        val holder = TorrentFileCardHolder(itemView)
        holder.onClick { view, position, type ->
            if(itemView.dropDownActionButtons.visibility == View.VISIBLE)
                itemView.dropDownActionButtons.visibility = View.GONE
            else
                itemView.dropDownActionButtons.visibility = View.VISIBLE
        }
        return holder
    }

    override fun onBindViewHolder(holder: TorrentFileCardHolder, position: Int) {
        holder.bind(torrentFiles[position])
        holder.itemView.playFileFab.setOnClickListener {itemClickListener(torrentFiles[position], ClickTypes.PLAY) }
        holder.itemView.downloadFileFab.setOnClickListener {itemClickListener(torrentFiles[position], ClickTypes.DOWNLOAD) }

    }

    override fun getItemCount(): Int {
        return torrentFiles.size
    }

    fun updatePercentages(updatedDetails: List<Triple<String, String, Int>>){
        torrentFiles.forEach { file ->
            updatedDetails.forEach {
                if(file.torrentHash == it.first && file.getFullPath() == it.second)
                file.percComplete = it.third
            }
        }
    }
}