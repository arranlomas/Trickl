package com.schiwfty.tex.confluence

import android.content.Context
import android.util.Log
import com.schiwfty.tex.Constants
import com.schiwfty.tex.dagger.utilities.JavaUtils
import com.schiwfty.tex.dagger.utilities.captureOutput
import rx.Observable
import java.io.*
import java.net.ServerSocket

/**
 * Created by arran on 11/04/2017.
 */
object Confluence {

    fun getSetupObservable(context: Context): Observable<Boolean> {
        if (isConfluenceInstalled(context)) {
            return getStartObservable(context)
        } else {
            return getCopyAndStartObservable(context)
        }
    }

    private fun getCopyAndStartObservable(context: Context): Observable<Boolean> {
        return getCopyObservable(context).flatMap { getStartObservable(context) }
    }

    private fun getCopyObservable(context: Context): Observable<Boolean> {
        return Observable.just(copyConfluenceAsset(context)).doOnError { e -> throw e }
    }

    private fun getStartObservable(context: Context): Observable<Boolean> {
        return Observable.just(startConfluence(context))
    }

    private fun isConfluenceInstalled(context: Context): Boolean {
        val file = context.getFileStreamPath(Constants.confluenceFileName)
        if(!Constants.torrentRepo.exists()) Constants.torrentRepo.mkdirs()
        if (file == null || !file.exists() || !Constants.torrentRepo.exists()) {
            return false
        }
        return true
    }

    private fun startConfluence(context: Context): Boolean {
        try {
            executeCommand(context).captureOutput()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    @Throws(IOException::class)
    private fun executeCommand(context: Context): Process {
        val file = context.getFileStreamPath(Constants.confluenceFileName)
        file.setExecutable(true)
        val workingDir = Constants.torrentRepo
        Log.v("WORKING DIR", workingDir.getAbsolutePath())
        if (!workingDir.exists()) workingDir.mkdirs()
        val cmd = file.absolutePath + " -addr=" + Constants.localhostUrl + Constants.daemonPort + " -fileDir=" + workingDir.getAbsolutePath() + " -torrentGrace=-10h" + " -seed=true"

        Log.v("COMMAND", cmd)
        return Runtime.getRuntime().exec(cmd, null, workingDir)
    }


    private fun copyConfluenceAsset(context: Context): Boolean {
        Log.v("DAEMON", "copying confluence asset")
        val assetManager = context.assets
        val files: Array<String>
        try {
            files = assetManager.list("")
        } catch (e: IOException) {
            Log.e("tag", "Failed to get asset file list.", e)
            return false
        }

        for (filename in files) {
            if (filename == Constants.confluenceFileName) {
                val `in`: InputStream
                try {
                    `in` = assetManager.open(filename)
                    val fos = context.openFileOutput(Constants.confluenceFileName, Context.MODE_PRIVATE)
                    JavaUtils.copyFile(`in`, fos)
                    `in`.close()
                    fos.flush()
                    fos.close()
                } catch (e: IOException) {
                    Log.e("tag", "Failed to copy asset file: " + filename, e)
                    return false
                }

            }
        }
        return isConfluenceInstalled(context)
    }

    fun setClientAddress() {
        val port = getAvaiablePort().toString()
        val addr = "http://" + Constants.localhostUrl + port
        Log.v("AVAILABLE ADDR", addr)
        Constants.daemonPort = port
        Constants.fullUrl = addr
    }

    private fun getAvaiablePort(): Int {
        var s: ServerSocket? = null
        var streamPort = -1
        try {
            s = ServerSocket(0)
            streamPort = s.localPort
            return streamPort
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (s != null)
                try {
                    s.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

        }
        return streamPort
    }

}