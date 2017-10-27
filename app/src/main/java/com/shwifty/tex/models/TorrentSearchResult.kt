package com.shwifty.tex.models

import com.google.gson.annotations.SerializedName

/**
 * Created by arran on 27/10/2017.
 */
data class TorrentSearchResult(
        @SerializedName("Name")
        val name: String? = null,
        @SerializedName("Magnet")
        val magnet: String? = null,
        @SerializedName("Link")
        val link: String? = null,
        @SerializedName("Uploaded")
        val uploaded: String? = null,
        @SerializedName("Size")
        val size: String? = null,
        @SerializedName("Uled")
        val uled: String? = null,
        @SerializedName("Seeds")
        val seeds: Int = 0,
        @SerializedName("Leechers")
        val leechers: Int = 0,
        @SerializedName("CategoryParent")
        val categoryParent: String? = null,
        @SerializedName("Category")
        val category: String? = null
)