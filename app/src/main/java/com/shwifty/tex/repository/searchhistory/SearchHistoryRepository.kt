package com.shwifty.tex.repository.searchhistory

import com.shwifty.tex.models.SearchHistoryItem
import io.reactivex.Observable
import io.realm.Realm

class SearchHistoryRepository : ISearchHistoryRepository {

    override fun saveItem(item: String) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.insertOrUpdate(SearchHistoryItem(item, System.currentTimeMillis()).mapToRealm())
        }
    }

    override fun getItems(): Observable<List<SearchHistoryItem>> {
        val realm = Realm.getDefaultInstance()
        val torrentFileList = mutableListOf<SearchHistoryItem>()
        try {
            realm.beginTransaction()
            val realmResult = realm.where(SearchHistoryRealm::class.java).findAll()
            val copy = realm.copyFromRealm(realmResult)
            realm.commitTransaction()
            copy.forEach { torrentFileList.add(it.mapToModel()) }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            realm.close()
        }

        return Observable.just(torrentFileList.toList().sortedBy { it.time })
    }
}