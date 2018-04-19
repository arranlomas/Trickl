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
    private val results: List<TorrentSearchResult>

    init {
        val mutable = mutableListOf<TorrentSearchResult>()
        (0..100)
            .map {
                mutable.add(TorrentSearchResult(name = "name $it"))
            }
        results = mutable.toList()
    }

    override fun search(searchTerm: String, sortType: TorrentSearchSortType, pageNumber: Int, category: TorrentSearchCategory): Observable<List<TorrentSearchResult>> {
        return torrentSearchApi.search(searchTerm, sortType, pageNumber, category)
            .map { it.filter { it.category != null } }
            .composeIo()
    }

    override fun browse(sortType: TorrentSearchSortType, pageNumber: Int, category: TorrentSearchCategory): Observable<List<TorrentSearchResult>> {
        val results = when (pageNumber) {
            0 -> results.subList(0, 10)
            1 -> results.subList(10, 20)
            2 -> results.subList(20, 30)
            3 -> results.subList(30, 40)
            4 -> results.subList(40, 50)
            else -> emptyList()
        }
//        return Observable.just(results)
//            .delay(2, TimeUnit.SECONDS)
        return torrentSearchApi.browse(sortType, pageNumber, category)
            .composeIo()
    }
}