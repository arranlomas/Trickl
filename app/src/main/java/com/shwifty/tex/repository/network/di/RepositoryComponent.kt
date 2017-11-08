package com.shwifty.tex.repository.network.di

import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.repository.preferences.IPreferenceRepository
import dagger.Component
import javax.inject.Singleton

/**
 * Created by arran on 27/10/2017.
 */
@Singleton
@Component(modules = arrayOf(ApiModule::class, RepositoryModule::class))
interface RepositoryComponent {
    fun getTorrentSearchRepository(): ITorrentSearchRepository
    fun getPreferencesRepository(): IPreferenceRepository
}

