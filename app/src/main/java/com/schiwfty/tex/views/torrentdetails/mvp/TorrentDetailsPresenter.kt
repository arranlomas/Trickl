package com.schiwfty.tex.views.torrentdetails.mvp

import android.content.Context

/**
 * Created by arran on 7/05/2017.
 */
class TorrentDetailsPresenter: TorrentDetailsContract.Presenter {
    lateinit var view: TorrentDetailsContract.View
    override fun setup(context: Context, view: TorrentDetailsContract.View) {
        this.view = view
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTorrent(torrentFilePath: String) {

//        torrentRepository.downloadTorrentInfo(hash)
//                .subscribe ({
//                },{
//                    view.showError(R.string.error_get_torrent_info)
//                },{
//                    view.showAddTorrentActivity(hash)
//                })
//        val torrentFile = File(torrentFilePath)
//        if(!torrentFile.exists() || !torrentFile.isFile) throw FileNotFoundException(" could not find torrent at path: $torrentFilePath")
//
//        val torrentInfo = torrentFile.getAsTorrent()


    }
}