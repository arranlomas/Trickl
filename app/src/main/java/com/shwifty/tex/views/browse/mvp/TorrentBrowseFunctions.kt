package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.arranlomas.kontent.commons.functions.KontentSimpleActionProcessor
import com.arranlomas.kontent.commons.functions.networkMapper
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.repository.network.torrentSearch.BROWSE_FIRST_PAGE
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
        shared.ofType(BrowseActions.LoadMoreResults::class.java).compose(loadMore(torrentSearchRepository)),
        shared.ofType(BrowseActions.Reload::class.java).compose(reload(torrentSearchRepository)),
        shared.ofType(BrowseActions.UpdateSortAndCategory::class.java).compose(updateSortAndCategoryProcessor()),
        shared.ofType(BrowseActions.ClearResults::class.java).compose(clearResultsProcessor()))
}

fun initialLoad(torrentSearchRepository: ITorrentSearchRepository) =
    KontentActionProcessor<BrowseActions.InitialLoad, BrowseResult, List<TorrentSearchResult>>(
        action = { action ->
            torrentSearchRepository.browse(action.sortType, BROWSE_FIRST_PAGE, action.category)
        },
        success = { results ->
            BrowseResult.BrowseSuccess(results)
        },
        error = {
            BrowseResult.BrowseError(it)
        },
        loading = BrowseResult.BrowseInFlight()
    )

fun loadMore(torrentSearchRepository: ITorrentSearchRepository) = ObservableTransformer<BrowseActions.LoadMoreResults, BrowseResult> {
    it.flatMap { action ->
        torrentSearchRepository.browse(action.sortType, action.page, action.category)
            .networkMapper(
                error = { BrowseResult.BrowseError(it) },
                loading = BrowseResult.BrowseInFlight(),
                success = { BrowseResult.BrowseSuccess(it) }
            )
    }
}

fun reload(torrentSearchRepository: ITorrentSearchRepository) = ObservableTransformer<BrowseActions.Reload, BrowseResult> {
    it.flatMap { action ->
        torrentSearchRepository.browse(action.sortType!!, BROWSE_FIRST_PAGE, action.category!!)
            .networkMapper(
                error = { BrowseResult.BrowseError(it) },
                loading = BrowseResult.BrowseInFlight(),
                success = { BrowseResult.BrowseSuccess(it) }
            )
    }
}

fun updateSortAndCategoryProcessor() = KontentSimpleActionProcessor<BrowseActions.UpdateSortAndCategory, BrowseResult> {
    Observable.just(BrowseResult.UpdateSortAndCategory(it.sortType, it.category))
}

fun clearResultsProcessor() = KontentSimpleActionProcessor<BrowseActions.ClearResults, BrowseResult> {
    Observable.just(BrowseResult.ClearResults(it.isInSearchMode))
}

val browseReducer = KontentReducer { result: BrowseResult, previousState: BrowseViewState ->
    when (result) {
        is BrowseResult.BrowseSuccess -> {
            val results = previousState.browseResults.toMutableList()
            results.addAll(result.result)
            previousState.copy(
                isLoading = false,
                error = null,
                browseResults = results
            )
        }
        is BrowseResult.BrowseError -> previousState.copy(isLoading = false, error = result.error)
        is BrowseResult.BrowseInFlight -> previousState.copy(isLoading = true, error = null)
        is BrowseResult.UpdateSortAndCategory -> previousState.copy(category = result.category, sortType = result.sortType)
        is BrowseResult.ClearResults -> previousState.copy(browseResults = emptyList())
    }
}

