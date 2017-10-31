package com.shwifty.tex.views.torrentSearch

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.utils.closeKeyboard
import com.shwifty.tex.utils.onSearchSubmitted
import com.shwifty.tex.utils.openKeyboard
import com.shwifty.tex.views.base.BaseFragment
import com.shwifty.tex.views.main.MainEventHandler
import com.shwifty.tex.views.torrentSearch.di.DaggerTorrentSearchComponent
import com.shwifty.tex.views.torrentSearch.list.TorrentSearchAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.frag_torrent_search.*
import javax.inject.Inject

/**
 * Created by arran on 27/10/2017.
 */
class TorrentSearchFragment : BaseFragment(), TorrentSearchContract.View {

    var initialQuery: String? = null

    val itemOnClick: (searchResult: TorrentSearchResult) -> Unit = { torrentSearchResult ->
        if (torrentSearchResult.magnet != null) {
            MainEventHandler.addMagnet(torrentSearchResult.magnet)
            activity.finish()
        } else {
            showError(R.string.error_cannot_open_torrent)
        }
    }
    val searchResultsAdapter = TorrentSearchAdapter(itemOnClick)

    var lastQuesry: String? = null

    @Inject
    lateinit var presenter: TorrentSearchContract.Presenter

    companion object {

        val ARG_INITIAL_QUERY = "arg_initial_query"

        fun newInstance(initialQuery: String? = null): Fragment {
            val frag = TorrentSearchFragment()
            val bundle = Bundle()
            bundle.putString(ARG_INITIAL_QUERY, initialQuery)
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        DaggerTorrentSearchComponent.builder().networkComponent(Trickl.networkComponent).build().inject(this)
        presenter.attachView(this)
        initialQuery = arguments.getString(ARG_INITIAL_QUERY)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (inflater == null) throw IllegalStateException("Torrent Details Fragment layout inflater is null!")
        val view = inflater.inflate(R.layout.frag_torrent_search, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = searchResultsAdapter
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        torrentSearchSwipeRefresh.setOnRefreshListener {
            lastQuesry?.let {
                searchResultsAdapter.torrentSearchResults = emptyList()
                searchResultsAdapter.notifyDataSetChanged()
                presenter.search(it)
            }
        }
        initialQuery?.let { presenter.search(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_fragment, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
        searchView.onSearchSubmitted {
            searchView.clearFocus()
            lastQuesry = it
            presenter.search(it)
            this.closeKeyboard()
        }
        super.onCreateOptionsMenu(menu, inflater)
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
        torrentSearchSwipeRefresh.isRefreshing = loading
    }

    override fun showError(msg: String) {
        Toasty.error(getActivityContext(), getString(R.string.error_search_server_unreachable), Toast.LENGTH_SHORT, true).show()
    }
}