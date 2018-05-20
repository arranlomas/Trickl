package com.shwifty.tex.views.search.mvi

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.jakewharton.rxbinding2.view.RxView
import com.shwifty.tex.R
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.navigation.NavigationKey
import com.shwifty.tex.repository.network.torrentSearch.BROWSE_FIRST_PAGE
import com.shwifty.tex.utils.setVisible
import com.shwifty.tex.views.EndlessScrollListener
import com.shwifty.tex.views.base.mvi.BaseDaggerMviFragment
import com.shwifty.tex.views.browse.searchHistory.SearchHistoryAdapter
import com.shwifty.tex.views.browse.torrentSearch.list.TorrentSearchAdapter
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.frag_torrent_search.*
import java.net.ConnectException
import javax.inject.Inject

/**
 * Created by arran on 27/10/2017.
 */
class TorrentSearchFragment : BaseDaggerMviFragment<SearchActions, SearchResult, SearchViewState>() {

    @Inject
    lateinit var navigation: INavigation

    @Inject
    lateinit var dialogManager: IDialogManager

    private lateinit var endlessScrollListener: EndlessScrollListener

    private val itemOnClick: (searchResult: TorrentSearchResult) -> Unit = { torrentSearchResult ->
        context?.let {
            if (torrentSearchResult.magnet != null) navigation.goTo(NavigationKey.AddTorrent(context = it, magnet = torrentSearchResult.magnet))
            else errorText.text = it.getString(R.string.error_cannot_open_torrent) ?: ""
        }
    }
    private val searchResultsAdapter = TorrentSearchAdapter(itemOnClick)
    private val searchHistoryAdapter = SearchHistoryAdapter({
        searchSubject.onNext(SearchActions.Search(it.searchTerm))
    })

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val loadMoreResultsSubject = PublishSubject.create<SearchActions.LoadMoreResults>()
    private val clearResultsSubject = PublishSubject.create<SearchActions.ClearResults>()
    private val searchSubject = PublishSubject.create<SearchActions.Search>()

    companion object {
        fun newInstance(): Fragment {
            return TorrentSearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_torrent_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TorrentSearchViewModel::class.java)
        setupRecyclerView()
        super.setup(viewModel, { error ->
            context?.let {
                Toasty.error(it, error.localizedMessage).show()
            }
        })
        super.attachActions(actions(), SearchActions.LoadSearchHistory::class.java)

        searchQueryInput.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSubject.onNext(SearchActions.Search(searchQueryInput.text.toString()))
            }
            false
        })
    }

    private fun setupRecyclerView() {
        recyclerView.setHasFixedSize(true)
        val searchResultsLLM = LinearLayoutManager(context)
        recyclerView.layoutManager = searchResultsLLM
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
        endlessScrollListener = object : EndlessScrollListener(searchResultsLLM, BROWSE_FIRST_PAGE) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadMoreResultsSubject.onNext(
                        SearchActions.LoadMoreResults(
                                viewModel.getLastState().lastQuery,
                                page
                        )
                )
            }
        }
        recyclerView.addOnScrollListener(endlessScrollListener)
        recyclerView.adapter = searchResultsAdapter
        searchHistoryRecyclerView.adapter = searchHistoryAdapter
    }

    private fun actions() = Observable.merge(listOf(
            loadSearchHistoryAction(),
            refreshIntent(),
            loadMoreResultsSubject,
            clearResultsSubject,
            clearTextAction(),
            clearSearchHistoryAction(),
            searchSubject))

    private fun clearSearchHistoryAction(): Observable<SearchActions> = RxView.clicks(clearHistoryBtn)
            .map { SearchActions.ClearSearchHistory() }

    private fun loadSearchHistoryAction(): Observable<SearchActions> = Observable.just(SearchActions.LoadSearchHistory())

    private fun clearTextAction(): Observable<SearchActions> = RxView.clicks(clearText)
            .map { searchQueryInput.setText("") }
            .map { SearchActions.ClearResults() }

    private fun refreshIntent(): Observable<SearchActions.Reload> = RxSwipeRefreshLayout.refreshes(torrentSearchSwipeRefresh)
            .map {
                clearResultsSubject.onNext(SearchActions.ClearResults())
            }
            .map { getReloadIntent() }

    private fun getReloadIntent(): SearchActions.Reload {
        return SearchActions.Reload(
                viewModel.getLastState().lastQuery)
    }

    override fun render(state: SearchViewState) {
        if (state.searchResults.isEmpty()) endlessScrollListener.resetState()

        clearHistoryBtn.setVisible(state.searchResults.isEmpty() && state.searchHistoryItems.isNotEmpty())
        searchHistoryRecyclerView.setVisible(state.searchResults.isEmpty())
        torrentSearchSwipeRefresh.setVisible(state.searchResults.isNotEmpty() || state.isLoading)

        searchResultsAdapter.setResults(state.searchResults)
        torrentSearchSwipeRefresh.isRefreshing = state.isLoading

        searchHistoryAdapter.setResults(state.searchHistoryItems)

        errorLayout.setVisible(state.error != null && !state.isLoading)
        state.error?.let {
            if (it is ConnectException) errorText.text = getString(R.string.error_connecting_to_search_server)
            else errorText.text = it.localizedMessage
        }
    }
}