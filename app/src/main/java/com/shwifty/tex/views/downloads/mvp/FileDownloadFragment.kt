package com.shwifty.tex.views.downloads.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.R
import com.shwifty.tex.actions.IActionManager
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.views.base.mvp.BaseDaggerFragment
import com.shwifty.tex.views.downloads.list.FileDownloadAdapter
import com.shwifty.tex.navigation.Navigation
import kotlinx.android.synthetic.main.frag_file_downloads.*
import javax.inject.Inject


/**
 * Created by arran on 7/05/2017.
 */
class FileDownloadFragment : BaseDaggerFragment(), FileDownloadContract.View {

    @Inject
    lateinit var presenter: FileDownloadContract.Presenter

    val itemOnClick: (torrentFile: TorrentFile, type: FileDownloadAdapter.Companion.ClickTypes) -> Unit = { torrentFile, type ->
        context?.let {
            presenter.viewClicked(it, torrentFile, type)
        }
    }
    val filesAdapter = FileDownloadAdapter(itemOnClick)

    companion object {
        fun newInstance(): Fragment {
            return FileDownloadFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.refresh()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_file_downloads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)
        presenter.setup(arguments)
        super.onViewCreated(view, savedInstanceState)
        fileDownloadsSwipeRefresh.isRefreshing = true
        fileDownloadsRecyclerView.adapter = filesAdapter
        fileDownloadsRecyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        fileDownloadsRecyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        presenter.refresh()
        fileDownloadsSwipeRefresh.setOnRefreshListener { presenter.refresh() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun setupViewFromTorrentInfo(torrentFiles: List<TorrentFile>) {
        if (!isVisible || !isAdded) return
        fileDownloadsSwipeRefresh.isRefreshing = false
        filesAdapter.torrentFiles = torrentFiles
        filesAdapter.notifyDataSetChanged()
    }

    override fun setLoading(loading: Boolean) {
        fileDownloadsSwipeRefresh.isRefreshing = loading
    }
}