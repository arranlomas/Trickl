package com.shwifty.tex.views.chromecast.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.base.BaseActivity
import com.shwifty.tex.views.chromecast.di.DaggerChromecastComponent
import kotlinx.android.synthetic.main.activity_chromecast.*
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class ChromecastActivity : BaseActivity(), ChromecastContract.View {

    @Inject
    lateinit var presenter: ChromecastContract.Presenter

    companion object {
        fun startActivity(context: Context) {
            val chromecastIntent = Intent(context, ChromecastActivity::class.java)
            context.startActivity(chromecastIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chromecast)
        DaggerChromecastComponent.builder().torrentRepositoryComponent(TricklComponent.torrentRepositoryComponent).build().inject(this)
        presenter.attachView(this)

        setSupportActionBar(chromecastToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.chromecast_toolbar_title)
        chromecastToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        MyApplication.castHandler.initializeCastContext(this)
        MyApplication.castHandler.addSessionListener()
        MyApplication.castHandler.getApproximatePosition()
    }


    override fun onResume() {
        MyApplication.castHandler.addSessionListener()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        MyApplication.castHandler.removeSessionListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}