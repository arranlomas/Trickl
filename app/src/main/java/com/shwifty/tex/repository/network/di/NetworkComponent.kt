package com.shwifty.tex.repository.network.di

import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import dagger.Component
import javax.inject.Singleton

/**
 * Created by arran on 27/10/2017.
 */
@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface NetworkComponent {
    fun getTorrentSearchRepository(): ITorrentSearchRepository
}

