package com.schiwfty.tex.retrofit;

/**
 * Created by arran on 4/02/2017.
 */


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


public interface ClientAPI {

    @POST("/metainfo")
    Observable<ResponseBody> postTorrent(@Query("ih") String info_hash, @Body RequestBody bencodedBody);

    @GET("/info")
    Observable<ResponseBody> getInfo(@Query("ih") String info_hash);

    @GET("/status")
    Observable<ResponseBody> getStatus();
}
