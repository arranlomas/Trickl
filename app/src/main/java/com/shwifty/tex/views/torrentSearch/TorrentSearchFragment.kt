package com.shwifty.tex.views.torrentSearch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.views.base.BaseFragment
import com.shwifty.tex.views.torrentSearch.di.DaggerTorrentSearchComponent
import com.shwifty.tex.views.torrentSearch.list.TorrentSearchAdapter
import kotlinx.android.synthetic.main.frag_torrent_search.*
import javax.inject.Inject

/**
 * Created by arran on 27/10/2017.
 */
class TorrentSearchFragment : BaseFragment(), TorrentSearchContract.View {

    val itemOnClick: (searchResult: TorrentSearchResult, type: TorrentSearchAdapter.Companion.ClickTypes) -> Unit = { torrentSearchResult, actionType ->
        //        presenter.viewClicked(torrentFile, type)
    }
    val filesAdapter = TorrentSearchAdapter(itemOnClick)

    @Inject
    lateinit var presenter: TorrentSearchContract.Presenter

    companion object {
        fun newInstance(): Fragment {
            val frag = TorrentSearchFragment()
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerTorrentSearchComponent.builder().networkComponent(Trickl.networkComponent).build().inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (inflater == null) throw IllegalStateException("Torrent Details Fragment layout inflater is null!")
        val view = inflater.inflate(R.layout.frag_torrent_search, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = filesAdapter
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        presenter.search("hello")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showTorrents(searchResults: List<TorrentSearchResult>) {
        filesAdapter.updateSearchResults(searchResults)
        filesAdapter.notifyDataSetChanged()
    }
}