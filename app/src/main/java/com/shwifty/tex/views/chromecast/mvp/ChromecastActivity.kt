package com.shwifty.tex.views.chromecast.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.SeekBar
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

        chromecastPlaybackButton.setOnClickListener {
            presenter.togglePlayback()
        }

        chromecastSeekbar.setOnSeekBarChangeListener(seeckBarChangedListener)
    }

    override fun updatePlayPauseButton(state: CastHandler.PlayerState) {
        when (state) {
            CastHandler.PlayerState.PLAYING -> setPlaybackButtonDrawable(R.drawable.ic_pause_circle_outline_accent)
            CastHandler.PlayerState.PAUSED -> setPlaybackButtonDrawable(R.drawable.ic_play_circle_outline_accent)
            else -> {
                chromecastPlaybackButton.visibility = View.GONE
                chromecastPlaybackSpinner.visibility = View.VISIBLE
            }
        }
    }

    private fun setPlaybackButtonDrawable(drawableResId: Int) {
        chromecastPlaybackButton.visibility = View.VISIBLE
        chromecastPlaybackSpinner.visibility = View.GONE
        chromecastPlaybackButton.setImageDrawable(ResourcesCompat.getDrawable(resources, drawableResId, null))
    }

    override fun updateProgress(position: Long, duration: Long) {
        chromecastSeekbar.setOnSeekBarChangeListener(null)
        chromecastSeekbar.max = duration.toInt()
        chromecastSeekbar.progress = position.toInt()
        chromecastSeekbar.setOnSeekBarChangeListener(seeckBarChangedListener)
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

    val seeckBarChangedListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, positions: Int, changed: Boolean) {
            presenter.seek(positions.toLong())
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }
}