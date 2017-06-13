package com.schiwfty.tex.utils

import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.realm.RealmString
import com.schiwfty.tex.realm.RealmTorrentFile
import io.realm.RealmList

/**
 * Created by arran on 16/05/2017.
 */
fun RealmTorrentFile.mapToModel(): TorrentFile{
    val tf = TorrentFile(
            fileLength,
            fileDirs?.mapToList(),
            torrentHash,
            primaryKey
    )
    tf.parentTorrentName = parentTorrentName
    tf.percComplete = percComplete
    return tf
}
fun RealmList<RealmString>.mapToList(): List<String>{
    val mutableList = mutableListOf<String>()
    forEach { mutableList.add(it.mapToModel()) }
    return mutableList.toList()
}
fun RealmString.mapToModel(): String{
    return value ?: ""
}