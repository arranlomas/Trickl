package com.shwifty.tex.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.models.TorrentFile
import com.shwifty.tex.repositories.ITorrentRepository
import com.shwifty.tex.views.main.mvp.MainContract
import javax.inject.Inject


/**
 * Created by arran on 10/05/2017.
 */

class NotOnWifiDialog : DialogFragment() {
    private lateinit var filePath: String
    private lateinit var torrentHash: String
    private lateinit var torrentFile: TorrentFile

    @Inject
    lateinit var mainPresenter: MainContract.Presenter

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    companion object {
        val ARG_FILE_PATH = "arg_file_path"
        val ARG_TORRENT_HASH = "arg_torrent_hash"

        fun newInstance(hash: String, fileName: String): NotOnWifiDialog {
            val frag = NotOnWifiDialog()
            val bundle = Bundle()
            bundle.putString(ARG_TORRENT_HASH, hash)
            bundle.putString(ARG_FILE_PATH, fileName)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        TricklComponent.mainComponent.inject(this)
        filePath = arguments.getString(DeleteFileDialog.ARG_FILE_PATH)
        torrentHash = arguments.getString(DeleteFileDialog.ARG_TORRENT_HASH)

        torrentFile = torrentRepository.getTorrentFileFromPersistence(torrentHash, filePath)

        val b = AlertDialog.Builder(activity)
                .setTitle("Not connected to wifi")
                .setMessage("You are not currency connected to wifi, would you like to download the file anyway?")
                .setPositiveButton("Download",
                        { _, _ -> torrentRepository.startFileDownloading(torrentFile, activity, false) })
                .setNegativeButton("Cancel",
                        { dialog, _ -> dialog.dismiss() }
                )

        return b.create()
    }

}
