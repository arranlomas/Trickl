package com.shwifty.tex

import android.arch.lifecycle.ViewModelProvider
import com.arranlomas.daggerviewmodelhelper.ViewModelFactory
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by arran on 8/10/2017.
 */
@Singleton
@Component(modules = arrayOf(TricklModule::class))
interface TricklComponent {
    fun getTorrentrepository(): ITorrentRepository
}

@Module
class TricklModule(val torrentRepository: ITorrentRepository) {

    @Provides
    internal fun providesTorrentRepository(): ITorrentRepository {
        return torrentRepository
    }

}

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}


