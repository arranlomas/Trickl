package com.schiwfty.tex.retrofit


import com.schiwfty.tex.models.ConfluenceInfo
import com.schiwfty.tex.models.FileStatePiece
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import rx.Observable


/**
 * Created by arran on 4/02/2017.
 */


class ConfluenceApi(private val clientAPI: ClientAPI) {

    fun getInfo(hash: String): Observable<ResponseBody> {
        return clientAPI.getInfo(hash)
    }

    fun postTorrent(hash: String, bencode: ByteArray): Observable<ResponseBody> {
        val requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), bencode)
        return clientAPI.postTorrent(hash, requestBody)
    }

    val getStatus: Observable<ConfluenceInfo>
        get() = clientAPI.getStatus()


    fun getFileData(hash: String, path: String, byteRangeStart: Long, byteRangeEnd: Long): Observable<ResponseBody> {
        return clientAPI.getData(hash, path, "bytes=$byteRangeStart-$byteRangeEnd")
    }

    fun getFileState(hash: String, path: String): Observable<List<FileStatePiece>> {
        return clientAPI.getFileState(hash, path)
    }

}