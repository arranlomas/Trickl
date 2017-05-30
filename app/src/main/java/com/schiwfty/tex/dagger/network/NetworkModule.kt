package com.schiwfty.tex.dagger.network


import android.util.Log
import com.schiwfty.tex.persistence.ITorrentPersistence
import com.schiwfty.tex.persistence.TorrentPersistence
import com.schiwfty.tex.repositories.ITorrentRepository
import com.schiwfty.tex.repositories.TorrentRepository
import com.schiwfty.tex.retrofit.ClientAPI
import com.schiwfty.tex.retrofit.ConfluenceApi
import com.schiwfty.tex.utils.getIPAddress
import dagger.Module
import dagger.Provides
import io.realm.Realm
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by arran on 15/02/2017.
 */

@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideTorrentRepository(confluenceApi: ConfluenceApi, torrentPersistence: ITorrentPersistence): ITorrentRepository {
        return TorrentRepository(confluenceApi, torrentPersistence)

    }

    @Provides
    internal fun provideTorrentPersistence(): ITorrentPersistence {
        return TorrentPersistence()

    }

    @Provides
    internal fun providesHttpController(clientAPI: ClientAPI): ConfluenceApi {
        return ConfluenceApi(clientAPI)
    }

    @Provides
    internal fun provideClientApi(retrofit: Retrofit): ClientAPI {
        return retrofit.create(ClientAPI::class.java)
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
        val ip = getIPAddress()
        Log.v("IP", ip)
        return "http://$ip:8080"
    }

    @Provides
    internal fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

}
