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
import com.shwifty.tex.views.all.list.AllTorrentsAdapter
import com.shwifty.tex.views.base.BaseFragment
import com.shwifty.tex.views.main.MainEventHandler
import kotlinx.android.synthetic.main.frag_all.*

/**
 * Created by arran on 17/04/2017.
 */
class AllFragment : BaseFragment(), AllContract.View {

    lateinit var presenter: AllContract.Presenter


    val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
        val torrentFile = filesAdapter.torrentFiles[position]
        MainEventHandler.showTorrentInfo(torrentFile)
    }
    val filesAdapter = AllTorrentsAdapter(itemOnClick)

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter = AllPresenter()

        return inflater?.inflate(R.layout.frag_all, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
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
}