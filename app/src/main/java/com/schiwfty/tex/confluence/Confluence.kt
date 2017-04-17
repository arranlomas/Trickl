package com.schiwfty.tex.confluence

import android.content.Context
import android.os.Environment
import android.util.Log
import com.schiwfty.tex.Constants
import com.schiwfty.tex.extensions.copyInputStreamToFile
import io.reactivex.Observable
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
        if (isConfluenceInstalled(context)) {
            return getStartObservable(context)
        }
        return getCopyObservable(context).flatMap { getStartObservable(context) }
    }

    private fun getCopyObservable(context: Context): Observable<Boolean> {
        return Observable.just(copyConfluenceAsset(context)).doOnError { e -> throw e }
    }

    private fun getStartObservable(context: Context): Observable<Boolean> {
        if (isConfluenceInstalled(context)) {
            return Observable.just(false).doOnNext { throw IllegalStateException("Confluence is not installed!") }
        }
        return Observable.just(startConfluence(context))
    }

    private fun isConfluenceInstalled(context: Context): Boolean {
        val file = context.getFileStreamPath(Constants.confluenceFileName)
        if (file == null || !file.exists()) {
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
        val url = Constants.localhostUrl
        val port = Constants.daemonPort
        val path = Constants.torrentRepo
        val absolutePath = Constants.torrentRepo.absolutePath
        val cmd = file.absolutePath + " -addr= $url $port  -fileDir= + $absolutePath +  -torrentGrace=-10h -seed=true"
        return Runtime.getRuntime().exec(cmd, null, path)
    }


    private fun copyConfluenceAsset(context: Context): Boolean {
        try {
            Log.v("DAEMON", "copying confluence asset")
            val assetManager = context.assets
            val files: Array<String>
            files = assetManager.list("")
            files.forEach {
                if (it == Constants.confluenceFileName) {
                    val input = assetManager.open(it)
                    val outputFile = File(context.getFilesDir(), Constants.confluenceFileName)
                    outputFile.copyInputStreamToFile(input)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        }

        return isConfluenceInstalled(context)

    }

    internal fun Process.captureOutput() {
        errorStream.bufferedReader().use {
            Log.v("ERROR", "value: " + it.readText())
        }

        inputStream.bufferedReader().use {
            Log.v("STD OUT", "value: " + it.readText())
        }
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