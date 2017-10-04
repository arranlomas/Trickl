package com.shwifty.tex.views.chromecast.mvp

import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.shwifty.tex.views.base.BasePresenter

/**
 * Created by arran on 7/05/2017.
 */
class ChromecastPresenter(val torrentRepository: ITorrentRepository) : BasePresenter<ChromecastContract.View>(), ChromecastContract.Presenter {
}