package com.shwifty.tex.repository.preferences.di

import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.repository.preferences.PreferencesRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by arran on 8/11/2017.
 */
@Singleton
@Component(modules = arrayOf(PreferencesModule::class))
interface PreferencesComponent {
    fun getPreferencesRepository(): IPreferenceRepository
}


@Module
class PreferencesModule {
    @Provides
    @Singleton
    internal fun providePreferencesRepository(): IPreferenceRepository {
        return PreferencesRepository()

    }
}