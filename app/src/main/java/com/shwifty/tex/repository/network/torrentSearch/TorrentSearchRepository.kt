package com.shwifty.tex.repository.network.torrentSearch

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.utils.composeIo
import io.reactivex.Observable

/**
 * Created by arran on 27/10/2017.
 */
internal class TorrentSearchRepository(private val torrentSearchApi: TorrentSearchApi) : ITorrentSearchRepository {
    override fun search(searchTerm: String, sortType: TorrentSearchSortType, pageNumber: Int, category: TorrentSearchCategory): Observable<List<TorrentSearchResult>> {
        return torrentSearchApi.search(searchTerm, sortType, pageNumber, category)
                .composeIo()
    }

    override fun browse(sortType: TorrentSearchSortType, pageNumber: Int, category: TorrentSearchCategory): Observable<List<TorrentSearchResult>> {
        return torrentSearchApi.browse(sortType, pageNumber, category)
                .composeIo()
    }
}