package com.shwifty.tex.repository.network.di

import com.shwifty.tex.BuildConfig
import com.shwifty.tex.repository.network.torrentSearch.TorrentSearchApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by arran on 8/11/2017.
 */

@Module
class ApiModule {

    @Provides
    internal fun provideTorrentSearchApi(retrofit: Retrofit): TorrentSearchApi {
        return retrofit.create(TorrentSearchApi::class.java)
    }

    @Provides
    internal fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    @Provides
    internal fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS
        val client = OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

        return client
    }

    @Provides
    internal fun provideBaseUrl(): String {
        return "http://${BuildConfig.TorrentBrowseServerIP}/"
    }
}