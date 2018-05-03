package com.shwifty.tex.views.search.mvi

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.shwifty.tex.R
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.navigation.NavigationKey
import com.shwifty.tex.repository.network.torrentSearch.BROWSE_FIRST_PAGE
import com.shwifty.tex.utils.createObservable
import com.shwifty.tex.utils.setVisible
import com.shwifty.tex.views.EndlessScrollListener
import com.shwifty.tex.views.base.mvi.BaseDaggerMviFragment
import com.shwifty.tex.views.browse.torrentSearch.list.TorrentSearchAdapter
import es.dmoral.toasty.Toasty
import io.reactivex.Emitter
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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val loadMoreResultsSubject = PublishSubject.create<SearchActions.LoadMoreResults>()
    private val clearResultsSubject = PublishSubject.create<SearchActions.ClearResults>()

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
        super.attachActions(actions())
    }

    private fun setupRecyclerView() {
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm
        endlessScrollListener = object : EndlessScrollListener(llm, BROWSE_FIRST_PAGE) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadMoreResultsSubject.onNext(
                    SearchActions.LoadMoreResults(
                        viewModel.getLastState().lastQuery,
                        viewModel.getLastState().sortType,
                        viewModel.getLastState().category,
                        page
                    )
                )
            }
        }
        recyclerView.addOnScrollListener(endlessScrollListener)
        recyclerView.adapter = searchResultsAdapter
    }

    private fun actions() = Observable.merge(listOf(
        searchAction(),
        refreshIntent(),
        updateSortAndCategoryAction(),
        searchQueryEnterPressed(),
        loadMoreResultsSubject,
        clearResultsSubject))

    private fun searchAction(): Observable<SearchActions> = createObservable { emitter ->
        searchQueryInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                emitter.getSearchTextAndEmitAction()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun searchQueryEnterPressed(): Observable<SearchActions> = createObservable { emitter ->
        searchQueryInput.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                emitter.getSearchTextAndEmitAction()
            }
            false
        })
    }

    private fun Emitter<SearchActions>.getSearchTextAndEmitAction() {
        val text = searchQueryInput.text.toString()
        if (text.isNotEmpty()) {
            this.onNext(SearchActions.Search(text))
        }
    }

    private fun refreshIntent(): Observable<SearchActions.Reload> = RxSwipeRefreshLayout.refreshes(torrentSearchSwipeRefresh)
        .map {
            clearResultsSubject.onNext(SearchActions.ClearResults())
        }
        .map { getReloadIntent() }

    private fun updateSortAndCategoryAction(): Observable<SearchActions> = createObservable { emitter ->
        fabFilter.setOnClickListener {
            context?.let {
                dialogManager.showBrowseFilterDialog(it,
                    viewModel.getLastState().sortType,
                    viewModel.getLastState().category,
                    { sortType, category ->
                        emitter.onNext(SearchActions.UpdateSortAndCategory(sortType, category))
                        emitter.onNext(getReloadIntent())
                    })
            }
        }
    }

    private fun getReloadIntent(): SearchActions.Reload {
        return SearchActions.Reload(
            viewModel.getLastState().lastQuery,
            viewModel.getLastState().sortType,
            viewModel.getLastState().category)
    }

    override fun render(state: SearchViewState) {
        if (state.searchResults.isEmpty()) endlessScrollListener.resetState()

        searchResultsAdapter.setResults(state.searchResults)
        torrentSearchSwipeRefresh.isRefreshing = state.isLoading

        errorLayout.setVisible(state.error != null && !state.isLoading)
        state.error?.let {
            if (it is ConnectException) errorText.text = getString(R.string.error_connecting_to_search_server)
            else errorText.text = it.localizedMessage
        }
    }
}