package com.shwifty.tex.network.torrentSearch

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import rx.Observable

/**
 * Created by arran on 27/10/2017.
 */
interface ITorrentSearchRepository {
    fun search(searchTerm: String,
               sortType: TorrentSearchSortType,
               pageNumber: Int,
               category: TorrentSearchCategory): Observable<List<TorrentSearchResult>>
}