package com.schiwfty.tex.views.downloads.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.tex.R
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.views.downloads.list.FileDownloadAdapter
import kotlinx.android.synthetic.main.frag_file_downloads.*


/**
 * Created by arran on 7/05/2017.
 */
class FileDownloadFragment : Fragment(), FileDownloadContract.View {

    lateinit var presenter: FileDownloadContract.Presenter
    val itemOnClick: (torrentFile: TorrentFile, type: FileDownloadAdapter.Companion.ClickTypes) -> Unit = { torrentFile, type ->
        presenter.viewClicked(torrentFile, type)
    }
    val filesAdapter = FileDownloadAdapter(itemOnClick)

    companion object {
        fun newInstance(): Fragment {
            val frag = FileDownloadFragment()
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = FileDownloadPresenter()
        presenter.setup(activity, this, arguments)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (inflater == null) throw IllegalStateException("Torrent Files Fragment layout inflater is null!")
        val view = inflater.inflate(R.layout.frag_file_downloads, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fileDownloadsSwipeRefresh.isRefreshing = true
        fileDownloadsRecyclerView.adapter = filesAdapter
        fileDownloadsRecyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        fileDownloadsRecyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        presenter.getDownloadingTorrents()
        fileDownloadsSwipeRefresh.setOnRefreshListener { presenter.getDownloadingTorrents() }
    }

    override fun setupViewFromTorrentInfo(torrentFiles: List<TorrentFile>) {
        fileDownloadsSwipeRefresh.isRefreshing = false
        filesAdapter.torrentFiles = torrentFiles
        filesAdapter.notifyDataSetChanged()
    }

}