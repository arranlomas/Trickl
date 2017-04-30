package com.schiwfty.tex

import android.content.Context
import com.schiwfty.tex.confluence.Confluence
import com.schiwfty.tex.dagger.context.ContextModule
import com.schiwfty.tex.dagger.network.DaggerNetworkComponent
import com.schiwfty.tex.dagger.network.NetworkComponent
import com.schiwfty.tex.dagger.network.NetworkModule
import com.schiwfty.tex.utils.composeIo
import rx.subjects.PublishSubject

/**
 * Created by arran on 29/04/2017.
 */
object TricklComponent {
    lateinit var networkComponent: NetworkComponent
    var running = false
    var heartbeat: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    private val heartbeatThread = Thread({
        while (running) {
            networkComponent.getClientApi().getStatus()
                    .composeIo()
                    .subscribe({
                        heartbeat.onNext(true)
                    }, {
                        heartbeat.onNext(false)
                    })
            Thread.sleep(1000)
        }
    })

    fun install(context: Context) {
        networkComponent = DaggerNetworkComponent.builder()
                .networkModule(NetworkModule())
                .contextModule(ContextModule(context))
                .build()
        running = true
        heartbeatThread.start()
        Confluence.setClientAddress()
    }

    fun shutdown() {
        running = false
    }
}