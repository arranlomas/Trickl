package com.shwifty.tex.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.crashlytics.android.Crashlytics
import com.schiwfty.torrentwrapper.utils.findNameFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.shwifty.tex.R
import com.shwifty.tex.models.AppTheme
import com.shwifty.tex.repository.preferences.PreferencesRepository


/**
 * Created by arran on 28/10/2017.
 */

fun View.closeKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.openKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInputFromInputMethod(windowToken, 0)
}

fun Context.forceOpenKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
}

fun Context.forceCloseKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
}

fun SearchView.onSearchSubmitted(onSubmit: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            onSubmit.invoke(query)
            return true
        }
    })
}

fun View.setVisible(visible: Boolean) {
    if (visible) visibility = View.VISIBLE
    else visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return !(visibility == View.INVISIBLE || visibility == View.GONE)
}

fun Context.dpToPx(dp: Int): Int {
    val displayMetrics = resources.displayMetrics
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

fun Context.pxToDp(px: Int): Int {
    val displayMetrics = resources.displayMetrics
    return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

fun View.animateWidthChange(newWidth: Int, onAnimationFinished: (() -> Unit)? = null, duration: Long = 250) {
    val anim = ValueAnimator.ofInt(layoutParams.width, newWidth)
    anim.duration = duration
    anim.addUpdateListener { valueAnimator ->
        val `val` = valueAnimator.animatedValue as Int
        val newLayoutParams = layoutParams
        newLayoutParams.width = `val`
        layoutParams = newLayoutParams
    }

    anim.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(p0: Animator?) {
        }

        override fun onAnimationEnd(p0: Animator?) {
            layoutParams.width = newWidth
            onAnimationFinished?.invoke()
        }

        override fun onAnimationCancel(p0: Animator?) {
        }

        override fun onAnimationStart(p0: Animator?) {
        }
    })
    anim.start()
}

fun EditText.afterTextChanged(afterTextChange: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChange.invoke(editable?.toString() ?: "")
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

    })
}

fun Intent?.validateOnActivityResult(requestCode: Int, expectedRequestCode: Int, resultCode: Int, expectedResultCode: Int, onIsValid: (Bundle) -> Unit) {
    if (requestCode == expectedRequestCode) {
        if (resultCode == expectedResultCode) {
            this?.extras?.let {
                onIsValid.invoke(it)
            }
        }
    }
}

fun Activity.onCreateSetThemeAndCallSuper(onLoaded: () -> Unit) {
    PreferencesRepository().getThemPreference(this)
            .subscribe({
                when (it) {
                    AppTheme.LIGHT -> {
                        setTheme(R.style.AppTheme_Light)
                        window.decorView.setLightStatusBar()
                    }
                    AppTheme.DARK -> {
                        setTheme(R.style.AppTheme)
                        window.decorView.clearLightStatusBar()
                    }
                    AppTheme.OLED -> {
                        setTheme(R.style.AppTheme_OLED)
                        window.decorView.clearLightStatusBar()
                    }
                    else -> {
                    }
                }
            }, {
                Crashlytics.logException(it)
            }, {
                onLoaded.invoke()
            })
}

private fun View.clearLightStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        systemUiVisibility = flags
    }
}

private fun View.setLightStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.systemUiVisibility = this.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}


const val ARG_ADD_TORRENT_RESULT = "arg_torrent_hash_result"
const val ARG_TORRENT_HASH = "arg_torrent_hash"
const val ARG_TORRENT_MAGNET = "arg_torrent_magnet"
const val ARG_TORRENT_FILE_PATH = "arg_torrent_file_path"

fun Activity.getTorrentNameFromMagnet(): String? = getMagnetFromIntent()?.findNameFromMagnet()

fun Activity.getTrackersFromMagnet(): List<String>? = getMagnetFromIntent()?.findTrackersFromMagnet()


fun Activity.getHashFromIntent(): String? {
    val arguments = intent.extras
    return if (arguments != null && arguments.containsKey(ARG_TORRENT_HASH)) {
        arguments.getString(ARG_TORRENT_HASH)
    } else null
}

fun Activity.getMagnetFromIntent(): String? {
    val arguments = intent.extras
    return if (arguments != null && arguments.containsKey(ARG_TORRENT_MAGNET)) {
        arguments.getString(ARG_TORRENT_MAGNET)
    } else null
}

fun Activity.getTorrentFilePathFromIntent(): String? {
    val arguments = intent.extras
    return if (arguments != null && arguments.containsKey(ARG_TORRENT_FILE_PATH)) {
        arguments.getString(ARG_TORRENT_FILE_PATH)
    } else null
}

fun Fragment.getTorrentNameFromMagnet(): String? = getMagnetFromIntent()?.findNameFromMagnet()

fun Fragment.getTrackersFromMagnet(): List<String>? = getMagnetFromIntent()?.findTrackersFromMagnet()


fun Fragment.getHashFromIntent(): String? = arguments?.getString(ARG_TORRENT_HASH)

fun Fragment.getMagnetFromIntent(): String? = arguments?.getString(ARG_TORRENT_MAGNET)


fun Uri.getRealPath(context: Context): String? {

    val uri = this

    // DocumentProvider
    if (DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return "${Environment.getExternalStorageDirectory()}/${split[1]}"
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {

            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf<String>(split[1])

            return getDataColumn(context, contentUri, selection, selectionArgs)
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {

        // Return the remote address
        return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)

    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    // MediaStore (and general)
    else if ("content".equals(uri.scheme, ignoreCase = true)) {

        // Return the remote address
        if (isGooglePhotosUri(uri))
            return uri.lastPathSegment;

        return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path;
    }


    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context       The context.
 * @param uri           The Uri to query.
 * @param selection     (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
private fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                          selectionArgs: Array<String>?): String? {

    var cursor: Cursor? = null
    val column = MediaStore.Images.Media.DATA
    val projection = arrayOf(column)

    try {
        cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor!!.moveToFirst()) {
            val index = cursor!!.getColumnIndexOrThrow(column)
            return cursor!!.getString(index)
        }
    } finally {
        if (cursor != null)
            cursor!!.close()
    }
    return null
}


/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}


fun Uri.getRealPathFromUri(context: Context): String? {

    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    val uri = this
    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                val path = StringBuilder()
                path.append(Environment.getExternalStorageDirectory())
                path.append("/")
                path.append(split[1])
                return path.toString()
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            if (!TextUtils.isEmpty(id)) {
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }
                return try {
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                    getDataColumn(context, contentUri, null, null)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    null
                }
            }
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return getDataColumn(context, contentUri, selection, selectionArgs)
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {

        // Return the remote address
        return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)

    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }// File
    // MediaStore (and general)

    return null
}