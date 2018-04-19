package com.shwifty.tex.views.browse.mvp

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
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
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.shwifty.tex.R
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.navigation.NavigationKey
import com.shwifty.tex.repository.network.torrentSearch.BROWSE_FIRST_PAGE
import com.shwifty.tex.utils.animateWidthChange
import com.shwifty.tex.utils.closeKeyboard
import com.shwifty.tex.utils.createObservable
import com.shwifty.tex.utils.dpToPx
import com.shwifty.tex.utils.forceOpenKeyboard
import com.shwifty.tex.utils.setVisible
import com.shwifty.tex.views.EndlessScrollListener
import com.shwifty.tex.views.base.mvi.BaseDaggerMviFragment
import com.shwifty.tex.views.browse.torrentSearch.list.TorrentSearchAdapter
import es.dmoral.toasty.Toasty
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.frag_torrent_browse.*
import java.net.ConnectException
import javax.inject.Inject

/**
 * Created by arran on 27/10/2017.
 */
class TorrentBrowseFragment : BaseDaggerMviFragment<BrowseActions, BrowseResult, BrowseViewState>() {

    @Inject
    lateinit var navigation: INavigation

    @Inject
    lateinit var dialogManager: IDialogManager

    lateinit var endlessScrollListener: EndlessScrollListener

    private val itemOnClick: (searchResult: TorrentSearchResult) -> Unit = { torrentSearchResult ->
        context?.let {
            if (torrentSearchResult.magnet != null) navigation.goTo(NavigationKey.AddTorrent(context = it, magnet = torrentSearchResult.magnet))
            else errorText.text = it.getString(R.string.error_cannot_open_torrent) ?: ""
        }
    }
    private val searchResultsAdapter = TorrentSearchAdapter(itemOnClick)
    private val browseResultsAdapter = TorrentSearchAdapter(itemOnClick)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val loadMoreResultsSubject = PublishSubject.create<BrowseActions.LoadMoreResults>()
    private val clearResultsSubject = PublishSubject.create<BrowseActions.ClearResults>()

    companion object {
        fun newInstance(): Fragment {
            return TorrentBrowseFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_torrent_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TorrentBrowseViewModel::class.java)
        setupRecyclerView()
        super.setup(viewModel, { error ->
            context?.let {
                Toasty.error(it, error.localizedMessage).show()
            }
        })
        super.attachActions(actions(), BrowseActions.InitialLoad::class.java)
    }

    private fun setupRecyclerView() {
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        recyclerView.layoutManager = llm
        endlessScrollListener = object : EndlessScrollListener(llm, BROWSE_FIRST_PAGE) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadMoreResultsSubject.onNext(
                    BrowseActions.LoadMoreResults(
                        viewModel.getLastState().isInSearchMode,
                        viewModel.getLastState().lastQuery,
                        viewModel.getLastState().sortType,
                        viewModel.getLastState().category,
                        page
                    )
                )
            }
        }
        recyclerView.addOnScrollListener(endlessScrollListener)
        recyclerView.adapter = browseResultsAdapter
    }

    private fun actions() = Observable.merge(listOf(
        initialAction(),
        searchAction(),
        toggleSearchModeAction(),
        refreshIntent(),
        updateSortAndCategoryAction(),
        searchQueryEnterPressed(),
        loadMoreResultsSubject,
        clearResultsSubject))

    private fun initialAction(): Observable<BrowseActions.InitialLoad> = Observable.just(
        BrowseActions.InitialLoad(
            TorrentSearchSortType.SEEDS,
            TorrentSearchCategory.Movies
        ))

    private fun searchAction(): Observable<BrowseActions> = createObservable { emitter ->
        fabSendSearch.setOnClickListener {
            emitter.getSearchTextAndEmitAction()
        }
    }

    private fun searchQueryEnterPressed(): Observable<BrowseActions> = createObservable { emitter ->
        searchQueryInput.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                emitter.getSearchTextAndEmitAction()
            }
            false
        })
    }

    private fun Emitter<BrowseActions>.getSearchTextAndEmitAction() {
        val text = searchQueryInput.text.toString()
        if (text.isNotEmpty()) {
            this.onNext(BrowseActions.Search(text))
            this.onNext(BrowseActions.SetSearchBarExpanded(false))
        }
    }

    private fun toggleSearchModeAction(): Observable<BrowseActions> = createObservable { emitter ->
        fabSearch.setOnClickListener {
            if (viewModel.getLastState().isInSearchMode) {
                recyclerView.adapter = browseResultsAdapter
                clearResultsSubject.onNext(BrowseActions.ClearResults(viewModel.getLastState().isInSearchMode))
                emitter.onNext(BrowseActions.SetSearchBarExpanded(false))
            } else {
                recyclerView.adapter = searchResultsAdapter
                emitter.onNext(BrowseActions.SetSearchBarExpanded(true))
            }

            emitter.onNext(BrowseActions.ToggleSearchMode())
        }
    }

    private fun refreshIntent(): Observable<BrowseActions.Reload> = RxSwipeRefreshLayout.refreshes(torrentBrowseSwipeRefresh)
        .map {
            clearResultsSubject.onNext(BrowseActions.ClearResults(viewModel.getLastState().isInSearchMode))
        }
        .map { getReloadIntent() }

    private fun updateSortAndCategoryAction(): Observable<BrowseActions> = createObservable { emitter ->
        fabFilter.setOnClickListener {
            context?.let {
                dialogManager.showBrowseFilterDialog(it,
                    viewModel.getLastState().sortType,
                    viewModel.getLastState().category,
                    { sortType, category ->
                        emitter.onNext(BrowseActions.UpdateSortAndCategory(sortType, category))
                        emitter.onNext(getReloadIntent())
                    })
            }
        }
    }

    private fun getReloadIntent(): BrowseActions.Reload {
        return BrowseActions.Reload(viewModel.getLastState().isInSearchMode,
            viewModel.getLastState().lastQuery,
            viewModel.getLastState().sortType,
            viewModel.getLastState().category)
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

    override fun render(state: BrowseViewState) {
        if (state.isInSearchMode && recyclerView.adapter.itemCount == 0) endlessScrollListener.resetState()

        searchResultsAdapter.setResults(state.searchResults)
        browseResultsAdapter.setResults(state.browseResults)
        torrentBrowseSwipeRefresh.isRefreshing = state.isLoading

        errorLayout.setVisible(state.error != null && !state.isLoading)
        state.error?.let {
            if (it is ConnectException) errorText.text = getString(R.string.error_connecting_to_search_server)
            else errorText.text = it.localizedMessage
        }

        if (state.isSearchBarExpanded) {
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

        if (state.isInSearchMode) {
            context?.resources?.let {
                fabSearch.setImageDrawable(ResourcesCompat.getDrawable(it, R.drawable.ic_close_white, null))
                fabFilter.hide()
            }
        } else {
            context?.resources?.let {
                fabSearch.setImageDrawable(ResourcesCompat.getDrawable(it, R.drawable.ic_search_white, null))
                fabFilter.show()
            }
        }
    }
}