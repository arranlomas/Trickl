package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.objects.KontentViewState
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 11/11/2017.
 */

data class SearchViewState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val lastQuery: String = "",
    val sortType: TorrentSearchSortType = TorrentSearchSortType.SEEDS,
    val category: TorrentSearchCategory = TorrentSearchCategory.Movies,
    val searchResults: List<TorrentSearchResult> = emptyList()
) : KontentViewState() {
    companion object Factory {
        fun default(): SearchViewState {
            return SearchViewState()
        }
    }
}