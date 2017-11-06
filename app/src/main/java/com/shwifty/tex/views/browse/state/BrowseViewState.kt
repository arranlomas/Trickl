package com.shwifty.tex.views.browse.state

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import rx.subjects.PublishSubject

/**
 * Created by arran on 31/10/2017.
 */
data class BrowseViewState(
        var isInSearchMode: Boolean = false,
        var showingSearchBar: Boolean = false,
        var lastQuery: String? = null,
        var sortType: TorrentSearchSortType = TorrentSearchSortType.SEEDS,
        var category: TorrentSearchCategory = TorrentSearchCategory.Movies,
        var browseResults: List<TorrentSearchResult> = emptyList(),
        var searchResults: List<TorrentSearchResult> = emptyList()
)

class BrowseReducer {
    private var state = BrowseViewState()

    private val stateChangeSubject: PublishSubject<BrowseViewState> = PublishSubject.create()

    fun reduce(event: BrowseViewEvents){
        val newState = when(event){
            is BrowseViewEvents.UpdateSearchMode -> state.copy(isInSearchMode = event.isInSearchMode)
            is BrowseViewEvents.UpdateBrowseResults -> state.copy(browseResults = event.result)
            is BrowseViewEvents.UpdateSearchResults -> state.copy(searchResults = event.result)
            is BrowseViewEvents.UpdateFilter -> state.copy(sortType = event.newSortType, category = event.newCategory)
            is BrowseViewEvents.UpdateShowingSearchBar -> state.copy(showingSearchBar = event.showing)
            is BrowseViewEvents.UpdateLastQuery -> state.copy(lastQuery = event.query)
        }
        state = newState
        stateChangeSubject.onNext(state)
    }

    fun getState(): BrowseViewState{
        return state
    }

    fun getViewStateChangeStream(): PublishSubject<BrowseViewState>{
        return stateChangeSubject
    }
}

sealed class BrowseViewEvents{
    data class UpdateSearchMode(val isInSearchMode: Boolean): BrowseViewEvents()
    data class UpdateShowingSearchBar(val showing: Boolean): BrowseViewEvents()
    data class UpdateLastQuery(val query: String): BrowseViewEvents()
    data class UpdateFilter(val newSortType: TorrentSearchSortType, val newCategory: TorrentSearchCategory): BrowseViewEvents()
    data class UpdateBrowseResults(val result: List<TorrentSearchResult>): BrowseViewEvents()
    data class UpdateSearchResults(val result: List<TorrentSearchResult>): BrowseViewEvents()
}