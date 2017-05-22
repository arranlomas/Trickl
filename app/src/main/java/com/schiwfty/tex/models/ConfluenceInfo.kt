package com.schiwfty.tex.models

import com.google.gson.annotations.SerializedName

/**
 * Created by arran on 13/05/2017.
 */
data class ConfluenceInfo(
        @SerializedName("addr")
        var address: String,

        @SerializedName("peerId")
        var peerId: String,

        @SerializedName("bannedIps")
        var bannedIps: Int,

        @SerializedName("dhtNodes")
        var dhtInfo: DhtInfo,

        @SerializedName("dhtServerId")
        var dhtServerId: String,

        @SerializedName("dhtPort")
        var dhtPort: String,

        @SerializedName("dhtAnnounces")
        var dhtAnnounces: String,

        @SerializedName("outstandingTransactions")
        var outstandingTransactions: String,

        @SerializedName("torrentCount")
        var torrentCount: String,

        @SerializedName("torrentList")
        var torrentList: List<ConfluenceTorrentInfo>
)

data class DhtInfo(
        @SerializedName("total")
        var totalNodes: Int,

        @SerializedName("good")
        var goodNodes: Int,

        @SerializedName("bad")
        var badNodes: Int
)

data class ConfluenceTorrentInfo(
        @SerializedName("name")
        var name: String,

        @SerializedName("percComplete")
        var percComplete: Float,

        @SerializedName("infoHash")
        var infoHash: String,

        @SerializedName("metadatLength")
        var metadatLength: Int,

        @SerializedName("pieceLength")
        var pieceLength: Int,

        @SerializedName("numPieces")
        var numPieces: Int
)

data class FileStatePiece(
        @SerializedName("Bytes")
        var bytes: Int,

        @SerializedName("Priority")
        var priority: Int,

        @SerializedName("Complete")
        var complete: Boolean,

        @SerializedName("Checking")
        var checking: Boolean,

        @SerializedName("Partial")
        var partial: Boolean,

        var torrentHash: String,

        var filePath: String
)