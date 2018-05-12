package com.shwifty.tex.repository.searchhistory

import android.support.annotation.NonNull
import com.shwifty.tex.models.SearchHistoryItem
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class SearchHistoryRealm(
    @NonNull
    @PrimaryKey
    var searchTerm: RealmString = RealmString(),

    @NonNull
    var time: Long
) : RealmObject()

open class RealmString(
    @PrimaryKey
    var value: String = ""
) : RealmObject()

fun SearchHistoryItem.mapToRealm(): SearchHistoryRealm =
    SearchHistoryRealm(RealmString(searchTearm), time)

fun SearchHistoryRealm.mapToModel(): SearchHistoryItem =
    SearchHistoryItem(searchTerm.value, time)