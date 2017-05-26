package com.schiwfty.tex.persistence

import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.realm.RealmTorrentFile
import com.schiwfty.tex.utils.mapToModel
import com.schiwfty.tex.utils.mapToRealm
import io.realm.Realm
import io.realm.RealmResults
import rx.subjects.PublishSubject

/**
 * Created by arran on 19/05/2017.
 */
class TorrentPersistence : ITorrentPersistence {
    lateinit override var torrentFileAdded: (TorrentFile) -> Unit


    override fun getDownloadFiles(): List<TorrentFile> {
        val realm = Realm.getDefaultInstance()
        val torrentFileList = mutableListOf<TorrentFile>()
        try {
            realm.beginTransaction()
            val realmResult = realm.where(RealmTorrentFile::class.java).findAll()
            val copy = realm.copyFromRealm(realmResult)
            realm.commitTransaction()
            copy?.forEach { torrentFileList.add(it.mapToModel()) }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            realm.close()
        }

        return torrentFileList.toList()
    }

    override fun getDownloadingFile(hash: String, path: String): TorrentFile {
        val realm = Realm.getDefaultInstance()
        var torrentFile: TorrentFile? = null
        try {
            realm.beginTransaction()
            val result: RealmResults<RealmTorrentFile> = realm.where(RealmTorrentFile::class.java).equalTo("primaryKey", hash + path).findAll()
            torrentFile = realm.copyFromRealm(result).first().mapToModel()
            realm.commitTransaction()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            realm.close()
        }
        return torrentFile ?: throw NullPointerException("torrent file should not be empty")
    }

    override fun removeTorrentDownloadFile(torrentFile: TorrentFile) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.where(RealmTorrentFile::class.java).equalTo("primaryKey", torrentFile.primaryKey).findFirst()?.deleteFromRealm()
            torrentFileAdded(torrentFile)
        }
        realm.close()
    }

    override fun saveTorrentFile(torrentFile: TorrentFile) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            realm.insertOrUpdate(torrentFile.mapToRealm())
            torrentFileAdded(torrentFile)
        }
        realm.close()
    }

}