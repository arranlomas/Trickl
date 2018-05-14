package com.shwifty.tex.repository.network.di

import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.repository.network.torrentSearch.TorrentSearchApi
import com.shwifty.tex.repository.network.torrentSearch.TorrentSearchRepository
import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.repository.preferences.PreferencesRepository
import com.shwifty.tex.repository.searchhistory.ISearchHistoryRepository
import com.shwifty.tex.repository.searchhistory.SearchHistoryRepository
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 27/10/2017.
 */
@Module
class RepositoryModule {
    @Provides
    internal fun provideTorrentSearchRepository(torrentSearchApi: TorrentSearchApi, searchHistoryRepository: ISearchHistoryRepository): ITorrentSearchRepository {
        return TorrentSearchRepository(torrentSearchApi, searchHistoryRepository)

    }

    @Provides
    internal fun providePreferencesRepository(): IPreferenceRepository {
        return PreferencesRepository()

    }

    @Provides
    fun provideTorrentRepository(): ITorrentRepository{
        return Confluence.torrentRepository
    }

    @Provides
    fun providesSearchHistoryRepository(): ISearchHistoryRepository {
        return SearchHistoryRepository()
    }
}