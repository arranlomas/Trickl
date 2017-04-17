package com.schiwfty.tex.tools.retrofit


import javax.inject.Inject

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import rx.Observable

/**
 * Created by arran on 4/02/2017.
 */


class HttpController @Inject
constructor(private val clientAPI: ClientAPI) {

    fun getInfo(hash: String): Observable<ResponseBody> {
        return clientAPI.getInfo(hash)
    }

    fun postTorrent(hash: String, bencode: ByteArray): Observable<ResponseBody> {
        val requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), bencode)
        return clientAPI.postTorrent(hash, requestBody)
    }

    val status: Observable<ResponseBody>
        get() = clientAPI.status


}