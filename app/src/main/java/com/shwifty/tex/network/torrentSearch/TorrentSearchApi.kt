package com.shwifty.tex.network.torrentSearch

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by arran on 27/10/2017.
 */
internal interface TorrentSearchApi {
    @GET("/search")
    fun search(@Query("searchTerm") searchTerm: String,
               @Query("sortedBy") sortType: TorrentSearchSortType,
               @Query("pageNumber") pageNumber: Int,
               @Query("category") category: TorrentSearchCategory): Observable<List<TorrentSearchResult>>
}