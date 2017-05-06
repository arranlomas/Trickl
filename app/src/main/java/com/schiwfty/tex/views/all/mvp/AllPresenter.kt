package com.schiwfty.tex.views.all.mvp

import android.content.Context
import android.os.Environment
import com.schiwfty.tex.R
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.utils.composeIo
import com.schiwfty.tex.utils.getAsTorrent
import rx.Observable
import java.io.File
import javax.inject.Inject

/**
 * Created by arran on 16/04/2017.
 */
class AllPresenter : AllContract.Presenter {

    lateinit var view: AllContract.View
    lateinit var context: Context

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    override fun setup(context: Context, view: AllContract.View) {
        this.view = view
        this.context = context
        TricklComponent.networkComponent.inject(this)
    }

    override fun getTorrentInfo(hash: String) {
        torrentRepository.getStatus()
                .flatMap {
                    torrentRepository.getTorrentInfo(hash)
                }
                .subscribe ({
                    torrentRepository.getStatus()
                    //SUCCESS
                },{
                    //ERROR
                })
    }

    override fun updateStatus() {
        torrentRepository.getStatus()
                .subscribe({
                    view.updateStatus(it)
                },{
                    view.updateStatus(it.message ?: "ERROR")
                    it.printStackTrace()
                })
    }

    override fun testGetInfo(hash: String) {

    }

    private fun testDownload(hash: String){

    }

}