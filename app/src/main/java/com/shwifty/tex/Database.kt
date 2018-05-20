package com.shwifty.tex

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.shwifty.tex.repository.searchhistory.SearchHistoryDao
import com.shwifty.tex.repository.searchhistory.SearchHistoryRoom


@Database(entities = [(SearchHistoryRoom::class)], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}