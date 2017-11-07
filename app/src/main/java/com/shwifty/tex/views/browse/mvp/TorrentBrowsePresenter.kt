package com.shwifty.tex.views.browse.mvp

import com.shwifty.tex.models.TorrentSearchCategory
import com.shwifty.tex.models.TorrentSearchResult
import com.shwifty.tex.models.TorrentSearchSortType
import com.shwifty.tex.repository.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.base.BasePresenter
import com.shwifty.tex.views.browse.state.BrowseViewEvents

/**
 * Created by arran on 27/10/2017.
 */
class TorrentBrowsePresenter(private val torrentSearchRepository: ITorrentSearchRepository) : BasePresenter<TorrentBrowseContract.View>(), TorrentBrowseContract.Presenter {
    override fun load(sortType: TorrentSearchSortType, category: TorrentSearchCategory) {
        subscriptions.clear()
        torrentSearchRepository.browse(sortType, 0, category)
                .subscribe(object : BaseSubscriber<List<TorrentSearchResult>>(){
                    override fun onNext(searchResults: List<TorrentSearchResult>) {
                        mvpView.browseReducer.reduce(BrowseViewEvents.UpdateBrowseResults(searchResults))
                    }
                }).addSubscription()
    }

    override fun search(query: String) {
        subscriptions.clear()
        torrentSearchRepository.search(query, TorrentSearchSortType.DEFAULT, 0, TorrentSearchCategory.All)
                .subscribe(object : BaseSubscriber<List<TorrentSearchResult>>(){
                    override fun onNext(searchResults: List<TorrentSearchResult>) {
                        mvpView.browseReducer.reduce(BrowseViewEvents.UpdateSearchResults(searchResults))
                    }
                }).addSubscription()
    }
}