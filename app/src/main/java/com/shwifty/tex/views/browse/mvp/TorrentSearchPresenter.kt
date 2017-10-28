package com.shwifty.tex.views.browse.mvp

import com.shwifty.tex.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 27/10/2017.
 */
class TorrentBrowsePresenter(private val torrentSearchRepository: ITorrentSearchRepository) : BasePresenter<TorrentBrowseContract.View>(), TorrentBrowseContract.Presenter {
//    override fun load() {
//        torrentSearchRepository.search(query, TorrentSearchSortType.DEFAULT, 0, TorrentSearchCategory.All)
//                .subscribe(object : BaseSubscriber<List<TorrentSearchResult>>(){
//                    override fun onNext(searchResults: List<TorrentSearchResult>) {
//                        mvpView.showTorrents(searchResults)
//                    }
//                }).addSubscription()
//    }

}