package com.shwifty.tex.dialogs

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.afollestad.materialdialogs.MaterialDialog
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.getFullPath
import com.shwifty.tex.R
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.views.main.MainEventHandler

/**
 * Created by arran on 10/05/2017.
 */
class DialogManager : IDialogManager {

    override lateinit var torrentRepository: ITorrentRepository

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
                .onNegative { dialog, _ ->
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

    override fun showExitAppDialog(context: Context, onExit: () -> Unit) {
        MaterialDialog.Builder(context)
                .title(R.string.exit_app_dialog_title)
                .content(R.string.exit_app_dialog_description)
                .positiveText(R.string.exit_app_dialog_confirm)
                .onPositive { dialog, _ ->
                    dialog.dismiss()
                    onExit.invoke()
                }
                .negativeText(R.string.exit_app_dialog_cancel)
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()
    }

    override fun showBrowseFilterDialog(context: Context, onConfirm: (TorrentSearchSortType?, TorrentSearchCategory?) -> Unit) {
        var selectedSort: TorrentSearchSortType? = null
        var selectedCategory: TorrentSearchCategory? = null

        val categoriesAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item)
        val filteredCategories = TorrentSearchCategory.values().filter { it != TorrentSearchCategory.All }
        filteredCategories.forEach {
            categoriesAdapter.add(it.toSpinnerItemString())
        }

        val sortedByAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item)
        val sortItems = TorrentSearchSortType.values()
        sortItems.forEach { sortedByAdapter.add(it.toSpinnerItemString()) }

        val dialog = MaterialDialog.Builder(context)
                .title(R.string.dialog_filter_browse_title)
                .customView(R.layout.dialog_browse_filters, true)
                .positiveText(R.string.dialog_filter_browse_confirm)
                .negativeText(R.string.dialog_filter_browse_cancel)
                .onPositive { dialog, _ ->
                    dialog.dismiss()
                    onConfirm.invoke(selectedSort, selectedCategory)
                }
                .onNegative { dialog, _ -> dialog.dismiss() }
                .show()

        val categorySpinner = dialog.customView?.findViewById<Spinner>(R.id.categorySpinner)
        categorySpinner?.adapter = categoriesAdapter
        val sortedBySpinner = dialog.customView?.findViewById<Spinner>(R.id.sortBySpinner)
        sortedBySpinner?.adapter = sortedByAdapter

        categorySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filteredCategories.forEach { if (it.toSpinnerItemString() == categoriesAdapter.getItem(position)) selectedCategory = it }
            }
        }

        sortedBySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sortItems.forEach { if (it.toSpinnerItemString() == sortedByAdapter.getItem(position)) selectedSort = it }
            }
        }
    }

}