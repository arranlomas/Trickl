package com.shwifty.tex.views.showtorrent.mvi

import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.base.mvi.BaseMviViewModel
import io.reactivex.Observable
import javax.inject.Inject

class TorrentInfoViewModel @Inject constructor(torrentRepository: ITorrentRepository)
    : TorrentInfoContract.ViewModel, BaseMviViewModel<TorrentInfoIntent, TorrentInfoActions, TorrentInfoResult, TorrentInfoViewState>(
        intentToAction = { torrentInfoIntentToAction(it) },
        actionProcessor = torrentInfoActionProcessor(torrentRepository),
        reducer = torrentInfoReducer,
        defaultState = TorrentInfoViewState.default()

)

fun torrentInfoIntentToAction(intent: TorrentInfoIntent): TorrentInfoActions = when (intent) {
    is TorrentInfoIntent.LoadInfoIntent -> TorrentInfoActions.Load(intent.torrentHash)
}


fun torrentInfoActionProcessor(torrentRepository: ITorrentRepository) = KontentMasterProcessor<TorrentInfoActions, TorrentInfoResult> { action ->
    Observable.merge(observables(action, torrentRepository))
}

private fun observables(shared: Observable<TorrentInfoActions>, torrentRepository: ITorrentRepository): List<Observable<TorrentInfoResult>> {
    return listOf<Observable<TorrentInfoResult>>(
            shared.ofType(TorrentInfoActions.Load::class.java).compose(loadTorrent(torrentRepository)))
}

fun loadTorrent(torrentRepository: ITorrentRepository) =
        KontentActionProcessor<TorrentInfoActions.Load, TorrentInfoResult, TorrentInfo>(
                action = { action ->
                    torrentRepository.downloadTorrentInfo(action.torrentHash)
                            .map {
                                it.unwrapIfSuccess { it }
                                        ?: throw IllegalStateException("couldn't parse torrent result")
                            }
                },
                success = {
                    TorrentInfoResult.LoadSuccess(it)
                },
                error = {
                    TorrentInfoResult.LoadError(it)
                },
                loading = TorrentInfoResult.LoadInFlight()
        )


val torrentInfoReducer = KontentReducer { result: TorrentInfoResult, previousState: TorrentInfoViewState ->
    when (result) {
        is TorrentInfoResult.LoadSuccess -> previousState.copy(isLoading = false, error = null, result = result.torrentInfo, torrentHash = result.torrentInfo.info_hash)
        is TorrentInfoResult.LoadError -> previousState.copy(isLoading = false, error = result.error.localizedMessage)
        is TorrentInfoResult.LoadInFlight -> previousState.copy(isLoading = true, error = null)
    }
}