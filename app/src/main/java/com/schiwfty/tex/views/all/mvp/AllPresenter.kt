package com.schiwfty.tex.views.all.mvp

import android.content.Context
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.repositories.ITorrentRepository
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by arran on 16/04/2017.
 */
class AllPresenter : AllContract.Presenter {


    @Inject
    lateinit var torrentRepository: ITorrentRepository

    private val compositeSubscription = CompositeSubscription()

    lateinit var view: AllContract.View
    lateinit var context: Context

    override fun setup(context: Context, view: AllContract.View) {
        this.view = view
        this.context = context
        TricklComponent.mainComponent.inject(this)

        compositeSubscription.add(
                torrentRepository.torrentInfoDeleteListener
                        .subscribe({refresh()}, {it.printStackTrace()})
        )
    }

    override fun destroy() {
        compositeSubscription.unsubscribe()
    }

    override fun refresh() {
         torrentRepository.getAllTorrentsFromStorage()
                .subscribe({
                    view.showAllTorrents(it)
                }, {
                    it.printStackTrace()
                })
    }
}