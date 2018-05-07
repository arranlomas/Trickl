package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.objects.KontentViewState
import com.shwifty.tex.Const
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 11/11/2017.
 */

data class BrowseViewState(
        val isLoading: Boolean = false,
        val error: Throwable? = null,
        val sortType: TorrentSearchSortType = Const.DEFAULT_SORT_TYPE,
        val category: TorrentSearchCategory = Const.DEFAULT_BROWSE_CATEGORY,
        val browseResults: List<TorrentSearchResult> = emptyList()
) : KontentViewState() {
    companion object Factory {
        fun default(): BrowseViewState {
            return BrowseViewState()
        }
    }
}