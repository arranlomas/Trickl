package com.shwifty.tex.actions

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.dialogs.IDialogManager
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 4/03/2018.
 */
@Module
class ActionManagerModule {
    @Provides
    internal fun provideActionManager(dialogManager: IDialogManager, torrentRepository: ITorrentRepository, castHandler: ICastHandler): IActionManager {
        return ActionManager(torrentRepository, dialogManager, castHandler)
    }
}