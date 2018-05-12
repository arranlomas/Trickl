package com.shwifty.tex.repository.searchhistory

import com.shwifty.tex.models.SearchHistoryItem
import io.realm.Realm

class SearchHistoryRepository : ISearchHistoryRepository {

    override fun saveItem(item: SearchHistoryItem) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.insertOrUpdate(item.mapToRealm())
        }
    }

    override fun getItems(): List<SearchHistoryItem> {
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

        return torrentFileList.toList().sortedBy { it.time }
    }
}