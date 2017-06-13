package com.shwifty.tex.utils

import com.shwifty.tex.models.TorrentFile
import com.shwifty.tex.realm.RealmString
import com.shwifty.tex.realm.RealmTorrentFile
import io.realm.RealmList

/**
 * Created by arran on 16/05/2017.
 */
@JvmName("mapTorrentFileListToRealm")
fun List<TorrentFile>.mapToRealm(): RealmList<RealmTorrentFile> {
    val realmList = RealmList<RealmTorrentFile>()
    forEach { realmList.add(it.mapToRealm()) }
    return realmList
}

fun TorrentFile.mapToRealm(): RealmTorrentFile {
    val realmTorrentFile = RealmTorrentFile(
            fileLength ?: 0,
            fileDirs?.mapToRealm(),
            torrentHash,
            primaryKey,
            percComplete,
            parentTorrentName
    )
    return realmTorrentFile
}

@JvmName("mapStringListToRealm")
fun List<String>.mapToRealm(): RealmList<RealmString> {
    val realmList = RealmList<RealmString>()
    this.forEach { realmList.add(it.mapToRealm()) }
    return realmList
}

fun String.mapToRealm(): RealmString{
    val realmString = RealmString(
            this
    )
    return realmString
}