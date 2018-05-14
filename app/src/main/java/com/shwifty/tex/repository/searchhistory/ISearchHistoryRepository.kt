package com.shwifty.tex.repository.searchhistory

import com.shwifty.tex.models.SearchHistoryItem
import io.reactivex.Observable

interface ISearchHistoryRepository {
    fun saveItem(item: String)

    fun getItems(): Observable<List<SearchHistoryItem>>
}