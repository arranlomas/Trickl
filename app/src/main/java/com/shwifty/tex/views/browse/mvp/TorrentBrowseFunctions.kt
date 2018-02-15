package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.arranlomas.kontent.commons.functions.KontentSimpleActionProcessor
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import io.reactivex.Observable

/**
 * Created by arran on 14/02/2018.
 */

fun browseIntentToAction(intent: BrowseIntents): BrowseActions = when (intent) {
    is BrowseIntents.SearchIntent -> BrowseActions.Search(intent.query)
    is BrowseIntents.LoadIntent -> BrowseActions.LoadBrowse(intent.sortType, intent.category)
    is BrowseIntents.ReloadIntent -> {
        if (intent.isInSearchMode && intent.query != null) BrowseActions.Search(intent.query)
        else if (intent.sortType != null && intent.category != null) BrowseActions.LoadBrowse(intent.sortType, intent.category)
        else BrowseActions.LoadBrowse(TorrentSearchSortType.SEEDS, TorrentSearchCategory.Movies)
    }
    is BrowseIntents.ToggleSearchMode -> BrowseActions.ToggleSearchMode()
    is BrowseIntents.UpdateSortAndCategoryIntent -> BrowseActions.UpdateSortAndCategory(intent.sortType, intent.category)
    is BrowseIntents.SetSearchBarExpanded -> BrowseActions.SetSearchBarExpanded(intent.expanded)
    is BrowseIntents.ClearSearchResultsIntent -> BrowseActions.ClearSearchResults()
}


fun browseActionProcessor(torrentSearchRepository: ITorrentSearchRepository) = KontentMasterProcessor<BrowseActions, BrowseResult> { action ->
    Observable.merge(observables(action, torrentSearchRepository))
}

private fun observables(shared: Observable<BrowseActions>, torrentSearchRepository: ITorrentSearchRepository): List<Observable<BrowseResult>> {
    return listOf<Observable<BrowseResult>>(
            shared.ofType(BrowseActions.LoadBrowse::class.java).compose(loadBrowseResults(torrentSearchRepository)),
            shared.ofType(BrowseActions.Search::class.java).compose(loadSearchResults(torrentSearchRepository)),
            shared.ofType(BrowseActions.ToggleSearchMode::class.java).compose(toggleSearchProcessor()),
            shared.ofType(BrowseActions.SetSearchBarExpanded::class.java).compose(toggleSearchBarExpandedProcessor()),
            shared.ofType(BrowseActions.UpdateSortAndCategory::class.java).compose(updateSortAndCategoryProcessor()),
            shared.ofType(BrowseActions.ClearSearchResults::class.java).compose(clearSearchResultsProcessor()))
}

fun loadBrowseResults(torrentSearchRepository: ITorrentSearchRepository) =
        KontentActionProcessor<BrowseActions.LoadBrowse, BrowseResult, List<TorrentSearchResult>>(
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
        KontentActionProcessor<BrowseActions.Search, BrowseResult, List<TorrentSearchResult>>(
                action = { action ->
                    torrentSearchRepository.search(action.query, TorrentSearchSortType.DEFAULT, 0, TorrentSearchCategory.All)
                },
                success = { results ->
                    BrowseResult.SearchSuccess(results)
                },
                error = {
                    BrowseResult.SearchError(it)
                },
                loading = BrowseResult.SearchInFlight()
        )

val browseReducer = KontentReducer { result: BrowseResult, previousState: BrowseViewState ->
    when (result) {
        is BrowseResult.BrowseSuccess -> previousState.copy(isLoading = false, error = null, browseResults = result.result)
        is BrowseResult.BrowseError -> previousState.copy(isLoading = false, error = result.error.localizedMessage)
        is BrowseResult.BrowseInFlight -> previousState.copy(isLoading = true, error = null)
        is BrowseResult.SearchSuccess -> previousState.copy(isLoading = false, searchResults = result.result, error = null)
        is BrowseResult.SearchError -> previousState.copy(isLoading = false, error = result.error.localizedMessage)
        is BrowseResult.SearchInFlight -> previousState.copy(isLoading = true, error = null)
        is BrowseResult.ToggleSearchMode -> previousState.copy(isInSearchMode = !previousState.isInSearchMode)
        is BrowseResult.UpdateSortAndCategory -> previousState.copy(category = result.category, sortType = result.sortType)
        is BrowseResult.SetSearchBarExpanded -> previousState.copy(isSearchBarExpanded = result.expanded)
        is BrowseResult.ClearSearchResults -> previousState.copy(searchResults = emptyList())
    }
}

