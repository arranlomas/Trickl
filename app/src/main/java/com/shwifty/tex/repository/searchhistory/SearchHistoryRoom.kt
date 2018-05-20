package com.shwifty.tex.repository.searchhistory

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.shwifty.tex.models.SearchHistoryItem

@Entity(tableName = "searchHistoryData")
data class SearchHistoryRoom(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name = "search_term") var searchTerm: String,
                       @ColumnInfo(name = "time") var time: Long

){
    constructor():this(null,"",0)
}

fun SearchHistoryItem.mapToRoom(): SearchHistoryRoom =
    SearchHistoryRoom(null, searchTerm, time)

fun SearchHistoryRoom.mapToModel(): SearchHistoryItem =
    SearchHistoryItem(searchTerm, time)