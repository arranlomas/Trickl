package com.schiwfty.tex.persistence

import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.realm.RealmTorrentFile
import com.schiwfty.tex.utils.mapToModel
import com.schiwfty.tex.utils.mapToRealm
import io.realm.Realm
import io.realm.RealmResults
import rx.Observable

/**
 * Created by arran on 19/05/2017.
 */
class TorrentPersistence : ITorrentPersistence {

    override fun getDownloadFiles(): List<TorrentFile>{
        val realm = Realm.getDefaultInstance()
        var realmResult = realm.where(RealmTorrentFile::class.java).findAll()
        val copy = realm.copyFromRealm(realmResult)

        val torrentFileList = mutableListOf<TorrentFile>()
        copy?.forEach { torrentFileList.add(it.mapToModel().copy()) }
        return torrentFileList.toList()
    }

    override fun getDownloadingFile(hash: String, path: String): TorrentFile {
        val realm = Realm.getDefaultInstance()
        val result: RealmResults<RealmTorrentFile> = realm.where(RealmTorrentFile::class.java).equalTo("primaryKey", hash + path).findAll()
        return realm.copyFromRealm(result).first().mapToModel()
    }

    override fun removeTorrentDownloadFile(downloadingFile: TorrentFile) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val result = realm.where(RealmTorrentFile::class.java).equalTo("primaryKey", downloadingFile.primaryKey).findFirst()
            result.deleteFromRealm()
        }
    }

    override fun saveTorrentFile(torrentFile: TorrentFile) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.insertOrUpdate(torrentFile.mapToRealm())
        }
    }

}