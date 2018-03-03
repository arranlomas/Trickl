package com.shwifty.tex.views.browse.mvp

import android.content.Context
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
import com.shwifty.tex.Trickl
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.utils.*
import com.shwifty.tex.views.base.mvi.BaseMviFragment
import com.shwifty.tex.views.browse.torrentSearch.list.TorrentSearchAdapter
import com.shwifty.tex.views.main.MainEventHandler
import dagger.android.support.AndroidSupportInjection
import es.dmoral.toasty.Toasty
import io.reactivex.Emitter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.frag_torrent_browse.*
import javax.inject.Inject

/**
 * Created by arran on 27/10/2017.
 */
class TorrentBrowseFragment : BaseMviFragment<BrowseIntents, BrowseViewState>() {

    val itemOnClick: (searchResult: TorrentSearchResult) -> Unit = { torrentSearchResult ->
        if (torrentSearchResult.magnet != null) MainEventHandler.addMagnet(torrentSearchResult.magnet)
        else errorText.text = getString(R.string.error_cannot_open_torrent)
    }
    val searchResultsAdapter = TorrentSearchAdapter(itemOnClick)
    val browseResultsAdapter = TorrentSearchAdapter(itemOnClick)

    @Inject
    lateinit var interactor: TorrentBrowseContract.Interactor

    companion object {
        fun newInstance(): Fragment {
            val frag = TorrentBrowseFragment()
            return frag
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_torrent_browse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        super.setup(interactor, { error ->
            context?.let {
                Toasty.error(it, error.localizedMessage).show()
            }
        })
        super.attachIntents(intents())
    }

    private fun setupRecyclerView() {
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context) as RecyclerView.LayoutManager
        recyclerView.layoutManager = llm
    }

    private fun intents() = Observable.merge(listOf(
            initialIntent(),
            searchIntent(),
            toggleSearchModeIntent(),
            refreshIntent(),
            updateSortAndCategoryIntent(),
            searchQueryEnterPressed()))

    private fun initialIntent(): Observable<BrowseIntents.ReloadIntent> = Observable.just(getReloadIntent())

    private fun searchIntent(): Observable<BrowseIntents> = createObservable { emitter ->
        fabSendSearch.setOnClickListener {
            emitter.getSearchTextAndEmitIntents()
        }
    }

    private fun searchQueryEnterPressed(): Observable<BrowseIntents> = createObservable { emitter ->
        searchQueryInput.setOnEditorActionListener({ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                emitter.getSearchTextAndEmitIntents()
            }
            false
        })
    }

    private fun Emitter<BrowseIntents>.getSearchTextAndEmitIntents() {
        val text = searchQueryInput.text.toString()
        if (text.isNotEmpty()) {
            this.onNext(BrowseIntents.SearchIntent(text))
            this.onNext(BrowseIntents.SetSearchBarExpanded(false))
        }
    }

    private fun toggleSearchModeIntent(): Observable<BrowseIntents> = createObservable { emitter ->
        fabSearch.setOnClickListener {
            if (interactor.getLastState()?.isInSearchMode == true) {
                emitter.onNext(BrowseIntents.ClearSearchResultsIntent())
                emitter.onNext(BrowseIntents.SetSearchBarExpanded(false))
            } else
                emitter.onNext(BrowseIntents.SetSearchBarExpanded(true))

            emitter.onNext(BrowseIntents.ToggleSearchMode())
        }
    }

    private fun refreshIntent(): Observable<BrowseIntents.ReloadIntent> = RxSwipeRefreshLayout.refreshes(torrentBrowseSwipeRefresh)
            .map { getReloadIntent() }

    private fun updateSortAndCategoryIntent(): Observable<BrowseIntents> = createObservable { emitter ->
        fabFilter.setOnClickListener {
            context?.let {
                Trickl.dialogManager.showBrowseFilterDialog(it,
                        interactor.getLastState()?.sortType ?: TorrentSearchSortType.SEEDS,
                        interactor.getLastState()?.category ?: TorrentSearchCategory.Movies,
                        { sortType, category ->
                            emitter.onNext(BrowseIntents.UpdateSortAndCategoryIntent(sortType, category))
                            emitter.onNext(getReloadIntent())
                        })
            }
        }
    }

    private fun getReloadIntent(): BrowseIntents.ReloadIntent {
        return BrowseIntents.ReloadIntent(interactor.getLastState()?.isInSearchMode ?: false,
                interactor.getLastState()?.lastQuery,
                interactor.getLastState()?.sortType,
                interactor.getLastState()?.category)
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
        if (!isAdded || !isVisible) return
        searchResultsAdapter.updateResults(state.searchResults)
        browseResultsAdapter.updateResults(state.browseResults)
        searchResultsAdapter.notifyDataSetChanged()
        browseResultsAdapter.notifyDataSetChanged()
        torrentBrowseSwipeRefresh.isRefreshing = state.isLoading

        errorLayout.setVisible(state.error != null && !state.isLoading)
        state.error?.let { errorText.text = it }


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
    }

}