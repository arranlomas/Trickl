package com.shwifty.tex.views.splash.mvi

import com.arranlomas.kontent.commons.objects.KontentResult
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType

/**
 * Created by arran on 14/02/2018.
 */

sealed class SplashResult : KontentResult() {
    data class HandleIntentSuccess(val magnet: String?, val torrentFile: String?) : SplashResult()
    data class HandleIntentError(val error: Throwable) : SplashResult()
    class HandleIntentInFlight : SplashResult()
    data class StoragePermissionRequestResult(val granted: Boolean): SplashResult()
    data class StoragePermissionError(val error: Throwable) : SplashResult()
    class StoragePermissionRequestInFlight : SplashResult()
}