package com.shwifty.tex.utils

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import io.reactivex.Observable

fun ITorrentRepository.torrentAlreadyExists(torrentHash: String): Observable<Boolean>{
    var alreadyExists = false
    this.getAllTorrentsFromStorage()
            .map { results ->
                results.forEach { result ->
                    result.unwrapIfSuccess {
                        if (it.info_hash == torrentHash) alreadyExists = true
                    } ?: result.logTorrentParseError()
                }
                alreadyExists
            }
    return Observable.just(alreadyExists)
}