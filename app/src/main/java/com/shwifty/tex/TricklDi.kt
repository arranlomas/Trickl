package com.shwifty.tex

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.repository.network.di.ApiModule
import com.shwifty.tex.repository.network.di.UnscopedRepositoryModule
import com.shwifty.tex.views.settings.di.SettingsActivityModule
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by arran on 8/10/2017.
 */
@Singleton
@Component(modules = arrayOf(TricklModule::class))
interface TricklComponent {
    fun getTorrentRepository(): ITorrentRepository
}

@Module
class TricklModule(val torrentRepository: ITorrentRepository) {

    @Provides
    internal fun providesTorrentRepository(): ITorrentRepository {
        return torrentRepository
    }

}
//
//@Singleton
//@Component(modules = arrayOf(
//        AndroidSupportInjectionModule::class,
//        UnscopedRepositoryModule::class,
//        ApiModule::class,
//        SettingsModule::class,
//        SettingsActivityModule::class))
//interface AppComponent : AndroidInjector<DaggerApplication> {
//    fun inject(app: MyApplication)
//}

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        SettingsActivityModule::class,
        ApiModule::class,
        UnscopedRepositoryModule::class))
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: MyApplication)
}

