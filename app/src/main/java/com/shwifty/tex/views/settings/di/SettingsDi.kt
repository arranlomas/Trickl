package com.shwifty.tex.views.settings.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.arranlomas.daggerviewmodelhelper.ViewModelFactory
import com.arranlomas.daggerviewmodelhelper.ViewModelKey
import com.shwifty.tex.views.settings.mvi.SettingsActivity
import com.shwifty.tex.views.settings.mvi.SettingsInteractor
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by arran on 28/10/2017.
 */
//@PresenterScope
//@Component(modules = arrayOf(SettingsModule::class), dependencies = arrayOf(RepositoryComponent::class))
//interface SettingsComponent {
//    fun inject(settingsActivity: SettingsActivity)
//}


//@Module
//class SettingsModule {
//
//    @Provides
//    @PresenterScope
//    internal fun providesSettingsPresenter(preferencesRepository: IPreferenceRepository): SettingsContract.Interactor {
//        return SettingsInteractor(preferencesRepository)
//    }
//
//
//}


@Module
abstract class SettingsActivityModule {
    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): SettingsActivity

    @Binds
    @IntoMap
    @ViewModelKey(SettingsInteractor::class)
    abstract fun bindMainViewModel(mainViewModel: SettingsInteractor): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

