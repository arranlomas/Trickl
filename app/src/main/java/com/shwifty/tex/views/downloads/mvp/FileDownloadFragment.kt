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
import com.schiwfty.torrentwrapper.utils.openFile
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.base.BaseFragment
import com.shwifty.tex.views.downloads.list.FileDownloadAdapter
import com.shwifty.tex.views.main.MainEventHandler
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.frag_all.*
import kotlinx.android.synthetic.main.frag_file_downloads.*
import rx.subjects.PublishSubject


/**
 * Created by arran on 7/05/2017.
 */
class FileDownloadFragment : BaseFragment(), FileDownloadContract.View {

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
        presenter.attachView(this)
        presenter.setup(arguments)
    }

    override fun onResume() {
        super.onResume()
        presenter.refresh()
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

    override fun torrentFileClicked(action: FileDownloadAdapter.Companion.ClickTypes, torrentFile: TorrentFile, torrentRepository: ITorrentRepository) {
        when (action) {
            FileDownloadAdapter.Companion.ClickTypes.DOWNLOAD -> MainEventHandler.downloadTorrent(torrentFile)
            FileDownloadAdapter.Companion.ClickTypes.OPEN -> MainEventHandler.openTorrent(torrentFile)
            FileDownloadAdapter.Companion.ClickTypes.DELETE -> MainEventHandler.deleteTorrent(torrentFile)
            FileDownloadAdapter.Companion.ClickTypes.CHROMECAST -> MainEventHandler.chromecastTorrent(torrentFile)
        }
    }
}