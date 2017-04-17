package com.schiwfty.tex.splash

import android.content.Context
import com.schiwfty.tex.R
import com.schiwfty.tex.confluence.Confluence
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber

/**
 * Created by arran on 16/04/2017.
 */
class SplashPresenter : SplashContract.Presenter {
    lateinit var view: SplashContract.View

    override fun setClienctAddress() {
        Confluence.setClientAddress()
    }

    override fun setup(view: SplashContract.View) {
        this.view = view
    }

    override fun setupConfluenceAsset(context: Context) {
        Confluence.getSetupObservable(context)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if(it) view.showSuccess(R.string.splash_start_confluence_error)
                    else  view.showError(R.string.splash_copy_confluence_error)
                }, {
                    view.showError(R.string.splash_copy_confluence_error)
                })
    }


}