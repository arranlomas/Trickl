package com.shwifty.tex.views.addtorrent.mvi

import android.util.Log
import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.ParseTorrentResult
import com.schiwfty.torrentwrapper.utils.getAsTorrentObject
import com.shwifty.tex.utils.logTorrentParseError
import com.shwifty.tex.utils.torrentAlreadyExists
import com.shwifty.tex.views.base.mvi.BaseMviViewModel
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentViewModel @Inject constructor(torrentRepository: ITorrentRepository)
    : AddTorrentContract.ViewModel, BaseMviViewModel<AddTorrentActions, AddTorrentResult, AddTorrentViewState>(
        actionProcessor = addTorrentActionProcessor(torrentRepository),
        reducer = addTorrentReducer,
        defaultState = AddTorrentViewState.default()

)

fun addTorrentActionProcessor(torrentRepository: ITorrentRepository) = KontentMasterProcessor<AddTorrentActions, AddTorrentResult> { action ->
    Observable.merge(observables(action, torrentRepository))
}

private fun observables(shared: Observable<AddTorrentActions>, torrentRepository: ITorrentRepository): List<Observable<AddTorrentResult>> {
    return listOf<Observable<AddTorrentResult>>(
            shared.ofType(AddTorrentActions.Load::class.java).compose(loadTorrent(torrentRepository)),
            shared.ofType(AddTorrentActions.RemoveTorrent::class.java).compose(removeTorrent(torrentRepository)))
}

fun loadTorrent(torrentRepository: ITorrentRepository) =
        KontentActionProcessor<AddTorrentActions.Load, AddTorrentResult, Pair<Boolean, TorrentInfo>>(
                action = { action ->
                    isAlreadyDownloadObs(torrentRepository, action.torrentHash, action.torrentFilePath)
                            .flatMap { alreadyDownloaded ->
                                getTorrentInfoObs(torrentRepository, action.torrentHash, action.trackers
                                        ?: emptyList(), action.torrentFilePath, alreadyDownloaded)
                                        .map { it.unwrapIfSuccess { alreadyDownloaded to it } }
                            }
                },
                success = {
                    val (alreadyAdded, torrentInfo) = it
                    AddTorrentResult.LoadSuccess(torrentInfo, alreadyAdded)
                },
                error = {
                    AddTorrentResult.LoadError(it)
                },
                loading = AddTorrentResult.LoadInFlight()
        )

private fun isAlreadyDownloadObs(
        torrentRepository: ITorrentRepository,
        torrentHash: String?,
        torrentFilePath: String?
): Observable<Boolean> = when {
    torrentHash != null -> torrentRepository.torrentAlreadyExists(torrentHash)
    torrentFilePath != null -> {
        val originalFile = File(torrentFilePath)
        originalFile.getAsTorrentObject()
                .flatMap {
                    it.unwrapIfSuccess {
                        torrentRepository.torrentAlreadyExists(it.info_hash)
                    }
                }

    }
    else -> Observable.just(false)
}

private fun getTorrentInfoObs(
        torrentRepository: ITorrentRepository,
        torrentHash: String?,
        trackers: List<String>,
        torrentFilePath: String?,
        alreadyExisted: Boolean
): Observable<ParseTorrentResult> = when {
    torrentHash != null -> torrentRepository.downloadTorrentInfo(torrentHash, trackers = trackers)
    torrentFilePath != null -> {
        val originalFile = File(torrentFilePath)
        originalFile.getAsTorrentObject()
                .map { result ->
                    result.unwrapIfSuccess {
                        val correctName = "${it.info_hash}.torrent"
                        val hasCorrectName = originalFile.name == correctName
                        if (alreadyExisted && !hasCorrectName) {
                            originalFile.renameTo(File(originalFile.parent, correctName))
                        } else if (!alreadyExisted && !File(Confluence.torrentInfoStorage.absolutePath, correctName).exists()) {
                            originalFile.copyTo(File(Confluence.torrentInfoStorage.absolutePath, correctName))
                        }
                        result
                    } ?: throw IllegalArgumentException("Could not parse torrent info")
                }
    }
    else -> Observable.just(null)
}

fun removeTorrent(torrentRepository: ITorrentRepository) =
        KontentActionProcessor<AddTorrentActions.RemoveTorrent, AddTorrentResult, Boolean>(
                action = { action ->
                    torrentRepository.downloadTorrentInfo(action.torrentHash)
                            .map { result ->
                                result.unwrapIfSuccess {
                                    val deleted = torrentRepository.deleteTorrentInfoFromStorage(it)
                                    if (deleted) {
                                        it.fileList.forEach {
                                            torrentRepository.deleteTorrentFileFromPersistence(it)
                                        }
                                        torrentRepository.deleteTorrentData(it)
                                    } else {
                                        throw Error("Error deleting torrent")
                                    }
                                }?.let {
                                    result.logTorrentParseError()
                                }
                                true
                            }
                },
                success = {
                    AddTorrentResult.RemoveSuccess()
                },
                error = {
                    AddTorrentResult.RemoveError(it)
                },
                loading = AddTorrentResult.RemoveInFlight()
        )

val addTorrentReducer = KontentReducer { result: AddTorrentResult, previousState: AddTorrentViewState ->
    Log.v("Add reducer result", result.toString())
    when (result) {
        is AddTorrentResult.LoadSuccess -> previousState.copy(isLoading = false, error = null, result = result.torrentInfo, torrentHash = result.torrentInfo.info_hash, torrentAlreadyExisted = result.torrentAlreadyExisted)
        is AddTorrentResult.LoadError -> previousState.copy(isLoading = false, error = result.error.localizedMessage)
        is AddTorrentResult.LoadInFlight -> previousState.copy(isLoading = true, error = null)
        is AddTorrentResult.RemoveSuccess -> previousState.copy(torrentRemovedAndShouldRestart = true, isLoading = false, error = null, torrentHash = null, result = null)
        is AddTorrentResult.RemoveError -> previousState.copy(isLoading = false, error = result.error.localizedMessage, torrentRemovedAndShouldRestart = false)
        is AddTorrentResult.RemoveInFlight -> previousState.copy(torrentRemovedAndShouldRestart = false, isLoading = false, error = null)
    }
}