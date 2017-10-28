package com.shwifty.tex.views.browse.mvp

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 27/10/2017.
 */
class TorrentBrowsePresenter(private val torrentSearchRepository: ITorrentSearchRepository) : BasePresenter<TorrentBrowseContract.View>(), TorrentBrowseContract.Presenter {
    override fun load(sortType: TorrentSearchSortType, category: TorrentSearchCategory) {
        torrentSearchRepository.browse(sortType, 0, category)
                .subscribe(object : BaseSubscriber<List<TorrentSearchResult>>(){
                    override fun onNext(searchResults: List<TorrentSearchResult>) {
                        mvpView.showTorrents(searchResults)
                    }
                }).addSubscription()


    }
}