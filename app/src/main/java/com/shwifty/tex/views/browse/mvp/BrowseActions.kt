package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.objects.KontentAction
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */
sealed class BrowseActions : KontentAction() {
    data class InitialLoad(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : BrowseActions()
    data class LoadMoreBrowseResults(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory, val page: Int) : BrowseActions()
    data class UpdateSortAndCategory(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : BrowseActions()
    data class Search(val query: String) : BrowseActions()
    class ToggleSearchMode : BrowseActions()
    data class SetSearchBarExpanded(val expanded: Boolean) : BrowseActions()
    class ClearSearchResults : BrowseActions()
    data class Reload(val isInSearchMode: Boolean, val query: String?, val sortType: TorrentSearchSortType?, val category: TorrentSearchCategory?) : BrowseActions()
}