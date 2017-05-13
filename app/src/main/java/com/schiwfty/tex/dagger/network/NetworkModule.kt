package com.schiwfty.tex.dagger.network


import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.repositories.TorrentRepository
import com.schiwfty.tex.retrofit.ClientAPI
import com.schiwfty.tex.retrofit.ConfluenceApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by arran on 15/02/2017.
 */

@Module
class NetworkModule {

    @Provides
    @NetworkScope
    internal fun provideTorrentRepository(confluenceApi: ConfluenceApi): ITorrentRepository {
        return TorrentRepository(confluenceApi)

    }

    @Provides
    @NetworkScope
    internal fun providesHttpController(clientAPI: ClientAPI): ConfluenceApi {
        return ConfluenceApi(clientAPI)
    }

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
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    @Provides
    @NetworkScope
    internal fun provideOkHttpClient(context: Context): OkHttpClient {
        val client = OkHttpClient.Builder()
                .addInterceptor(ChuckInterceptor(context))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()


        return client
    }

    @Provides
    @NetworkScope
    internal fun provideBaseUrl(): String {
        return "http://127.0.0.1:8080"
    }

}
