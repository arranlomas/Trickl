package com.shwifty.tex.models

/**
 * Created by arran on 27/10/2017.
 */
enum class TorrentSearchSortType {
    NAME,
    NAME_DESCENDING,
    UPLOAD,
    UPLOAD_DESCENDING,
    SIZE,
    SIZE_DESCENDING,
    SEEDS,
    SEEDS_DESCENDING,
    LEECHERS,
    LEECHERS_DESCENDING,
    ULED_BY,
    ULED_BY_DESCENDING,
    TYPE,
    TYPE_DESCENDING,
    DEFAULT;

    fun toHumanFriendlyString(): String {
        return when (this){
            TorrentSearchSortType.NAME -> "Name"
            TorrentSearchSortType.NAME_DESCENDING -> "Name Descending"
            TorrentSearchSortType.UPLOAD -> "Upload Date"
            TorrentSearchSortType.UPLOAD_DESCENDING -> "Upload Date Descending"
            TorrentSearchSortType.SIZE -> "Size"
            TorrentSearchSortType.SIZE_DESCENDING -> "Size Descending"
            TorrentSearchSortType.SEEDS -> "Seeders"
            TorrentSearchSortType.SEEDS_DESCENDING -> "Seeders Descending"
            TorrentSearchSortType.LEECHERS -> "Leechers"
            TorrentSearchSortType.LEECHERS_DESCENDING -> "Leechers Descending"
            TorrentSearchSortType.ULED_BY -> "Uploader Name"
            TorrentSearchSortType.ULED_BY_DESCENDING -> "Uploader Name Descending"
            TorrentSearchSortType.TYPE -> "Type"
            TorrentSearchSortType.TYPE_DESCENDING -> "Type Descending"
            TorrentSearchSortType.DEFAULT -> "Default"
        }
    }
}