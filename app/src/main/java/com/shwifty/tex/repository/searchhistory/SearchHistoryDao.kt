package com.shwifty.tex.repository.searchhistory

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Maybe

@Dao
interface SearchHistoryDao {

    @Query("SELECT * from searchHistoryData")
    fun getAll(): Maybe<List<SearchHistoryRoom>>

    @Insert(onConflict = REPLACE)
    fun insert(searchHistory: SearchHistoryRoom)

    @Query("DELETE from searchHistoryData")
    fun deleteAll()
}