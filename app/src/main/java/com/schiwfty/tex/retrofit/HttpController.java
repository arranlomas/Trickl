package com.schiwfty.tex.retrofit;


import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by arran on 4/02/2017.
 */


public class HttpController {
    private ClientAPI clientAPI;

    @Inject
    public HttpController(ClientAPI clientAPI) {
        this.clientAPI = clientAPI;
    }

    public Observable<ResponseBody> getInfo(String hash) {
        return clientAPI.getInfo(hash);
    }

    public Observable<ResponseBody> postTorrent(String hash, byte[] bencode) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),bencode);
        return clientAPI.postTorrent(hash, requestBody);
    }

    public Observable <ResponseBody> getStatus() {
        return clientAPI.getStatus();
    }


}