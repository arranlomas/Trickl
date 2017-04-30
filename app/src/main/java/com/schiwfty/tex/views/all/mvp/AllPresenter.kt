package com.schiwfty.tex.views.all.mvp

import android.content.Context
import android.os.Environment
import com.schiwfty.tex.R
import com.schiwfty.tex.utils.composeIo
import com.schiwfty.tex.utils.getAsTorrent
import rx.Observable
import java.io.File

/**
 * Created by arran on 16/04/2017.
 */
class AllPresenter : AllContract.Presenter {

    lateinit var view: AllContract.View
    lateinit var context: Context

    override fun setup(context: Context, view: AllContract.View) {
        this.view = view
        this.context = context
    }

    override fun getTorrentInfo(hash: String) {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + "test.torrent")
        Observable.fromCallable { file.getAsTorrent() }
                .composeIo()
                .subscribe({
                    it?.name
                }, {
                    it.printStackTrace()
                    view.showError(R.string.get_torrent_error)
                })
    }
}