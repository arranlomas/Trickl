package com.shwifty.tex.dialogs

import android.content.Context
import android.view.View
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.getFullPath
import com.shwifty.tex.R
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.utils.logTorrentParseError
import com.shwifty.tex.views.main.MainEventHandler
import es.dmoral.toasty.Toasty
import java.io.File

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
                    val foundTorrentFile = torrentRepository.getTorrentFileFromPersistence(torrentHash, filePath)
                    foundTorrentFile?.let {
                        torrentRepository.deleteTorrentFileFromPersistence(torrentFile)
                        torrentRepository.deleteTorrentFileData(torrentFile)
                    } ?: Toasty.error(context, context.getString(R.string.error_deleting_torrent), Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                })
                .neutralText(android.R.string.cancel)
                .onNeutral { dialog, _ -> dialog.dismiss() }
                .negativeText(R.string.keep)
                .onNegative { dialog, _ ->
                    val persistentTorrentFile = torrentRepository.getTorrentFileFromPersistence(torrentHash, filePath)
                    persistentTorrentFile?.let {
                        torrentRepository.deleteTorrentFileFromPersistence(persistentTorrentFile)
                    }
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
                    torrentRepository.downloadTorrentInfo(torrentInfo.info_hash)
                            .subscribe({ result ->
                                result.unwrapIfSuccess {
                                    val deleted = torrentRepository.deleteTorrentInfoFromStorage(it)
                                    if (deleted) {
                                        it.fileList.forEach {
                                            torrentRepository.deleteTorrentFileFromPersistence(it)
                                        }
                                        torrentRepository.deleteTorrentData(it)
                                    } else {
                                        onError.invoke()
                                    }
                                }?.let { result.logTorrentParseError() }
                            }, {
                                it.printStackTrace()
                            })
                    dialog.dismiss()
                }
                .negativeText(R.string.keep)
                .onNegative { dialog, _ ->
                    torrentRepository.downloadTorrentInfo(torrentInfo.info_hash)
                            .subscribe({ result ->
                                result.unwrapIfSuccess {
                                    torrentRepository.deleteTorrentInfoFromStorage(it)
                                } ?: let { result.logTorrentParseError() }
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

    override fun showBrowseFilterDialog(context: Context,
                                        defaultSortType: TorrentSearchSortType,
                                        defaultCategory: TorrentSearchCategory,
                                        onConfirm: (TorrentSearchSortType, TorrentSearchCategory) -> Unit) {

        var selectedSort: TorrentSearchSortType = defaultSortType
        var selectedCategory: TorrentSearchCategory = defaultCategory

        val categoriesAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item)
        val filteredCategories = TorrentSearchCategory.values().filter { it != TorrentSearchCategory.All }
        filteredCategories.forEach {
            categoriesAdapter.add(it.toHumanFriendlyString())
        }

        val sortedByAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item)
        val sortItems = TorrentSearchSortType.values()
        sortItems.forEach { sortedByAdapter.add(it.toHumanFriendlyString()) }

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

        filteredCategories.forEachIndexed { index, torrentSearchCategory ->
            if (torrentSearchCategory == defaultCategory) categorySpinner?.setSelection(index)
        }

        sortItems.forEachIndexed { index, sortItem ->
            if (sortItem == defaultSortType) sortedBySpinner?.setSelection(index)
        }

        categorySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filteredCategories.forEach { if (it.toHumanFriendlyString() == categoriesAdapter.getItem(position)) selectedCategory = it }
            }
        }

        sortedBySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sortItems.forEach { if (it.toHumanFriendlyString() == sortedByAdapter.getItem(position)) selectedSort = it }
            }
        }
    }

    override fun showChangeWorkingDirectoryRestartRequired(context: Context, onContinue: () -> Unit) {
        MaterialDialog.Builder(context)
                .title(R.string.change_working_directory_warning_dialog_title)
                .content(R.string.change_working_directory_warning_dialog_description)
                .positiveText(R.string.change_working_directory_warning_dialog_confirm)
                .onPositive { dialog, _ ->
                    dialog.dismiss()
                    onContinue.invoke()
                }
                .negativeText(R.string.change_working_directory_warning_dialog_cancel)
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    override fun showChangeWorkingDirectoryDialog(context: Context, newDirectory: File, onMove: (File) -> Unit, onKeepInDirectory: (File) -> Unit) {
        MaterialDialog.Builder(context)
                .title(R.string.change_working_directory_dialog_title)
                .content(R.string.change_working_directory_dialog_description)
                .positiveText(R.string.change_working_directory_dialog_confirm)
                .onPositive { dialog, _ ->
                    dialog.dismiss()
                    onMove.invoke(newDirectory)
                }
                .negativeText(R.string.change_working_directory_dialog_cancel)
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                    onKeepInDirectory.invoke(newDirectory)
                }
                .show()
    }

    override fun showSettingExitDialog(context: Context, onRestart: () -> Unit) {
        MaterialDialog.Builder(context)
                .title(R.string.change_settings_exit_restart_title)
                .content(R.string.change_settings_exit_restart_description)
                .positiveText(R.string.change_settings_exit_restart_positive)
                .onPositive { dialog, _ ->
                    dialog.dismiss()
                    onRestart.invoke()
                }
                .negativeText(R.string.change_settings_exit_restart_negative)
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

}