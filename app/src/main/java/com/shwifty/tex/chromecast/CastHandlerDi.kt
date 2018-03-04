package com.shwifty.tex.chromecast

import com.shwifty.tex.MyApplication
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 4/03/2018.
 */
@Module
class CastHandlerModule {
    @Provides
    internal fun provideCastHandler(): ICastHandler {
        return MyApplication.castHandler
    }
}