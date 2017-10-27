package com.shwifty.tex.models

import com.google.gson.annotations.SerializedName

/**
 * Created by arran on 27/10/2017.
 */
data class TorrentSearchResult(
        @SerializedName("Name")
        var name: String? = null,
        @SerializedName("MAgnet")
        var magnet: String? = null,
        @SerializedName("Link")
        var link: String? = null,
        @SerializedName("Uploaded")
        var uploaded: String? = null,
        @SerializedName("Size")
        var size: String? = null,
        @SerializedName("Uled")
        var uled: String? = null,
        @SerializedName("Seeds")
        var seeds: Int = 0,
        @SerializedName("Leechers")
        var leechers: Int = 0,
        @SerializedName("CategoryParent")
        var categoryParent: String? = null,
        @SerializedName("Category")
        var category: String? = null,
        @SerializedName("ImdbID")
        var imdbID: String? = null,
        @SerializedName("CoverImage")
        var coverImage: String? = null
)