package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.objects.KontentAction
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */
sealed class BrowseActions : KontentAction() {
    data class LoadBrowse(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : BrowseActions()
    data class UpdateSortAndCategory(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : BrowseActions()
    data class Search(val query: String) : BrowseActions()
    class ToggleSearchMode : BrowseActions()
    data class SetSearchBarExpanded(val expanded: Boolean) : BrowseActions()
    class ClearSearchResults : BrowseActions()
}
