package com.shwifty.tex.views.base.mvp

import android.support.v4.app.Fragment
import com.arranlomas.daggerviewmodelhelper.Injectable
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Created by arran on 11/07/2017.
 */
open class BaseDaggerActivity : BaseActivity(), BaseContract.MvpView, HasSupportFragmentInjector, Injectable {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentDispatchingAndroidInjector
    }
}