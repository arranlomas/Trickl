package com.shwifty.tex.repository.searchhistory

import com.shwifty.tex.models.SearchHistoryItem

interface ISearchHistoryRepository {
    fun saveItem(item: SearchHistoryItem)

    fun getItems(): List<SearchHistoryItem>
}