package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.objects.KontentAction
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */
sealed class SearchActions : KontentAction() {
    data class InitialLoad(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : SearchActions()
    data class LoadMoreResults(val isInSearchMode: Boolean, val query: String?, val sortType: TorrentSearchSortType?, val category: TorrentSearchCategory?, val page: Int) : SearchActions()
    data class UpdateSortAndCategory(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : SearchActions()
    data class Search(val query: String) : SearchActions()
    class ToggleSearchMode : SearchActions()
    data class SetSearchBarExpanded(val expanded: Boolean) : SearchActions()
    data class ClearResults(val isInSearchMode: Boolean) : SearchActions()
    data class Reload(val isInSearchMode: Boolean, val query: String?, val sortType: TorrentSearchSortType?, val category: TorrentSearchCategory?) : SearchActions()
}