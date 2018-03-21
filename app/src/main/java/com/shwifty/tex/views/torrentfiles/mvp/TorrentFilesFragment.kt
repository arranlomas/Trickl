package com.shwifty.tex.views.torrentfiles.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.R
import com.shwifty.tex.utils.ARG_TORRENT_HASH
import com.shwifty.tex.views.base.mvp.BaseDaggerFragment
import com.shwifty.tex.views.torrentfiles.list.TorrentFilesAdapter
import kotlinx.android.synthetic.main.frag_torrent_files.*
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesFragment : BaseDaggerFragment(), TorrentFilesContract.View {

    @Inject
    lateinit var presenter: TorrentFilesContract.Presenter

    val itemOnClick: (torrentFile: TorrentFile, type: TorrentFilesAdapter.Companion.ClickTypes) -> Unit = { torrentFile, type ->
        context?.let {
            presenter.viewClicked(it, torrentFile, type)
        }
    }
    val filesAdapter = TorrentFilesAdapter(itemOnClick)

    companion object {
        fun newInstance(torrentFilePath: String?): Fragment {
            val frag = TorrentFilesFragment()
            val args = Bundle()
            args.putString(ARG_TORRENT_HASH, torrentFilePath)
            frag.arguments = args
            return frag
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_torrent_files, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setup(arguments)
        presenter.attachView(this)
        torrentFilesRecyclerView.adapter = filesAdapter
        torrentFilesRecyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        torrentFilesRecyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        presenter.loadTorrent()
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
        activity?.finish()
    }
}