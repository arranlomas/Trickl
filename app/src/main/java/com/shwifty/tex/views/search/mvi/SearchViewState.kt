package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.objects.KontentViewState
import com.shwifty.tex.models.SearchHistoryItem
import com.shwifty.tex.models.TorrentSearchResult

/**
 * Created by arran on 11/11/2017.
 */

data class SearchViewState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val lastQuery: String = "",
    val searchResults: List<TorrentSearchResult> = emptyList(),
    val searchHistoryItems: List<SearchHistoryItem> = emptyList()
) : KontentViewState() {
    companion object Factory {
        fun default(): SearchViewState {
            return SearchViewState()
        }
    }
}