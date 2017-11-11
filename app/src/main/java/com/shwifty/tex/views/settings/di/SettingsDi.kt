package com.shwifty.tex.views.settings.di

import com.shwifty.tex.repository.network.di.RepositoryComponent
import com.shwifty.tex.repository.preferences.IPreferenceRepository
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.settings.mvi.SettingsReducer
import com.shwifty.tex.views.settings.mvp.SettingsActivity
import com.shwifty.tex.views.settings.mvp.SettingsContract
import com.shwifty.tex.views.settings.mvi.SettingsInteractor
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 28/10/2017.
 */
@PresenterScope
@Component(modules = arrayOf(SettingsModule::class), dependencies = arrayOf(RepositoryComponent::class))
interface SettingsComponent {
    fun inject(settingsActivity: SettingsActivity)
}

@Module
class SettingsModule {

    @Provides
    @PresenterScope
    internal fun providesSettingsPresenter(preferencesRepository: IPreferenceRepository): SettingsContract.Interactor {
        return SettingsInteractor(SettingsReducer(), preferencesRepository)
    }


}
