package com.shwifty.tex.views.browse.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.utils.*
import com.shwifty.tex.views.base.mvp.BaseFragment
import com.shwifty.tex.views.browse.di.DaggerTorrentBrowseComponent
import com.shwifty.tex.views.browse.state.BrowseReducer
import com.shwifty.tex.views.browse.state.BrowseViewEvents
import com.shwifty.tex.views.browse.state.BrowseViewState
import com.shwifty.tex.views.browse.torrentSearch.list.TorrentSearchAdapter
import com.shwifty.tex.views.main.MainEventHandler
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.frag_torrent_browse.*
import javax.inject.Inject

/**
 * Created by arran on 27/10/2017.
 */
class TorrentBrowseFragment : BaseFragment(), TorrentBrowseContract.View {

    override val browseReducer = BrowseReducer()

    val itemOnClick: (searchResult: TorrentSearchResult) -> Unit = { torrentSearchResult ->
        if (torrentSearchResult.magnet != null) {
            MainEventHandler.addMagnet(torrentSearchResult.magnet)
        } else {
            showError(R.string.error_cannot_open_torrent)
        }
    }
    val searchResultsAdapter = TorrentSearchAdapter(itemOnClick)
    val browseResultsAdapter = TorrentSearchAdapter(itemOnClick)

    @Inject
    lateinit var presenter: TorrentBrowseContract.Presenter

    companion object {
        fun newInstance(): Fragment {
            val frag = TorrentBrowseFragment()
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerTorrentBrowseComponent.builder().repositoryComponent(Trickl.repositoryComponent).build().inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_torrent_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        torrentBrowseSwipeRefresh.setOnRefreshListener {
            reload()
        }
        fabFilter.setOnClickListener {
            context?.let {
                Trickl.dialogManager.showBrowseFilterDialog(it, browseReducer.getState().sortType, browseReducer.getState().category, { sortType, category ->
                    browseReducer.reduce(BrowseViewEvents.UpdateFilter(sortType, category))
                    reload()
                })
            }
        }
        fabSearch.setOnClickListener {
            if (browseReducer.getState().isInSearchMode) {
                browseReducer.reduce(BrowseViewEvents.UpdateSearchResults(emptyList()))
                browseReducer.reduce(BrowseViewEvents.UpdateShowingSearchBar(false))
                browseReducer.reduce(BrowseViewEvents.UpdateSearchMode(false))
            } else {
                browseReducer.reduce(BrowseViewEvents.UpdateShowingSearchBar(true))
                browseReducer.reduce(BrowseViewEvents.UpdateSearchMode(true))
            }
        }
        fabSendSearch.setOnClickListener {
            getQueryFromInputAndSearch()
        }
        searchQueryInput.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getQueryFromInputAndSearch()
            }
            false
        })
        render(browseReducer.getState())
        browseReducer.getViewStateChangeStream().subscribe { render(it) }
        reload()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun setLoading(loading: Boolean) {
        torrentBrowseSwipeRefresh.isRefreshing = loading
    }

    override fun showError(msg: String) {
        context?.let {
            Toasty.error(it, getString(R.string.error_search_server_unreachable), Toast.LENGTH_SHORT, true).show()
        }
    }

    private fun reload() {
        if (browseReducer.getState().isInSearchMode) {
            browseReducer.reduce(BrowseViewEvents.UpdateSearchResults(emptyList()))
            browseReducer.getState().lastQuery?.let { if (it.isNotEmpty()) presenter.search(it) }
        } else {
            browseReducer.reduce(BrowseViewEvents.UpdateBrowseResults(emptyList()))
            presenter.load(browseReducer.getState().sortType, browseReducer.getState().category)
        }
    }

    private fun expandQueryInput() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val fullWidthWithPadding = screenWidth - (context?.dpToPx(30) ?: 0)
        searchQueryInput.animateWidthChange(fullWidthWithPadding)
    }

    private fun collapseQueryInput() {
        searchQueryInput.setText("")
        context?.resources?.getDimensionPixelSize(R.dimen.fab_size_mini)?.let {
            searchQueryInput.animateWidthChange(it)
        }
    }

    private fun getQueryFromInputAndSearch() {
        val query = searchQueryInput.text.toString()
        if (query.isNotEmpty()) {
            presenter.search(query)
            browseReducer.reduce(BrowseViewEvents.UpdateLastQuery(query))
            browseReducer.reduce(BrowseViewEvents.UpdateShowingSearchBar(false))
        }
    }

    private fun render(viewState: BrowseViewState) {
        if (!isAdded || !isVisible) return
        if (viewState.showingSearchBar) {
            expandQueryInput()
            searchQueryInput.requestFocus()
            context?.forceOpenKeyboard()
            searchQueryInput.setVisible(true)
            fabSendSearch.show()
        } else {
            searchQueryInput.clearFocus()
            searchQueryInput.closeKeyboard()
            collapseQueryInput()
            searchQueryInput.setVisible(false)
            fabSendSearch.hide()
        }

        if (viewState.isInSearchMode) {
            context?.resources?.let {
                fabSearch.setImageDrawable(ResourcesCompat.getDrawable(it, R.drawable.ic_close_white, null))
                recyclerView.adapter = searchResultsAdapter
                fabFilter.hide()
            }

        } else {
            context?.resources?.let {
                fabSearch.setImageDrawable(ResourcesCompat.getDrawable(it, R.drawable.ic_search_white, null))
                recyclerView.adapter = browseResultsAdapter
                fabFilter.show()
            }
        }
        searchResultsAdapter.updateResults(viewState.searchResults)
        browseResultsAdapter.updateResults(viewState.browseResults)
        searchResultsAdapter.notifyDataSetChanged()
        browseResultsAdapter.notifyDataSetChanged()
    }

}