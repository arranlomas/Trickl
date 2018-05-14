package com.shwifty.tex.repository.searchhistory

import android.support.annotation.NonNull
import com.shwifty.tex.models.SearchHistoryItem
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SearchHistoryRealm(
    @NonNull
    var searchTerm: RealmString = RealmString(),

    @NonNull
    var time: Long = -1
) : RealmObject()

open class RealmString(
    @PrimaryKey
    var value: String = ""
) : RealmObject()

fun SearchHistoryItem.mapToRealm(): SearchHistoryRealm =
    SearchHistoryRealm(RealmString(searchTerm), time)

fun SearchHistoryRealm.mapToModel(): SearchHistoryItem =
    SearchHistoryItem(searchTerm.value, time)