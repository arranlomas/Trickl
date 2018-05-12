package com.shwifty.tex.views.search.mvi

import com.arranlomas.kontent.commons.objects.KontentAction

/**
 * Created by arran on 14/02/2018.
 */
sealed class SearchActions : KontentAction() {
    class LoadSearchHistory : SearchActions()
    data class LoadMoreResults(val query: String, val page: Int) : SearchActions()
    data class Search(val query: String) : SearchActions()
    class ClearResults : SearchActions()
    data class Reload(val query: String) : SearchActions()
}