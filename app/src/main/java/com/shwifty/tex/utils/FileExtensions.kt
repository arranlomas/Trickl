package com.shwifty.tex.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.support.v4.content.FileProvider
import android.webkit.MimeTypeMap
import com.shwifty.tex.R
import java.io.File

/**
 * Created by arran on 20/10/2017.
 */
fun File.openFileViaIntent(context: Context, onActivityNotFound: () -> Unit, unexpectedError: () -> Unit) {
    try {
        val newIntent = Intent(Intent.ACTION_VIEW)
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        var fileUri: Uri
        try {
            fileUri = FileProvider.getUriForFile(context, "com.shwifty.tex.utils.GenericFileProvider", this)
        } catch (e: Exception){
            e.printStackTrace()
            fileUri = Uri.parse(this.toString())
        }
        newIntent.setDataAndType(fileUri, mimeType)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            context.startActivity(newIntent)
        } catch (e: ActivityNotFoundException) {
            onActivityNotFound.invoke()
        }
    } catch (e: Exception){
        unexpectedError.invoke()
        e.printStackTrace()
    }
}


fun File.validateWorkingDirectoryCanBeChanged(previousDirectory: File): ValidateChangeWorkingDirectoryResult {
    if (!isDirectory) return ValidateChangeWorkingDirectoryResult.Error(R.string.working_direcotory_error_not_folder)
    if (absolutePath == Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath) return ValidateChangeWorkingDirectoryResult.Error(R.string.working_directory_error_is_downloads)
    if (absolutePath.startsWith(previousDirectory.absolutePath)) return ValidateChangeWorkingDirectoryResult.Error(R.string.working_directory_error_cannot_be_child)
    if (listFiles().isNotEmpty()) return ValidateChangeWorkingDirectoryResult.Error(R.string.working_directory_error_is_not_empty)
    return ValidateChangeWorkingDirectoryResult.Success()
}

sealed class ValidateChangeWorkingDirectoryResult {
    class Success : ValidateChangeWorkingDirectoryResult()
    data class Error(val messageRes: Int) : ValidateChangeWorkingDirectoryResult()
}