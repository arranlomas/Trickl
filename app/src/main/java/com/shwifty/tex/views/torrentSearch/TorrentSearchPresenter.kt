package com.shwifty.tex.views.torrentSearch

import com.shwifty.tex.network.torrentSearch.ITorrentSearchRepository
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 27/10/2017.
 */
class TorrentSearchPresenter(private val torrentSearchRepository: ITorrentSearchRepository) : BasePresenter<TorrentSearchContract.View>(), TorrentSearchContract.Presenter {

}