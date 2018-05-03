package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.arranlomas.kontent.commons.functions.KontentSimpleActionProcessor
import com.arranlomas.kontent.commons.functions.networkMapper
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
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
        shared.ofType(SearchActions.LoadMoreResults::class.java).compose(loadMore(torrentSearchRepository)),
        shared.ofType(SearchActions.Reload::class.java).compose(reload(torrentSearchRepository)),
        shared.ofType(SearchActions.Search::class.java).compose(loadSearchResults(torrentSearchRepository)),
        shared.ofType(SearchActions.UpdateSortAndCategory::class.java).compose(updateSortAndCategoryProcessor()),
        shared.ofType(SearchActions.ClearResults::class.java).compose(clearResultsProcessor()))
}

fun loadMore(torrentSearchRepository: ITorrentSearchRepository) = ObservableTransformer<SearchActions.LoadMoreResults, SearchResult> {
    it.flatMap { action ->
        torrentSearchRepository.search(action.query!!, action.sortType!!, action.page, action.category!!)
            .networkMapper(
                error = { SearchResult.SearchError(it) },
                loading = SearchResult.SearchInFlight(),
                success = { SearchResult.SearchSuccess(it, action.query) }
            )
    }
}

fun reload(torrentSearchRepository: ITorrentSearchRepository) = ObservableTransformer<SearchActions.Reload, SearchResult> {
    it.flatMap { action ->
        torrentSearchRepository.search(action.query, action.sortType, 0, action.category)
            .networkMapper(
                error = { SearchResult.SearchError(it) },
                loading = SearchResult.SearchInFlight(),
                success = { SearchResult.SearchSuccess(it, action.query) }
            )
    }
}

fun updateSortAndCategoryProcessor() = KontentSimpleActionProcessor<SearchActions.UpdateSortAndCategory, SearchResult> {
    Observable.just(SearchResult.UpdateSortAndCategory(it.sortType, it.category))
}

fun clearResultsProcessor() = KontentSimpleActionProcessor<SearchActions.ClearResults, SearchResult> {
    Observable.just(SearchResult.ClearResults())
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
        is SearchResult.SearchSuccess -> previousState.copy(
            isLoading = false,
            searchResults = result.result,
            error = null,
            lastQuery = result.query
        )
        is SearchResult.SearchError -> previousState.copy(isLoading = false, error = result.error)
        is SearchResult.SearchInFlight -> previousState.copy(isLoading = true, error = null)
        is SearchResult.UpdateSortAndCategory -> previousState.copy(category = result.category, sortType = result.sortType)
        is SearchResult.ClearResults -> previousState.copy(searchResults = emptyList())
    }
}

