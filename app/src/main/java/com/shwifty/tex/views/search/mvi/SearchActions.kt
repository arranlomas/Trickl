package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.objects.KontentAction
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */
sealed class SearchActions : KontentAction() {
    data class LoadMoreResults(val query: String, val sortType: TorrentSearchSortType, val category: TorrentSearchCategory, val page: Int) : SearchActions()
    data class UpdateSortAndCategory(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : SearchActions()
    data class Search(val query: String) : SearchActions()
    class ClearResults : SearchActions()
    data class Reload(val query: String, val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : SearchActions()
}