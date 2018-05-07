package com.shwifty.tex.views.browse.mvp

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout
import com.shwifty.tex.Const
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
        refreshIntent(),
        updateSortAndCategoryAction(),
        loadMoreResultsSubject,
        clearResultsSubject))

    private fun initialAction(): Observable<BrowseActions.InitialLoad> = Observable.just(
        BrowseActions.InitialLoad(
            Const.DEFAULT_SORT_TYPE,
            Const.DEFAULT_BROWSE_CATEGORY
        ))

    private fun refreshIntent(): Observable<BrowseActions.Reload> = RxSwipeRefreshLayout.refreshes(torrentBrowseSwipeRefresh)
        .map {
            clearResultsSubject.onNext(BrowseActions.ClearResults())
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
        return BrowseActions.Reload(
            viewModel.getLastState().sortType,
            viewModel.getLastState().category)
    }

    override fun render(state: BrowseViewState) {
        if (state.browseResults.isEmpty()) endlessScrollListener.resetState()

        browseResultsAdapter.setResults(state.browseResults)
        torrentBrowseSwipeRefresh.isRefreshing = state.isLoading

        errorLayout.setVisible(state.error != null && !state.isLoading)
        state.error?.let {
            if (it is ConnectException) errorText.text = getString(R.string.error_connecting_to_search_server)
            else errorText.text = it.localizedMessage
        }
    }
}