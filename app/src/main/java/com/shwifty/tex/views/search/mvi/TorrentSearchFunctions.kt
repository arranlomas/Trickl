package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.arranlomas.kontent.commons.functions.KontentSimpleActionProcessor
import com.arranlomas.kontent.commons.functions.networkMapper
import com.shwifty.tex.Const
import com.shwifty.tex.models.SearchHistoryItem
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.repository.network.torrentSearch.BROWSE_FIRST_PAGE
import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.repository.searchhistory.ISearchHistoryRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Created by arran on 14/02/2018.
 */

fun searchActionProcessor(
    torrentSearchRepository: ITorrentSearchRepository,
    searchHistoryRepository: ISearchHistoryRepository
) = KontentMasterProcessor<SearchActions, SearchResult> { action ->
    Observable.merge(observables(action, torrentSearchRepository, searchHistoryRepository))
}

private fun observables(
    shared: Observable<SearchActions>,
    torrentSearchRepository: ITorrentSearchRepository,
    searchHistoryRepository: ISearchHistoryRepository
): List<Observable<SearchResult>> {
    return listOf<Observable<SearchResult>>(
        shared.ofType(SearchActions.LoadSearchHistory::class.java).compose(loadSearchHistory(searchHistoryRepository)),
        shared.ofType(SearchActions.LoadMoreResults::class.java).compose(loadMore(torrentSearchRepository)),
        shared.ofType(SearchActions.Reload::class.java).compose(reload(torrentSearchRepository)),
        shared.ofType(SearchActions.Search::class.java).compose(loadSearchResults(torrentSearchRepository, searchHistoryRepository)),
        shared.ofType(SearchActions.ClearResults::class.java).compose(clearResultsProcessor()))
}

fun loadSearchHistory(searchHistoryRepository: ISearchHistoryRepository) = ObservableTransformer<SearchActions.LoadSearchHistory, SearchResult> {
    it.flatMap { action ->
        searchHistoryRepository.getItems()
            .networkMapper(
                error = { SearchResult.SearchHistoryError(it) },
                loading = SearchResult.SearchHistoryInFlight(),
                success = { SearchResult.SearchHistorySuccess(it) }
            )
    }
}

fun loadMore(torrentSearchRepository: ITorrentSearchRepository) = ObservableTransformer<SearchActions.LoadMoreResults, SearchResult> {
    it.flatMap { action ->
        torrentSearchRepository.search(action.query, Const.DEFAULT_SORT_TYPE, action.page, Const.DEFAULT_SEARCH_CATEGORY)
            .networkMapper(
                error = { SearchResult.SearchError(it) },
                loading = SearchResult.SearchInFlight(),
                success = { SearchResult.SearchSuccess(it, action.query) }
            )
    }
}

fun reload(torrentSearchRepository: ITorrentSearchRepository) = ObservableTransformer<SearchActions.Reload, SearchResult> {
    it.flatMap { action ->
        torrentSearchRepository.search(action.query, Const.DEFAULT_SORT_TYPE, BROWSE_FIRST_PAGE, Const.DEFAULT_SEARCH_CATEGORY)
            .networkMapper(
                error = { SearchResult.SearchError(it) },
                loading = SearchResult.SearchInFlight(),
                success = { SearchResult.SearchSuccess(it, action.query) }
            )
    }
}

fun clearResultsProcessor() = KontentSimpleActionProcessor<SearchActions.ClearResults, SearchResult> {
    Observable.just(SearchResult.ClearResults())
}

fun loadSearchResults(torrentSearchRepository: ITorrentSearchRepository, searchHistoryRepository: ISearchHistoryRepository) =
    KontentActionProcessor<SearchActions.Search, SearchResult, Pair<List<TorrentSearchResult>, String>>(
        action = { action ->
            torrentSearchRepository.search(action.query, Const.DEFAULT_SORT_TYPE, BROWSE_FIRST_PAGE, Const.DEFAULT_SEARCH_CATEGORY)
                .doOnNext { searchHistoryRepository.saveItem(SearchHistoryItem(action.query)) }
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
        is SearchResult.SearchSuccess -> {
            val results = previousState.searchResults.toMutableList()
            results.addAll(result.result)
            previousState.copy(
                isLoading = false,
                error = null,
                searchResults = results,
                lastQuery = result.query
            )
        }
        is SearchResult.SearchError -> previousState.copy(isLoading = false, error = result.error)
        is SearchResult.SearchInFlight -> previousState.copy(isLoading = true, error = null)
        is SearchResult.ClearResults -> previousState.copy(searchResults = emptyList())
        is SearchResult.SearchHistoryInFlight -> previousState.copy(isLoading = true)
        is SearchResult.SearchHistorySuccess -> previousState.copy(isLoading = false, error = null, searchHistoryItems = result.result)
        is SearchResult.SearchHistoryError -> previousState.copy(isLoading = false, error = result.error)
    }
}

