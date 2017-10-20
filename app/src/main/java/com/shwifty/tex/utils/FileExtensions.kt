package com.shwifty.tex.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.support.v4.content.FileProvider
import android.webkit.MimeTypeMap
import java.io.File

/**
 * Created by arran on 20/10/2017.
 */
fun File.openFileViaIntent(context: Context, onActivityNotFound: () -> Unit) {
    val newIntent = Intent(Intent.ACTION_VIEW)
    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    val fileUri = FileProvider.getUriForFile(context, "com.shwifty.tex.utils.GenericFileProvider", this)
    newIntent.setDataAndType(fileUri, mimeType)
    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    try {
        context.startActivity(newIntent)
    } catch (e: ActivityNotFoundException) {
        onActivityNotFound.invoke()
    }

}