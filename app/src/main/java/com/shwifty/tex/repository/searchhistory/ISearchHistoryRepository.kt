package com.shwifty.tex.repository.searchhistory

import com.shwifty.tex.models.SearchHistoryItem
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface ISearchHistoryRepository {
    fun saveItem(item: String)

    fun getItems(): Observable<List<SearchHistoryItem>>

    fun deleteAll()
}