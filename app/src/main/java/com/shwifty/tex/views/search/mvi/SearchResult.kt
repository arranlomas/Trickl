package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.objects.KontentResult
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */

sealed class SearchResult : KontentResult() {
    data class SearchSuccess(val result: List<TorrentSearchResult>, val query: String) : SearchResult()
    data class SearchError(val error: Throwable) : SearchResult()
    class SearchInFlight : SearchResult()

    data class UpdateSortAndCategory(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : SearchResult()
    class ClearResults : SearchResult()
}