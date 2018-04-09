package com.shwifty.tex.views.addtorrent.mvi

import android.util.Log
import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.utils.logTorrentParseError
import com.shwifty.tex.views.base.mvi.BaseMviViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
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
                    val alreadyExistedObs = torrentRepository.getAllTorrentsFromStorage()
                            .map { results ->
                                var alreadyExists = false
                                results.forEach { result ->
                                    result.unwrapIfSuccess {
                                        if (it.info_hash == action.torrentHash) alreadyExists = true
                                    } ?: result.logTorrentParseError()
                                }
                                alreadyExists
                            }

                    val trackerList = action.trackers ?: emptyList()
                    val downloadInfoObs = torrentRepository.downloadTorrentInfo(action.torrentHash, trackers = trackerList)
                            .map {
                                it.unwrapIfSuccess { it }
                                        ?: throw IllegalStateException("Torrent not found")
                            }

                    Observable.zip(alreadyExistedObs, downloadInfoObs, BiFunction { alreadyExisted, torrentInfo ->
                        alreadyExisted to torrentInfo
                    })
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