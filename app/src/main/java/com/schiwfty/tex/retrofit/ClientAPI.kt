package com.schiwfty.tex.retrofit

/**
 * Created by arran on 4/02/2017.
 */


import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable


interface ClientAPI {

    @POST("/metainfo")
    fun postTorrent(@Query("ih") info_hash: String, @Body bencodedBody: RequestBody): Observable<ResponseBody>

    @GET("/info")
    fun getInfo(@Query("ih") info_hash: String): Observable<ResponseBody>

    @get:GET("/status")
    val status: Observable<ResponseBody>
}
