package com.shwifty.tex.views.all.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.R
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.views.all.list.AllTorrentsAdapter
import com.shwifty.tex.views.base.mvp.BaseDaggerFragment
import com.shwifty.tex.navigation.NavigationKey
import kotlinx.android.synthetic.main.frag_all.*
import javax.inject.Inject

/**
 * Created by arran on 17/04/2017.
 */
class AllFragment : BaseDaggerFragment(), AllContract.View {

    @Inject
    lateinit var presenter: AllContract.Presenter

    @Inject
    lateinit var navigation: INavigation

    @Inject
    lateinit var dialogManager: IDialogManager

    val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
        val torrentFile = filesAdapter.torrentFiles[position]
        context?.let {
            navigation.goTo(NavigationKey.OpenTorrentInfo(it, torrentFile.info_hash))
        }
    }

    val onDeleteItem: (View, Int, Int) -> Unit = { _, position, _ ->
        val torrentFile = filesAdapter.torrentFiles[position]
        context?.let {
            dialogManager.showDeleteTorrentDialog(it, torrentFile, {
                showError(R.string.error_deleting_torrent)
            })
        }
    }

    val filesAdapter = AllTorrentsAdapter(itemOnClick, onDeleteItem)

    companion object {
        fun newInstance(): Fragment {
            val allFragment = AllFragment()
            val args = Bundle()
            allFragment.arguments = args
            return allFragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        presenter.refresh()
        allTorrentsRecyclerView.adapter = filesAdapter
        allTorrentsRecyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        allTorrentsRecyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        allTorrentsSwipeRefresh.setOnRefreshListener { presenter.refresh() }
    }

    override fun onResume() {
        super.onResume()
        presenter.refresh()
    }

    override fun showAllTorrents(torrentInfoList: List<TorrentInfo>) {
        if (!isAdded || !isVisible) return
        filesAdapter.torrentFiles = torrentInfoList
        filesAdapter.notifyDataSetChanged()
    }

    override fun setLoading(loading: Boolean) {
        allTorrentsSwipeRefresh.isRefreshing = loading
    }

    override fun showSomeTorrentsCouldNotBeLoaded(torrentCount: Int) {
        showError(getString(R.string.error_all_torrents_some_could_not_be_parsed, torrentCount))
    }

}