package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.objects.mvi.KontentResult
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */

sealed class BrowseResult : KontentResult() {
    data class BrowseSuccess(val result: List<TorrentSearchResult>) : BrowseResult()
    data class BrowseError(val error: Throwable) : BrowseResult()
    class BrowseInFlight : BrowseResult()

    data class SearchSuccess(val result: List<TorrentSearchResult>) : BrowseResult()
    data class SearchError(val error: Throwable) : BrowseResult()
    class SearchInFlight : BrowseResult()

    class ToggleSearchMode : BrowseResult()
    data class SetSearchBarExpanded(val expanded: Boolean) : BrowseResult()

    data class UpdateSortAndCategory(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : BrowseResult()
    class ClearSearchResults : BrowseResult()
}