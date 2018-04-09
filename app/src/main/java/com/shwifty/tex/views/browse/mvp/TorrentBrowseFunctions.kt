package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.functions.*
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Created by arran on 14/02/2018.
 */

fun browseActionProcessor(torrentSearchRepository: ITorrentSearchRepository) = KontentMasterProcessor<BrowseActions, BrowseResult> { action ->
    Observable.merge(observables(action, torrentSearchRepository))
}

private fun observables(shared: Observable<BrowseActions>, torrentSearchRepository: ITorrentSearchRepository): List<Observable<BrowseResult>> {
    return listOf<Observable<BrowseResult>>(
            shared.ofType(BrowseActions.InitialLoad::class.java).compose(initialLoad(torrentSearchRepository)),
            shared.ofType(BrowseActions.Reload::class.java).compose(reload(torrentSearchRepository)),
            shared.ofType(BrowseActions.Search::class.java).compose(loadSearchResults(torrentSearchRepository)),
            shared.ofType(BrowseActions.ToggleSearchMode::class.java).compose(toggleSearchProcessor()),
            shared.ofType(BrowseActions.SetSearchBarExpanded::class.java).compose(toggleSearchBarExpandedProcessor()),
            shared.ofType(BrowseActions.UpdateSortAndCategory::class.java).compose(updateSortAndCategoryProcessor()),
            shared.ofType(BrowseActions.ClearSearchResults::class.java).compose(clearSearchResultsProcessor()))
}

fun initialLoad(torrentSearchRepository: ITorrentSearchRepository) =
        KontentActionProcessor<BrowseActions.InitialLoad, BrowseResult, List<TorrentSearchResult>>(
                action = { action ->
                    torrentSearchRepository.browse(action.sortType, 0, action.category)
                },
                success = { results ->
                    BrowseResult.BrowseSuccess(results)
                },
                error = {
                    BrowseResult.BrowseError(it)
                },
                loading = BrowseResult.BrowseInFlight()
        )

fun reload(torrentSearchRepository: ITorrentSearchRepository) = ObservableTransformer<BrowseActions.Reload, BrowseResult> {
    it.flatMap { action ->
        if (action.isInSearchMode) torrentSearchRepository.search(action.query!!, action.sortType!!, 0, action.category!!)
                .networkMapper(
                        error = { BrowseResult.SearchError(it) },
                        loading = BrowseResult.SearchInFlight(),
                        success = { BrowseResult.SearchSuccess(it, action.query) }
                )
        else torrentSearchRepository.browse(action.sortType!!, 0, action.category!!)
                .networkMapper(
                        error = { BrowseResult.BrowseError(it) },
                        loading = BrowseResult.BrowseInFlight(),
                        success = { BrowseResult.BrowseSuccess(it) }
                )
    }
}


fun toggleSearchProcessor() = KontentSimpleActionProcessor<BrowseActions.ToggleSearchMode, BrowseResult> {
    Observable.just(BrowseResult.ToggleSearchMode())
}

fun toggleSearchBarExpandedProcessor() = KontentSimpleActionProcessor<BrowseActions.SetSearchBarExpanded, BrowseResult> {
    Observable.just(BrowseResult.SetSearchBarExpanded(it.expanded))
}

fun updateSortAndCategoryProcessor() = KontentSimpleActionProcessor<BrowseActions.UpdateSortAndCategory, BrowseResult> {
    Observable.just(BrowseResult.UpdateSortAndCategory(it.sortType, it.category))
}

fun clearSearchResultsProcessor() = KontentSimpleActionProcessor<BrowseActions.ClearSearchResults, BrowseResult> {
    Observable.just(BrowseResult.ClearSearchResults())
}

fun loadSearchResults(torrentSearchRepository: ITorrentSearchRepository) =
        KontentActionProcessor<BrowseActions.Search, BrowseResult, Pair<List<TorrentSearchResult>, String>>(
                action = { action ->
                    torrentSearchRepository.search(action.query, TorrentSearchSortType.DEFAULT, 0, TorrentSearchCategory.All)
                            .map { it to action.query }
                },
                success = { results ->
                    BrowseResult.SearchSuccess(results.first, results.second)
                },
                error = {
                    BrowseResult.SearchError(it)
                },
                loading = BrowseResult.SearchInFlight()
        )

val browseReducer = KontentReducer { result: BrowseResult, previousState: BrowseViewState ->
    when (result) {
        is BrowseResult.BrowseSuccess -> previousState.copy(isLoading = false, error = null, browseResults = result.result)
        is BrowseResult.BrowseError -> previousState.copy(isLoading = false, error = result.error)
        is BrowseResult.BrowseInFlight -> previousState.copy(isLoading = true, error = null)
        is BrowseResult.SearchSuccess -> previousState.copy(isLoading = false, searchResults = result.result, error = null, lastQuery = result.query)
        is BrowseResult.SearchError -> previousState.copy(isLoading = false, error = result.error)
        is BrowseResult.SearchInFlight -> previousState.copy(isLoading = true, error = null)
        is BrowseResult.ToggleSearchMode -> previousState.copy(isInSearchMode = !previousState.isInSearchMode)
        is BrowseResult.UpdateSortAndCategory -> previousState.copy(category = result.category, sortType = result.sortType)
        is BrowseResult.SetSearchBarExpanded -> previousState.copy(isSearchBarExpanded = result.expanded)
        is BrowseResult.ClearSearchResults -> previousState.copy(searchResults = emptyList())
    }
}

