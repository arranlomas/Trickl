package com.schiwfty.tex.views.all.mvp

import android.content.Context
import com.schiwfty.tex.R
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.utils.composeIo
import com.schiwfty.tex.utils.getAsTorrent
import javax.inject.Inject

/**
 * Created by arran on 16/04/2017.
 */
class AllPresenter : AllContract.Presenter {

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    lateinit var view: AllContract.View
    lateinit var context: Context
    private var statusUpdateRunning = true

    private val statusThread = Thread({
        while (statusUpdateRunning){
            torrentRepository.getStatus()
                    .map {
                        val hashMap = HashMap<String, Float>()
                        it.torrentList.forEach { hashMap.put(it.infoHash, it.percComplete) }
                        hashMap
                    }
                    .subscribe({
                        view.updateTorrentPercentage(it)
                    },{/*swallow the error*/})
            Thread.sleep(1000)
        }
    })

    override fun setup(context: Context, view: AllContract.View) {
        this.view = view
        this.context = context
        TricklComponent.networkComponent.inject(this)
        statusThread.start()
    }

    override fun getAllTorrentsInStorageAndAddToClient() {
        torrentRepository.setupClientFromRepo()
                .flatMap { torrentRepository.getAllTorrentsFromStorage()}
                .subscribe ({
                    if(it!=null) {
                        view.showAllTorrents(it)
                        view.showInfo(R.string.splash_start_confluence_success)
                    }else
                        throw IllegalAccessException("Could not setup client")
                },{
                    it.printStackTrace()
                })
    }
}