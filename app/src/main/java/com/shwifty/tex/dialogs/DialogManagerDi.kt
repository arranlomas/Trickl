package com.shwifty.tex.dialogs

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.actions.IActionManager
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 4/03/2018.
 */
@Module
class DialogManagerModule {
    @Provides
    internal fun provideDialogManager(torrentRepository: ITorrentRepository): IDialogManager {
        return DialogManager(torrentRepository)
    }
}