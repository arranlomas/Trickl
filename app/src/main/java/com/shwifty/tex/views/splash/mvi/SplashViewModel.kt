package com.shwifty.tex.views.splash.mvi

import android.Manifest
import android.content.Intent
import android.util.Log
import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.R
import com.shwifty.tex.utils.getRealPathFromUri
import com.shwifty.tex.views.base.mvi.BaseMviViewModel
import io.reactivex.Observable
import javax.inject.Inject

class SplashViewModel @Inject constructor(torrentRepository: ITorrentRepository)
    : SplashContract.ViewModel, BaseMviViewModel<SplashActions, SplashResult, SplashViewState>(
        actionProcessor = torrentInfoActionProcessor(torrentRepository),
        reducer = splashReducer,
        defaultState = SplashViewState.default()

)

fun torrentInfoActionProcessor(torrentRepository: ITorrentRepository) = KontentMasterProcessor<SplashActions, SplashResult> { action ->
    Observable.merge(observables(action, torrentRepository))
}

private fun observables(shared: Observable<SplashActions>, torrentRepository: ITorrentRepository): List<Observable<SplashResult>> {
    return listOf<Observable<SplashResult>>(
            shared.ofType(SplashActions.HandleIntent::class.java).compose(handleIntent()),
            shared.ofType(SplashActions.RequestPermissions::class.java).compose(requestPermission()),
            shared.ofType(SplashActions.StartConfluence::class.java).compose(startConfluence(torrentRepository))
    )
}

private fun handleIntent() =
        KontentActionProcessor<SplashActions.HandleIntent, SplashResult, Triple<Boolean, String?, String?>>(
                action = { action ->
                    var didHaveData = false
                    var magnet: String? = null
                    var torrentFile: String? = null
                    action.intent.dataString?.let { dataString ->
                        didHaveData = true
                        if (dataString.startsWith("magnet")) {
                            magnet = dataString
                        } else {
                            action.intent.data.getRealPathFromUri(action.context)?.let {
                                torrentFile = it
                            }
                        }
                    }
                    Observable.just(Triple(didHaveData, magnet, torrentFile))
                },
                success = {
                    val (didHaveData, magnet, torrentFile) = it
                    if (didHaveData && (magnet == null && torrentFile == null)) {
                        SplashResult.HandleIntentError(CouldNotLoadTorrentError())
                    } else {
                        SplashResult.HandleIntentSuccess(magnet, torrentFile)
                    }
                },
                error = {
                    SplashResult.HandleIntentError(it)
                },
                loading = SplashResult.HandleIntentInFlight()
        )

private fun requestPermission() =
        KontentActionProcessor<SplashActions.RequestPermissions, SplashResult, Boolean>(
                action = {
                    it.rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                },
                success = {
                    SplashResult.StoragePermissionRequestResult(it)
                },
                error = {
                    SplashResult.StoragePermissionError(it)
                },
                loading = SplashResult.StoragePermissionRequestInFlight()

        )

private fun startConfluence(torrentRepository: ITorrentRepository) =
        KontentActionProcessor<SplashActions.StartConfluence, SplashResult, Boolean>(
                action = { action ->
                    val notificationIntent = Intent(action.activity, SplashActivity::class.java)
                    val seed = false
                    val stopActionInNotification = true
                    val notificationChannelId = "trickl_daemon_notif"
                    val notificationChannelName = "Trick Client Daemon"
                    Confluence.start(action.activity, R.drawable.trickl_notification, notificationChannelId, notificationChannelName, seed, stopActionInNotification, notificationIntent, {
                        Log.v("Arran", "Storage permissions is required to start the client")
                    })
                    torrentRepository.isConnected()
                            .retry()
                },
                success = {
                    SplashResult.ConfluenceIsConnected(it)
                },
                error = {
                    SplashResult.StartConfluenceError(it)
                },
                loading = SplashResult.ConfluenceConnectionInFlight()

        )


private val splashReducer = KontentReducer { result: SplashResult, previousState: SplashViewState ->
    when (result) {
        is SplashResult.HandleIntentSuccess -> previousState.copy(isLoading = false, error = null, magnet = result.magnet, torrentFile = result.torrentFile)
        is SplashResult.HandleIntentError -> previousState.copy(isLoading = false, error = if (result.error is CouldNotLoadTorrentError) "Error loading torrent" else result.error.localizedMessage, magnet = null, torrentFile = null)
        is SplashResult.HandleIntentInFlight -> previousState.copy(isLoading = true)
        is SplashResult.StoragePermissionRequestResult -> previousState.copy(permissionGranted = result.granted)
        is SplashResult.StoragePermissionError -> previousState.copy(permissionGranted = false, error = result.error.localizedMessage)
        is SplashResult.StoragePermissionRequestInFlight -> previousState.copy(permissionRequested = true)
        is SplashResult.ConfluenceIsConnected -> previousState.copy(confluenceStarted = true, isLoading = false, waitingForConfluenceToStart = false)
        is SplashResult.StartConfluenceError -> previousState.copy(error = result.error.localizedMessage, isLoading = false, waitingForConfluenceToStart = false)
        is SplashResult.ConfluenceConnectionInFlight -> previousState.copy(isLoading = true, waitingForConfluenceToStart = true)
    }
}