package com.shwifty.tex.views.browse.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.views.base.BaseFragment
import com.shwifty.tex.views.browse.di.DaggerTorrentBrowseComponent
import com.shwifty.tex.views.main.MainEventHandler
import com.shwifty.tex.views.torrentSearch.list.TorrentSearchAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.frag_torrent_browse.*
import javax.inject.Inject

/**
 * Created by arran on 27/10/2017.
 */
class TorrentBrowseFragment : BaseFragment(), TorrentBrowseContract.View {

    val itemOnClick: (searchResult: TorrentSearchResult) -> Unit = { torrentSearchResult ->
        if (torrentSearchResult.magnet != null) {
            MainEventHandler.addMagnet(torrentSearchResult.magnet)
            activity.finish()
        } else {
            showError(R.string.error_cannot_open_torrent)
        }
    }
    val searchResultsAdapter = TorrentSearchAdapter(itemOnClick)

    @Inject
    lateinit var presenter: TorrentBrowseContract.Presenter

    var sortType: TorrentSearchSortType = TorrentSearchSortType.SEEDS
    var category: TorrentSearchCategory = TorrentSearchCategory.Movies

    companion object {
        fun newInstance(): Fragment {
            val frag = TorrentBrowseFragment()
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerTorrentBrowseComponent.builder().networkComponent(Trickl.networkComponent).build().inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (inflater == null) throw IllegalStateException("Torrent Details Fragment layout inflater is null!")
        val view = inflater.inflate(R.layout.frag_torrent_browse, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = searchResultsAdapter
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        torrentBrowseSwipeRefresh.setOnRefreshListener {
            reload()
        }
        reload()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun showTorrents(searchResults: List<TorrentSearchResult>) {
        searchResultsAdapter.updateSearchResults(searchResults)
        searchResultsAdapter.notifyDataSetChanged()
    }

    override fun setLoading(loading: Boolean) {
        torrentBrowseSwipeRefresh.isRefreshing = loading
    }

    override fun showError(msg: String) {
        Toasty.error(getActivityContext(), getString(R.string.error_search_server_unreachable), Toast.LENGTH_SHORT, true).show()
    }

    fun updateFilter(sortType: TorrentSearchSortType?, category: TorrentSearchCategory?){
        sortType?.let { this.sortType = sortType }
        category?.let { this.category = category }
        reload()
    }

    private fun reload(){
        searchResultsAdapter.torrentSearchResults = emptyList()
        searchResultsAdapter.notifyDataSetChanged()
        presenter.load(this.sortType, this.category)
    }
}