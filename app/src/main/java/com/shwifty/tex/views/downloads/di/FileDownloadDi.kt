package com.shwifty.tex.views.downloads.di


import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.actions.IActionManager
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.views.downloads.mvp.FileDownloadContract
import com.shwifty.tex.views.downloads.mvp.FileDownloadFragment
import com.shwifty.tex.views.downloads.mvp.FileDownloadPresenter
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 15/02/2017.
 */
@Module
class FileDownloadModule {
    @Provides
    internal fun providesFileDownloadPresenter(torrentRepository: ITorrentRepository,
                                               actionManager: IActionManager,
                                               dialogManager: IDialogManager,
                                               navigation: INavigation,
                                               castHandler: ICastHandler): FileDownloadContract.Presenter {
        return FileDownloadPresenter(torrentRepository, actionManager, dialogManager, navigation, castHandler)
    }
}

@Module
abstract class FileFownloadFragmentBuilder {
    @ContributesAndroidInjector(modules = arrayOf(FileDownloadModule::class))
    internal abstract fun fileDownloadFragment(): FileDownloadFragment
}


