package com.schiwfty.tex.views.main.mvp

import android.content.Context
import com.pawegio.kandroid.i
import com.pawegio.kandroid.v
import com.schiwfty.tex.R
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.utils.getFullPath
import java.io.FileNotFoundException
import javax.inject.Inject

/**
 * Created by arran on 16/04/2017.
 */
class MainPresenter : MainContract.Presenter {

    lateinit var view: MainContract.View
    @Inject
    lateinit var torrentRepository: ITorrentRepository

    override fun setup(context: Context, view: MainContract.View) {
        this.view = view
        TricklComponent.networkComponent.inject(this)
    }

    override fun getTorrentFileData(hash: String, path: String) {
        torrentRepository.getTorrentFileData(hash, path)
                .subscribe({

                }, {
                    view.showError(R.string.error_getting_torrent_file)
                })
    }

    override fun getAllTorrentData(hash: String) {
        var size = 0L

        torrentRepository.getTorrentInfo(hash)
                .map {
                    size = it?.totalSize ?: 0
                    it?.fileList?.get(3)?.getFullPath()
                }
                .flatMap {
                    if(it==null)throw FileNotFoundException()
                    torrentRepository.getTorrentFileData(hash, it)
                }
                .subscribe({
                    v { it.bytes().size.toString() }
                    v {size.toString()}
                }, {
                    it.printStackTrace()
                })
    }

}