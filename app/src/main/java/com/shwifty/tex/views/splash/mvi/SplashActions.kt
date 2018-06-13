package com.shwifty.tex.views.splash.mvi

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import com.arranlomas.kontent.commons.objects.KontentAction
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Created by arran on 14/02/2018.
 */
sealed class SplashActions : KontentAction() {
    data class HandleIntent(val intent: Intent, val contentResolver: ContentResolver) : SplashActions()
    data class RequestPermissions(val rxPermissions: RxPermissions) : SplashActions()
    data class StartConfluence(val activity: Activity): SplashActions()
}
