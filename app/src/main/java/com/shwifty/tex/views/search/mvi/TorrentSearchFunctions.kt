package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.arranlomas.kontent.commons.functions.KontentSimpleActionProcessor
import com.arranlomas.kontent.commons.functions.networkMapper
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.repository.network.torrentSearch.BROWSE_FIRST_PAGE
import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Created by arran on 14/02/2018.
 */

fun searchActionProcessor(torrentSearchRepository: ITorrentSearchRepository) = KontentMasterProcessor<SearchActions, SearchResult> { action ->
    Observable.merge(observables(action, torrentSearchRepository))
}

private fun observables(shared: Observable<SearchActions>, torrentSearchRepository: ITorrentSearchRepository): List<Observable<SearchResult>> {
    return listOf<Observable<SearchResult>>(
        shared.ofType(SearchActions.InitialLoad::class.java).compose(initialLoad(torrentSearchRepository)),
        shared.ofType(SearchActions.LoadMoreResults::class.java).compose(loadMore(torrentSearchRepository)),
        shared.ofType(SearchActions.Reload::class.java).compose(reload(torrentSearchRepository)),
        shared.ofType(SearchActions.Search::class.java).compose(loadSearchResults(torrentSearchRepository)),
        shared.ofType(SearchActions.ToggleSearchMode::class.java).compose(toggleSearchProcessor()),
        shared.ofType(SearchActions.SetSearchBarExpanded::class.java).compose(toggleSearchBarExpandedProcessor()),
        shared.ofType(SearchActions.UpdateSortAndCategory::class.java).compose(updateSortAndCategoryProcessor()),
        shared.ofType(SearchActions.ClearResults::class.java).compose(clearResultsProcessor()))
}

fun initialLoad(torrentSearchRepository: ITorrentSearchRepository) =
    KontentActionProcessor<SearchActions.InitialLoad, SearchResult, List<TorrentSearchResult>>(
        action = { action ->
            torrentSearchRepository.browse(action.sortType, BROWSE_FIRST_PAGE, action.category)
        },
        success = { results ->
            SearchResult.BrowseSuccess(results)
        },
        error = {
            SearchResult.BrowseError(it)
        },
        loading = SearchResult.BrowseInFlight()
    )


fun loadMore(torrentSearchRepository: ITorrentSearchRepository) = ObservableTransformer<SearchActions.LoadMoreResults, SearchResult> {
    it.flatMap { action ->
        if (action.isInSearchMode) torrentSearchRepository.search(action.query!!, action.sortType!!, action.page, action.category!!)
            .networkMapper(
                error = { SearchResult.SearchError(it) },
                loading = SearchResult.SearchInFlight(),
                success = { SearchResult.SearchSuccess(it, action.query) }
            )
        else torrentSearchRepository.browse(action.sortType!!, action.page, action.category!!)
            .networkMapper(
                error = { SearchResult.BrowseError(it) },
                loading = SearchResult.BrowseInFlight(),
                success = { SearchResult.BrowseSuccess(it) }
            )
    }
}


fun reload(torrentSearchRepository: ITorrentSearchRepository) = ObservableTransformer<SearchActions.Reload, SearchResult> {
    it.flatMap { action ->
        if (action.isInSearchMode) torrentSearchRepository.search(action.query!!, action.sortType!!, 0, action.category!!)
            .networkMapper(
                error = { SearchResult.SearchError(it) },
                loading = SearchResult.SearchInFlight(),
                success = { SearchResult.SearchSuccess(it, action.query) }
            )
        else torrentSearchRepository.browse(action.sortType!!, BROWSE_FIRST_PAGE, action.category!!)
            .networkMapper(
                error = { SearchResult.BrowseError(it) },
                loading = SearchResult.BrowseInFlight(),
                success = { SearchResult.BrowseSuccess(it) }
            )
    }
}

fun toggleSearchProcessor() = KontentSimpleActionProcessor<SearchActions.ToggleSearchMode, SearchResult> {
    Observable.just(SearchResult.ToggleSearchMode())
}

fun toggleSearchBarExpandedProcessor() = KontentSimpleActionProcessor<SearchActions.SetSearchBarExpanded, SearchResult> {
    Observable.just(SearchResult.SetSearchBarExpanded(it.expanded))
}

fun updateSortAndCategoryProcessor() = KontentSimpleActionProcessor<SearchActions.UpdateSortAndCategory, SearchResult> {
    Observable.just(SearchResult.UpdateSortAndCategory(it.sortType, it.category))
}

fun clearResultsProcessor() = KontentSimpleActionProcessor<SearchActions.ClearResults, SearchResult> {
    Observable.just(SearchResult.ClearResults(it.isInSearchMode))
}

fun loadSearchResults(torrentSearchRepository: ITorrentSearchRepository) =
    KontentActionProcessor<SearchActions.Search, SearchResult, Pair<List<TorrentSearchResult>, String>>(
        action = { action ->
            torrentSearchRepository.search(action.query, TorrentSearchSortType.DEFAULT, 0, TorrentSearchCategory.All)
                .map { it to action.query }
        },
        success = { results ->
            SearchResult.SearchSuccess(results.first, results.second)
        },
        error = {
            SearchResult.SearchError(it)
        },
        loading = SearchResult.SearchInFlight()
    )

val searchReducer = KontentReducer { result: SearchResult, previousState: SearchViewState ->
    when (result) {
        is SearchResult.BrowseSuccess -> {
            val results = previousState.browseResults.toMutableList()
            results.addAll(result.result)
            previousState.copy(
                isLoading = false,
                error = null,
                browseResults = results
            )
        }
        is SearchResult.BrowseError -> previousState.copy(isLoading = false, error = result.error)
        is SearchResult.BrowseInFlight -> previousState.copy(isLoading = true, error = null)
        is SearchResult.SearchSuccess -> previousState.copy(
            isLoading = false,
            searchResults = result.result,
            error = null,
            lastQuery = result.query
        )
        is SearchResult.SearchError -> previousState.copy(isLoading = false, error = result.error)
        is SearchResult.SearchInFlight -> previousState.copy(isLoading = true, error = null)
        is SearchResult.ToggleSearchMode -> previousState.copy(isInSearchMode = !previousState.isInSearchMode)
        is SearchResult.UpdateSortAndCategory -> previousState.copy(category = result.category, sortType = result.sortType)
        is SearchResult.SetSearchBarExpanded -> previousState.copy(isSearchBarExpanded = result.expanded)
        is SearchResult.ClearResults -> {
            if (result.isInSearchMode) previousState.copy(searchResults = emptyList())
            else previousState.copy(browseResults = emptyList())
        }
    }
}

