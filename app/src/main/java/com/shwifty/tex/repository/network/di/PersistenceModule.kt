package com.shwifty.tex.repository.network.di

import com.shwifty.tex.Database
import com.shwifty.tex.repository.searchhistory.SearchHistoryDao
import dagger.Module
import dagger.Provides

@Module
class PersistenceModule(private val db: Database) {
    @Provides
    internal fun provideSearchHistoryPersistence(): SearchHistoryDao {
        return db.searchHistoryDao()
    }
}