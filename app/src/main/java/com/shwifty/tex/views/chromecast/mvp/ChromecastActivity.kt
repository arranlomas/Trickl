package com.shwifty.tex.views.chromecast.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.chromecast.CastHandler
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
        presenter.setup()

        setSupportActionBar(chromecastToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.chromecast_toolbar_title)
        chromecastToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        MyApplication.castHandler.initializeCastContext(this)
        MyApplication.castHandler.addSessionListener()

        playbackButton.setOnClickListener {
            presenter.togglePlayback()
        }
    }

    override fun updatePlayPauseButton(state: CastHandler.PlayerState) {
        when(state){
            CastHandler.PlayerState.PLAYING -> setPlaybackButtonDrawable(R.drawable.ic_pause_circle_outline_accent_24dp)
            CastHandler.PlayerState.PAUSED -> setPlaybackButtonDrawable(R.drawable.ic_play_circle_outline_accent_24dp)
            else-> {
                playbackButton.visibility = View.GONE
                playbackSpinner.visibility = View.VISIBLE
            }
        }
    }

    private fun setPlaybackButtonDrawable(drawableResId: Int){
        playbackButton.visibility = View.VISIBLE
        playbackSpinner.visibility = View.GONE
        playbackButton.setImageDrawable(ResourcesCompat.getDrawable(resources, drawableResId, null) )
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