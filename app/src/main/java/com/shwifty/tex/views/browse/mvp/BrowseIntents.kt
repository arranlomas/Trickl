package com.shwifty.tex.views.browse.mvp

import com.arranlomas.kontent.commons.objects.mvi.KontentIntent
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 11/11/2017.
 */
sealed class BrowseIntents : KontentIntent() {
    data class LoadIntent(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : BrowseIntents()
    data class UpdateSortAndCategoryIntent(val sortType: TorrentSearchSortType, val category: TorrentSearchCategory) : BrowseIntents()
    data class ReloadIntent(val isInSearchMode: Boolean, val query: String?, val sortType: TorrentSearchSortType?, val category: TorrentSearchCategory?) : BrowseIntents()
    data class SearchIntent(val query: String) : BrowseIntents()
    class ToggleSearchMode : BrowseIntents()
    data class SetSearchBarExpanded(val expanded: Boolean) : BrowseIntents()
}