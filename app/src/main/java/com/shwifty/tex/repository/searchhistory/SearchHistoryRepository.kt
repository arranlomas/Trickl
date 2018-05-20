package com.shwifty.tex.repository.searchhistory

import com.shwifty.tex.models.SearchHistoryItem
import io.reactivex.Observable

class SearchHistoryRepository(
        private val searchHistoryDao: SearchHistoryDao
) : ISearchHistoryRepository {

    override fun saveItem(item: String) {
        searchHistoryDao.insert(SearchHistoryRoom(null, searchTerm = item, time = System.currentTimeMillis()))
    }

    override fun getItems(): Observable<List<SearchHistoryItem>> {
        return searchHistoryDao.getAll().map { it.map { it.mapToModel() } }.toObservable()
    }

    override fun deleteAll() {
        searchHistoryDao.deleteAll()
    }
}