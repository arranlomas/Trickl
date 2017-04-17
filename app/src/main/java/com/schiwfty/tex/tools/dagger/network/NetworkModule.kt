package com.schiwfty.tex.tools.dagger.network

import android.content.Context


import com.readystatesoftware.chuck.ChuckInterceptor
import com.schiwfty.tex.tools.Constants
import com.schiwfty.tex.tools.retrofit.ClientAPI

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

/**
 * Created by arran on 15/02/2017.
 */

@Module
class NetworkModule {

    @Provides
    @NetworkScope
    internal fun provideClientApi(retrofit: Retrofit): ClientAPI {
        return retrofit.create(ClientAPI::class.java)
    }

    @Provides
    @NetworkScope
    internal fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    @Provides
    @NetworkScope
    internal fun provideOkHttpClient(context: Context): OkHttpClient {
        val client = OkHttpClient.Builder()
                .addInterceptor(ChuckInterceptor(context))
                .build()


        return client
    }

    @Provides
    @NetworkScope
    internal fun provideBaseUrl(): String {
        return Constants.fullUrl
    }

}
