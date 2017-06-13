package com.shwifty.tex.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.repositories.ITorrentRepository
import javax.inject.Inject

/**
 * Created by arran on 25/05/2017.
 */
class DeleteFileDialog : DialogFragment() {
    private lateinit var torrentName: String
    private lateinit var filePath: String
    private lateinit var torrentHash: String

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    companion object {
        val ARG_TORRENT_HASH = "arg_torrent_hash"
        val ARG_TORRENT_NAME = "arg_torrent_name"
        val ARG_FILE_PATH = "arg_file_path"
        fun newInstance(hash: String, torrentName: String, fileName: String): DeleteFileDialog {
            val frag = DeleteFileDialog()
            val bundle = Bundle()
            bundle.putString(ARG_TORRENT_HASH, hash)
            bundle.putString(ARG_TORRENT_NAME, torrentName)
            bundle.putString(ARG_FILE_PATH, fileName)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        TricklComponent.mainComponent.inject(this)
        torrentName = arguments.getString(ARG_TORRENT_NAME)
        filePath = arguments.getString(ARG_FILE_PATH)
        torrentHash = arguments.getString(ARG_TORRENT_HASH)

        val b = AlertDialog.Builder(activity)
                .setTitle("$torrentName Deleted")
                .setMessage(R.string.delete__file_dialog_text)
                .setPositiveButton("Delete",
                        { dialog, _ ->
                            val torrentFile = torrentRepository.getTorrentFileFromPersistence(torrentHash, filePath)
                            torrentRepository.deleteTorrentFileFromPersistence(torrentFile)
                            torrentRepository.deleteTorrentFileData(torrentFile)
                            dialog.dismiss()
                        }
                )
                .setNegativeButton("Keep",
                        { dialog, _ ->
                            val torrentFile = torrentRepository.getTorrentFileFromPersistence(torrentHash, filePath)
                            torrentRepository.deleteTorrentFileFromPersistence(torrentFile)
                            dialog.dismiss()
                        }
                ).setNeutralButton("Cancel",
                { dialog, _ -> dialog.dismiss() })

        return b.create()
    }
}