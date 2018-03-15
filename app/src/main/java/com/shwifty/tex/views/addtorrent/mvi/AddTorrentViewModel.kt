package com.shwifty.tex.views.addtorrent.mvi

import android.util.Log
import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.base.mvi.BaseMviViewModel
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentViewModel @Inject constructor(torrentRepository: ITorrentRepository)
    : AddTorrentContract.ViewModel, BaseMviViewModel<AddTorrentIntent, AddTorrentActions, AddTorrentResult, AddTorrentViewState>(
        intentToAction = { addTorrentIntentToAction(it) },
        actionProcessor = addTorrentActionProcessor(torrentRepository),
        reducer = addTorrentReducer,
        defaultState = AddTorrentViewState.default()

)

fun addTorrentIntentToAction(intent: AddTorrentIntent): AddTorrentActions = when (intent) {
    is AddTorrentIntent.LoadIntent -> AddTorrentActions.Load(intent.torrentHash)
    is AddTorrentIntent.RemoveTorrent -> AddTorrentActions.RemoveTorrent(intent.torrentHash)
}


fun addTorrentActionProcessor(torrentRepository: ITorrentRepository) = KontentMasterProcessor<AddTorrentActions, AddTorrentResult> { action ->
    Observable.merge(observables(action, torrentRepository))
}

private fun observables(shared: Observable<AddTorrentActions>, torrentRepository: ITorrentRepository): List<Observable<AddTorrentResult>> {
    return listOf<Observable<AddTorrentResult>>(
            shared.ofType(AddTorrentActions.Load::class.java).compose(loadTorrent(torrentRepository)),
            shared.ofType(AddTorrentActions.RemoveTorrent::class.java).compose(removeTorrent(torrentRepository)))
}

fun loadTorrent(torrentRepository: ITorrentRepository) =
        KontentActionProcessor<AddTorrentActions.Load, AddTorrentResult, TorrentInfo>(
                action = { action -> torrentRepository.downloadTorrentInfo(action.torrentHash)
                            .map {
                                it.unwrapIfSuccess { it }
                                        ?: throw IllegalStateException("Torrent not found")
                            }
                },
                success = { result ->
                    AddTorrentResult.LoadSuccess(result)
                },
                error = {
                    AddTorrentResult.LoadError(it)
                },
                loading = AddTorrentResult.LoadInFlight()
        )

fun removeTorrent(torrentRepository: ITorrentRepository) =
        KontentActionProcessor<AddTorrentActions.RemoveTorrent, AddTorrentResult, Boolean>(
                action = { action ->
                    torrentRepository.getAllTorrentsFromStorage()
                            .map {
                                val successList = mutableListOf<TorrentInfo>()
                                it.forEach { it.unwrapIfSuccess { successList.add(it) } }
                                successList.toList()
                            }
                            .map {
                                it.filter { it.info_hash == action.torrentHash }.firstOrNull()
                                        ?: throw IllegalArgumentException()
                            }
                            .map { torrentRepository.deleteTorrentInfoFromStorage(it) }
                            .doOnComplete {
                                Log.v("OnComplete", "remove torrent")
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
    when (result) {
        is AddTorrentResult.LoadSuccess -> previousState.copy(isLoading = false, error = null, result = result.torrentInfo, torrentHash = result.torrentInfo.info_hash)
        is AddTorrentResult.LoadError -> previousState.copy(isLoading = false, error = result.error.localizedMessage)
        is AddTorrentResult.LoadInFlight -> previousState.copy(isLoading = true, error = null)
        is AddTorrentResult.RemoveSuccess -> previousState.copy(torrentRemovedAndShouldRestart = true, isLoading = false, error = null)
        is AddTorrentResult.RemoveError -> previousState.copy(isLoading = false, error = result.error.localizedMessage)
        is AddTorrentResult.RemoveInFlight -> previousState.copy(torrentRemovedAndShouldRestart = false, isLoading = true, error = null)
    }
}


//
//(val torrentRepository: ITorrentRepository) : BaseMviViewModel<> {
//
//    private var alreadyExisted = false
//
//    override var torrentHash: String? = null
//    override var torrentMagnet: String? = null
//    override var torrentName: String? = null
//    override var torrentTrackers: List<String>? = null
//
//    override fun setup(arguments: Bundle?) {
//        if (arguments?.containsKey(AddTorrentActivity.Companion.ARG_TORRENT_HASH) == true) {
//            torrentHash = arguments?.getString(AddTorrentActivity.Companion.ARG_TORRENT_HASH) ?: ""
//        }
//
//        if (arguments?.containsKey(AddTorrentActivity.Companion.ARG_TORRENT_MAGNET) == true) {
//            torrentMagnet = arguments?.getString(AddTorrentActivity.Companion.ARG_TORRENT_MAGNET) ?: ""
//            torrentHash = torrentMagnet?.findHashFromMagnet()
//            torrentMagnet?.findNameFromMagnet()?.let {
//                torrentName = URLDecoder.decode(it, "UTF-8")
//            }
//            torrentTrackers = torrentMagnet?.findTrackersFromMagnet()
//        }
//
//        torrentRepository.getAllTorrentsFromStorage()
//                .subscribe(object : BaseSubscriber<List<ParseTorrentResult>>() {
//                    override fun onCompleted() {
//                    }
//
//                    override fun onStart() {
//                    }
//
//                    override fun onNext(results: List<ParseTorrentResult>) {
//                        var alreadyExists = false
//                        results.forEach { result ->
//                            result.unwrapIfSuccess {
//                                if (it.info_hash == torrentHash) alreadyExists = true
//                            } ?: let { result.logTorrentParseError() }
//                        }
//                        this@AddTorrentViewModel.alreadyExisted = alreadyExists
//                        fetchTorrent()
//                    }
//                })
//    }
//
//    private fun fetchTorrent() {
//        val hash = torrentHash ?: return
//        torrentRepository.downloadTorrentInfo(hash)
//                .subscribe(object : BaseSubscriber<ParseTorrentResult>() {
//                    override fun onNext(result: ParseTorrentResult) {
//                        mvpView.setLoading(false)
//                        mvpView.notifyTorrentAdded()
//                        if (result is ParseTorrentResult.Error) super.onError(result.exception)
//                    }
//                })
//                .addSubscription()
//    }
//
//}