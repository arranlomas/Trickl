package com.schiwfty.tex.confluence

import com.schiwfty.tex.repositories.ITorrentRepository
import rx.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created by arran on 30/04/2017.
 */
class ConfluenceHeartbeat{
    var isAlive: Boolean = false
    var heartbeatObservable: PublishSubject<Boolean> = PublishSubject.create<Boolean>()
    private var running = false
    @Inject
    lateinit var torrentRepository: ITorrentRepository
    private lateinit var heartbeatThread: Thread

    fun setup() {
        heartbeatThread = Thread({
            while (running) {
                torrentRepository.getStatus()
                        .subscribe({
                            isAlive = true
                            heartbeatObservable.onNext(true)
                        }, {
                            isAlive = false
                            heartbeatObservable.onNext(false)
                        })
                Thread.sleep(1000)
            }
        })
    }

    fun start() {
        running = true
        heartbeatThread.start()
    }

    fun stop() {
        running = false
    }
}