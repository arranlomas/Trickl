package com.schiwfty.tex.views.all.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.tex.R
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.views.addtorrent.AddTorrentActivity
import com.schiwfty.tex.views.all.list.AllTorrentsAdapter
import com.schiwfty.tex.views.showtorrent.ShowTorrentActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.frag_all.*

/**
 * Created by arran on 17/04/2017.
 */
class AllFragment : Fragment(), AllContract.View {

    lateinit var presenter: AllContract.Presenter
    val itemOnClick: (View, Int, Int) -> Unit = { _, position, _ ->
        val torrentFile = filesAdapter.torrentFiles[position]
        val intent= Intent(activity, ShowTorrentActivity::class.java)
        intent.putExtra(ShowTorrentActivity.ARG_TORRENT_HASH, torrentFile.info_hash)
        startActivity(intent)
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
    }

    override fun onResume() {
        super.onResume()
        presenter.getAllTorrentsInStorage()

    }

    override fun updateStatus(string: String) {
        if (!isAdded || !isVisible) return
    }

    override fun updateTorrentPercentage(percMap: HashMap<String, Float>) {
        if (!isAdded || !isVisible) return
        filesAdapter.torrentFiles.forEach {
            it.percComplete = percMap[it.info_hash] ?: 0f
        }
        filesAdapter.notifyDataSetChanged()
    }

    override fun showAllTorrents(torrentInfoList: List<TorrentInfo>) {
        if (!isAdded || !isVisible) return
        filesAdapter.torrentFiles = torrentInfoList
        filesAdapter.notifyDataSetChanged()
    }

    override fun showError(stringId: Int) {
        Toasty.error(activity, getString(stringId))
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(activity, getString(stringId))
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(activity, getString(stringId))
    }


}