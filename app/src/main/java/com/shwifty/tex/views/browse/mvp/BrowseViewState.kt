package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.objects.KontentViewState
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.views.search.mvi.SearchViewState

/**
 * Created by arran on 11/11/2017.
 */

data class BrowseViewState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isInSearchMode: Boolean = false,
    val isSearchBarExpanded: Boolean = false,
    val lastQuery: String? = null,
    val sortType: TorrentSearchSortType = TorrentSearchSortType.SEEDS,
    val category: TorrentSearchCategory = TorrentSearchCategory.Movies,
    val browseResults: List<TorrentSearchResult> = emptyList(),
    val searchResults: List<TorrentSearchResult> = emptyList()
) : KontentViewState() {
    companion object Factory {
        fun default(): SearchViewState {
            return SearchViewState()
        }
    }
}