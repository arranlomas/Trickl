package com.shwifty.tex.views.settings.di

import com.shwifty.tex.network.di.NetworkComponent
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.settings.mvp.SettingsActivity
import com.shwifty.tex.views.settings.mvp.SettingsContract
import com.shwifty.tex.views.settings.mvp.SettingsPresenter
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 28/10/2017.
 */
@PresenterScope
@Component(modules = arrayOf(SettingsModule::class), dependencies = arrayOf(NetworkComponent::class))
interface SettingsComponent {
    fun inject(settingsActivity: SettingsActivity)
}

@Module
class SettingsModule {

    @Provides
    @PresenterScope
    internal fun providesTorrentSearchPresenter(): SettingsContract.Presenter {
        return SettingsPresenter()
    }
}
