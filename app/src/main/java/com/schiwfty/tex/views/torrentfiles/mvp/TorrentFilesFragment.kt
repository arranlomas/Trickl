package com.schiwfty.tex.views.torrentfiles.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.tex.R
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.views.torrentfiles.list.TorrentFilesAdapter
import kotlinx.android.synthetic.main.frag_torrent_files.*


/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesFragment : Fragment(), TorrentFilesContract.View {

    lateinit var presenter: TorrentFilesContract.Presenter
    val itemOnClick: (torrentFile: TorrentFile, type: TorrentFilesAdapter.Companion.ClickTypes) -> Unit = { torrentFile, type ->
        presenter.viewClicked(torrentFile, type)
    }
    val filesAdapter = TorrentFilesAdapter(itemOnClick)

    companion object {
        val ARG_TORRENT_HASH = "arg_torrent_hash"

        fun newInstance(torrentFilePath: String?): Fragment {
            val frag = TorrentFilesFragment()
            val args = Bundle()
            args.putString(ARG_TORRENT_HASH, torrentFilePath)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = TorrentFilesPresenter()
        presenter.setup(activity, this, arguments)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (inflater == null) throw IllegalStateException("Torrent Files Fragment layout inflater is null!")
        val view = inflater.inflate(R.layout.frag_torrent_files, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        torrentFilesRecyclerView.adapter = filesAdapter
        torrentFilesRecyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        torrentFilesRecyclerView.layoutManager = llm as RecyclerView.LayoutManager?

        presenter.loadTorrent(presenter.torrentHash)
    }

    override fun setupViewFromTorrentInfo(torrentInfo: TorrentInfo) {
        filesAdapter.torrentFiles = torrentInfo.fileList
        filesAdapter.notifyDataSetChanged()
    }

    override fun updateTorrentPercentages(updatedDetails: List<TorrentFile>) {
        filesAdapter.updateTorrentFiles(updatedDetails)
        filesAdapter.notifyDataSetChanged()
    }

    override fun dismiss() {
        activity.finish()
    }
}