package com.schiwfty.tex.retrofit

/**
 * Created by arran on 4/02/2017.
 */


import com.schiwfty.tex.models.ConfluenceInfo
import com.schiwfty.tex.models.FileState
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import rx.Observable


interface ClientAPI {

    @POST("/metainfo")
    fun postTorrent(@Query("ih") info_hash: String, @Body bencodedBody: RequestBody): Observable<ResponseBody>

    @GET("/info")
    fun getInfo(@Query("ih") info_hash: String): Observable<ResponseBody>

    @GET("/status")
    fun getStatus(): Observable<ConfluenceInfo>

    @Streaming
    @GET("/data")
    fun getData(@Query("ih") info_hash: String, @Query("path") file_path: String): Observable<ResponseBody>

    @GET("/fileState")
    fun getFileState(@Query("ih") info_hash: String, @Query("path") file_path: String): Observable<FileState>

}
