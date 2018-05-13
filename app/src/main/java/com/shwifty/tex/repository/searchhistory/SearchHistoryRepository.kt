package com.shwifty.tex.repository.searchhistory

import com.shwifty.tex.models.SearchHistoryItem
import io.reactivex.Observable

class SearchHistoryRepository : ISearchHistoryRepository {

    private val mockList = (0..10).map { SearchHistoryItem("$it", System.currentTimeMillis()) }.toMutableList()

    override fun saveItem(item: SearchHistoryItem) {
        mockList.add(item)
//        val realm = Realm.getDefaultInstance()
//        realm.executeTransaction {
//            realm.insertOrUpdate(item.mapToRealm())
//        }
    }

    override fun getItems(): Observable<List<SearchHistoryItem>> {
//        val realm = Realm.getDefaultInstance()
//        val torrentFileList = mutableListOf<SearchHistoryItem>()
//        try {
//            realm.beginTransaction()
//            val realmResult = realm.where(SearchHistoryRealm::class.java).findAll()
//            val copy = realm.copyFromRealm(realmResult)
//            realm.commitTransaction()
//            copy.forEach { torrentFileList.add(it.mapToModel()) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            realm.close()
//        }

//        return Observable.just(torrentFileList.toList().sortedBy { it.time })

        return Observable.just(mockList.toList())
    }
}