package com.shwifty.tex.views.downloads.di


import com.schiwfty.torrentwrapper.dagger.network.TorrentRepositoryComponent
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.base.PresenterScope
import com.shwifty.tex.views.downloads.mvp.FileDownloadContract
import com.shwifty.tex.views.downloads.mvp.FileDownloadFragment
import com.shwifty.tex.views.downloads.mvp.FileDownloadPresenter
import dagger.Component
import dagger.Module
import dagger.Provides

/**
 * Created by arran on 15/02/2017.
 */
@PresenterScope
@Component(modules = arrayOf(FileDownloadModule::class), dependencies = arrayOf(TorrentRepositoryComponent::class))
interface FileDownloadComponent {
    fun inject(fileDownloadFragment: FileDownloadFragment)
}

@Module
class FileDownloadModule {
    @Provides
    internal fun providesFileDownloadPresenter(torrentRepository: ITorrentRepository): FileDownloadContract.Presenter {
        return FileDownloadPresenter(torrentRepository)
    }

}

