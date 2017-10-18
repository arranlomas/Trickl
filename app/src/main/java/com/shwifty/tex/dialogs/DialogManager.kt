package com.shwifty.tex.dialogs

import android.app.AlertDialog
import android.app.FragmentManager
import android.content.Context
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.getFullPath
import com.shwifty.tex.R
import com.shwifty.tex.views.main.MainEventHandler


/**
 * Created by arran on 10/05/2017.
 */
class DialogManager : IDialogManager {
    override lateinit var torrentRepository: ITorrentRepository

    private var TAG_DIALOG = "dialog"

    override fun showNoWifiDialog(context: Context, torrentFile: TorrentFile) {
        MaterialDialog.Builder(context)
                .title(R.string.dialog_title_no_wifi)
                .content(R.string.dialog_content_no_wifi)
                .positiveText(R.string.dialog_positive_no_wifi)
                .onPositive({ _, _ ->
                    torrentRepository.startFileDownloading(torrentFile, context, false)
                })
                .negativeText(R.string.dialog_negative_no_wifi)
                .show()
    }

    override fun showAddMagnetDialog(context: Context) {
        MaterialDialog.Builder(context)
                .title(R.string.dialog_add_magnet_title)
                .customView(R.layout.dialog_frag_add_magnet, true)
                .positiveText(android.R.string.ok)
                .onPositive({ dialog, _ ->
                    val magnetText = dialog.customView?.findViewById<EditText>(R.id.addMagnetDialogEditText)?.text?.toString()
                    magnetText?.let { MainEventHandler.addMagnet(it) }
                })
                .negativeText(android.R.string.cancel)
                .show()
    }

    override fun showAddHashDialog(context: Context) {
        MaterialDialog.Builder(context)
                .title(R.string.dialog_add_hash_title)
                .customView(R.layout.dialog_frag_add_hash, true)
                .positiveText(android.R.string.ok)
                .onPositive({ dialog, _ ->
                    val hash = dialog.customView?.findViewById<EditText>(R.id.addHashDialogEditText)?.text?.toString()
                    hash?.let { MainEventHandler.addHash(it) }
                })
                .negativeText(android.R.string.cancel)
                .show()
    }

    override fun showDeleteFileDialog(context: Context, torrentFile: TorrentFile) {
        val torrentName = torrentFile.parentTorrentName
        val torrentHash = torrentFile.torrentHash
        val filePath = torrentFile.getFullPath()

        MaterialDialog.Builder(context)
                .title("$torrentName Deleted")
                .content(R.string.delete__file_dialog_text)
                .positiveText(R.string.delete)
                .onPositive({ dialog, _ ->
                    val torrentFile = torrentRepository.getTorrentFileFromPersistence(torrentHash, filePath) ?: throw NullPointerException("Cannot delete a null torrent file")
                    torrentRepository.deleteTorrentFileFromPersistence(torrentFile)
                    torrentRepository.deleteTorrentFileData(torrentFile)
                    dialog.dismiss()
                })
                .neutralText(android.R.string.cancel)
                .onNeutral { dialog, _ -> dialog.dismiss() }
                .negativeText(R.string.keep)
                .onNegative { dialog, which ->
                    val torrentFile = torrentRepository.getTorrentFileFromPersistence(torrentHash, filePath) ?: throw NullPointerException("Cannot delete a null torrent file")
                    torrentRepository.deleteTorrentFileFromPersistence(torrentFile)
                    dialog.dismiss()
                }
                .show()
    }

    override fun showDeleteTorrentDialog(context: Context, torrentInfo: TorrentInfo, onError: () -> Unit) {
        MaterialDialog.Builder(context)
                .title("${torrentInfo.name} ${context.getString(R.string.deleted)}")
                .content(R.string.delete_torrent_dialog_text)
                .positiveText(R.string.delete)
                .onPositive { dialog, _ ->
                    torrentRepository.getTorrentInfo(torrentInfo.info_hash)
                            .subscribe({
                                it?.let {
                                    val deleted = torrentRepository.deleteTorrentInfoFromStorage(it)
                                    if (deleted) {
                                        it.fileList.forEach {
                                            torrentRepository.deleteTorrentFileFromPersistence(it)
                                        }
                                        torrentRepository.deleteTorrentData(it)
                                    } else {
                                        onError.invoke()
                                    }
                                }
                            }, {
                                it.printStackTrace()
                            })
                    dialog.dismiss()
                }
                .negativeText(R.string.keep)
                .onNegative { dialog, _ ->
                    torrentRepository.getTorrentInfo(torrentInfo.info_hash)
                            .subscribe({
                                it?.let { torrentRepository.deleteTorrentInfoFromStorage(it) }
                            }, {
                                it.printStackTrace()
                            })
                    dialog.dismiss()
                }
                .neutralText(R.string.cancel)
                .onNeutral { dialog, _ -> dialog.dismiss() }
                .show()
    }


}