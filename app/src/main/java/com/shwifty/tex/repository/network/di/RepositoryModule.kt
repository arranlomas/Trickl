package com.shwifty.tex.repository.network.di

import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.repository.network.torrentSearch.TorrentSearchApi
import com.shwifty.tex.repository.network.torrentSearch.TorrentSearchRepository
import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.repository.preferences.PreferencesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by arran on 27/10/2017.
 */

@Module
class RepositoryModule {

    @Provides
    @Singleton
    internal fun provideTorrentRepository(torrentSearchApi: TorrentSearchApi): ITorrentSearchRepository {
        return TorrentSearchRepository(torrentSearchApi)

    }

    @Provides
    @Singleton
    internal fun providePreferencesRepository(): IPreferenceRepository {
        return PreferencesRepository()

    }
}