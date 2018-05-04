package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.objects.KontentResult
import com.shwifty.tex.models.TorrentSearchResult

/**
 * Created by arran on 14/02/2018.
 */

sealed class SearchResult : KontentResult() {
    data class SearchSuccess(val result: List<TorrentSearchResult>, val query: String) : SearchResult()
    data class SearchError(val error: Throwable) : SearchResult()
    class SearchInFlight : SearchResult()
    class ClearResults : SearchResult()
}