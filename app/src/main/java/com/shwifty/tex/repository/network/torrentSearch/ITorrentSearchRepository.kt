package com.shwifty.tex.repository.network.torrentSearch

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import io.reactivex.Observable

/**
 * Created by arran on 27/10/2017.
 */
val BROWSE_FIRST_PAGE: Int = 1

interface ITorrentSearchRepository {

    fun search(searchTerm: String,
               sortType: TorrentSearchSortType,
               pageNumber: Int,
               category: TorrentSearchCategory): Observable<List<TorrentSearchResult>>

    fun browse(sortType: TorrentSearchSortType,
               pageNumber: Int,
               category: TorrentSearchCategory): Observable<List<TorrentSearchResult>>
}