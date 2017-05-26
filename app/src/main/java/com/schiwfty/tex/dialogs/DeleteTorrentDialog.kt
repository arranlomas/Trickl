package com.schiwfty.tex.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import com.schiwfty.tex.R
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import javax.inject.Inject

/**
 * Created by arran on 25/05/2017.
 */
class DeleteTorrentDialog : DialogFragment() {
    private lateinit var torrentName: String
    private lateinit var torretHash: String

    @Inject
    lateinit var torrentRepository: ITorrentRepository

    companion object {
        val ARG_TORRENT_NAME = "arg_torrent_name"
        val ARG_TORRENT_HASH = "arg_torrent_hash"
        fun newInstance(torrentName: String, torrentHash: String): DeleteTorrentDialog {
            val frag = DeleteTorrentDialog()
            val bundle = Bundle()
            bundle.putString(ARG_TORRENT_NAME, torrentName)
            bundle.putString(ARG_TORRENT_HASH, torrentHash)
            frag.arguments = bundle
            return frag
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        TricklComponent.mainComponent.inject(this)
        torrentName = arguments.getString(ARG_TORRENT_NAME)
        torretHash = arguments.getString(ARG_TORRENT_HASH)

        val b = AlertDialog.Builder(activity)
                .setTitle("$torrentName Deleted")
                .setMessage(R.string.delete_torrent_dialog_text)
                .setPositiveButton("Delete",
                        { dialog, _ ->
                            val deleted = torrentRepository.deleteTorrentInfoFromStorage(torretHash)
                            if (deleted) torrentRepository.deleteTorrentData(torrentName)
                            dialog.dismiss()
                        }
                )
                .setNegativeButton("Keep",
                        { dialog, _ ->
                            torrentRepository.deleteTorrentInfoFromStorage(torretHash)
                            dialog.dismiss()
                        }
                ).setNeutralButton("Cancel",
                { dialog, _ -> dialog.dismiss() })

        return b.create()
    }
}