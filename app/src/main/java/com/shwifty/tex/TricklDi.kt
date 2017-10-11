package com.shwifty.tex

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.chromecast.CastHandler
import com.shwifty.tex.chromecast.ICastHandler
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
class TricklModule {

    @Provides
    internal fun providesTorrentRepository(): ITorrentRepository {
        return Trickl.torrentRepository
    }

}
