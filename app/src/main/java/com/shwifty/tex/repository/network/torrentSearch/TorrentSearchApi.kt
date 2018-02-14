package com.shwifty.tex.repository.network.torrentSearch

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by arran on 27/10/2017.
 */
internal interface TorrentSearchApi {
    @GET("/search")
    fun search(@Query("searchTerm") searchTerm: String,
               @Query("sortedBy") sortType: TorrentSearchSortType,
               @Query("pageNumber") pageNumber: Int,
               @Query("category") category: TorrentSearchCategory): Observable<List<TorrentSearchResult>>

    @GET("/browse")
    fun browse(@Query("sortedBy") sortType: TorrentSearchSortType,
               @Query("pageNumber") pageNumber: Int,
               @Query("category") category: TorrentSearchCategory): Observable<List<TorrentSearchResult>>
}