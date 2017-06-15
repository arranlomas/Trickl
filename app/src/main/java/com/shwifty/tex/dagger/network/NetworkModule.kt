package com.shwifty.tex.dagger.network


import android.util.Log
import com.shwifty.tex.confluence.Confluence
import com.shwifty.tex.persistence.ITorrentPersistence
import com.shwifty.tex.persistence.TorrentPersistence
import com.shwifty.tex.repositories.ITorrentRepository
import com.shwifty.tex.repositories.TorrentRepository
import com.shwifty.tex.retrofit.ClientAPI
import com.shwifty.tex.retrofit.ConfluenceApi
import dagger.Module
import dagger.Provides
import io.realm.Realm
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.NetworkInterface
import java.util.*
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
        Confluence.localhostIP = getIPAddress(true)
        Confluence.daemonPort = "8080"
        Confluence.fullUrl = "http://${Confluence.localhostIP}${Confluence.daemonPort}"
        Log.v("IP", Confluence.localhostIP)
        Log.v("PORT", Confluence.daemonPort)
        Log.v("URL", Confluence.fullUrl)
        return Confluence.fullUrl
    }

    @Provides
    internal fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }


    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0

                        if (useIPv4) {
                            if (isIPv4)
                            return "$sAddr:"
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) "${sAddr.toUpperCase()}:" else "${sAddr.substring(0, delim).toUpperCase()}:"
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }
}
