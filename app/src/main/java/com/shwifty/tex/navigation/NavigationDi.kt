package com.shwifty.tex.navigation

import dagger.Module
import dagger.Provides

/**
 * Created by arran on 4/03/2018.
 */
@Module
class NavigationModule {
    @Provides
    internal fun provideNavigation(): INavigation {
        return Navigation()
    }
}