package com.shwifty.tex.views.all.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.all.list.AllTorrentsAdapter
import com.shwifty.tex.views.main.mvp.MainContract
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.frag_all.*

/**
 * Created by arran on 17/04/2017.
 */
class AllFragment : Fragment(), AllContract.View {

    lateinit var mainPresenter: MainContract.Presenter

    lateinit var presenter: AllContract.Presenter


    val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
        val torrentFile = filesAdapter.torrentFiles[position]
        mainPresenter.showTorrentInfoActivity(torrentFile.info_hash)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPresenter = TricklComponent.mainComponent.getMainPresenter()
        Log.v("Arran", mainPresenter.toString())
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter = AllPresenter()
        presenter.setup(activity, this)
        return inflater?.inflate(R.layout.frag_all, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allTorrentsRecyclerView.adapter = filesAdapter
        allTorrentsRecyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        allTorrentsRecyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        allTorrentsSwipeRefresh.isRefreshing = true
        allTorrentsSwipeRefresh.setOnRefreshListener{presenter.refresh()}
    }

    override fun onResume() {
        super.onResume()
        presenter.refresh()
    }

    override fun updateStatus(string: String) {
        if (!isAdded || !isVisible) return
    }

    override fun showAllTorrents(torrentInfoList: List<TorrentInfo>) {
        if (!isAdded || !isVisible) return
        allTorrentsSwipeRefresh.isRefreshing = false
        filesAdapter.torrentFiles = torrentInfoList
        filesAdapter.notifyDataSetChanged()
    }

    override fun showError(stringId: Int) {
        Toasty.error(activity, getString(stringId)).show()
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(activity, getString(stringId)).show()
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(activity, getString(stringId)).show()
    }
}