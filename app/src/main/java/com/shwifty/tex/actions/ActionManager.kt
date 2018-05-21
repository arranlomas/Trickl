package com.shwifty.tex.actions

import android.content.Context
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.canCast
import com.schiwfty.torrentwrapper.utils.openFile
import com.shwifty.tex.R
import com.shwifty.tex.chromecast.ICastHandler
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.utils.CONNECTIVITY_STATUS
import com.shwifty.tex.utils.getConnectivityStatus

/**
 * Created by arran on 4/03/2018.
 */
class ActionManager(val torrentRepository: ITorrentRepository, val dialogManager: IDialogManager, val castHandler: ICastHandler) : IActionManager {
    override fun startDownload(context: Context, torrentFile: TorrentFile, forceDownloadEvenWithoutWifi: Boolean, onError: (String) -> Unit) {
        if (forceDownloadEvenWithoutWifi){
            torrentRepository.startFileDownloading(torrentFile, context, false)
            return
        }
        when (context.getConnectivityStatus()) {
            CONNECTIVITY_STATUS.WIFI -> {
                torrentRepository.startFileDownloading(torrentFile, context, true)
            }
            CONNECTIVITY_STATUS.MOBILE -> {
                dialogManager.showNoWifiDialog(context, torrentFile)
            }
            CONNECTIVITY_STATUS.NOT_CONNECTED -> {
                onError.invoke(context.getString(R.string.error_not_connected_to_wifi))
            }
        }
    }

    override fun startChromecast(context: Context, torrentFile: TorrentFile, onError: (String) -> Unit) {
        if (torrentFile.canCast()) {
            val casted = castHandler.loadRemoteMedia(torrentFile)
            if (!casted) onError.invoke(context.getString(R.string.chromecast_not_connect))
        } else {
            onError.invoke(context.getString(R.string.error_file_not_supported_by_chromecast))
        }
    }

    override fun openDeleteTorrentDialog(context: Context, torrentFile: TorrentFile) {
        dialogManager.showDeleteFileDialog(context, torrentFile)
    }

    override fun openTorrentFile(context: Context, torrentFile: TorrentFile, onError: (String) -> Unit) {
        torrentFile.openFile(context, torrentRepository, {
            onError.invoke(context.getString(R.string.error_no_activity))
        })
    }
}