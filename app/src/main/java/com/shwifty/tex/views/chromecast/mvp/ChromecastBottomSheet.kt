package com.shwifty.tex.views.chromecast.mvp

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.chromecast.CastHandler
import com.shwifty.tex.views.base.BaseNestedScrollView
import com.shwifty.tex.views.chromecast.di.DaggerChromecastComponent
import kotlinx.android.synthetic.main.bottom_sheet_chromecast_full.view.*
import kotlinx.android.synthetic.main.bottom_sheet_chromecast_peek.view.*
import kotlinx.android.synthetic.main.bottom_sheet_chromecast_view.view.*
import kotlinx.android.synthetic.main.bottom_sheet_main_activity.*
import javax.inject.Inject


/**
 * Created by arran on 7/05/2017.
 */
class ChromecastBottomSheet : BaseNestedScrollView, ChromecastContract.View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        View.inflate(context, R.layout.bottom_sheet_chromecast_view, this)
    }

    @Inject
    lateinit var presenter: ChromecastContract.Presenter

    lateinit var chromecastFull: View
    lateinit var chromecastPeek: View

    override fun onFinishInflate() {
        super.onFinishInflate()
        DaggerChromecastComponent.builder().torrentRepositoryComponent(TricklComponent.torrentRepositoryComponent).build().inject(this)
        presenter.attachView(this)
        presenter.setup()

        MyApplication.castHandler.initializeCastContext(context)
        MyApplication.castHandler.addSessionListener()
        chromecastFull = bottom_sheet_layout_full
        chromecastPeek = bottom_sheet_layout_peek


        chromecastPlaybackButtonFull.setOnClickListener {
            presenter.togglePlayback()
        }
        chromecastPlaybackButtonPeek.setOnClickListener {
            presenter.togglePlayback()
        }

        chromecastSeekbarFull.setOnSeekBarChangeListener(seeckBarChangedListener)
    }

    override fun setTitle(title: String) {
        chromecastTitleFull.text = title
        chromecastTitlePeek.text = title
    }

    fun notifyBottomSheetStateChanged(newState: Int) {
        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
            bottom_sheet_layout_full.visibility = View.VISIBLE
            bottom_sheet_layout_peek.visibility = View.GONE
        } else {
            bottom_sheet_layout_full.visibility = View.GONE
            bottom_sheet_layout_peek.visibility = View.VISIBLE
        }
    }


    override fun updatePlayPauseButton(state: CastHandler.PlayerState) {
        when (state) {
            CastHandler.PlayerState.PLAYING -> setPlaybackButtonDrawable(R.drawable.ic_pause_circle_outline_accent)
            CastHandler.PlayerState.PAUSED -> setPlaybackButtonDrawable(R.drawable.ic_play_circle_outline_accent)
            else -> {
                chromecastPlaybackButtonFull.visibility = View.GONE
                chromecastPlaybackSpinnerFull.visibility = View.VISIBLE
                chromecastPlaybackButtonPeek.visibility = View.GONE
                chromecastPlaybackSpinnerPeek.visibility = View.VISIBLE
            }
        }
    }

    private fun setPlaybackButtonDrawable(drawableResId: Int) {
        chromecastPlaybackButtonFull.visibility = View.VISIBLE
        chromecastPlaybackSpinnerFull.visibility = View.GONE
        chromecastPlaybackButtonFull.setImageDrawable(ResourcesCompat.getDrawable(resources, drawableResId, null))

        chromecastPlaybackButtonPeek.visibility = View.VISIBLE
        chromecastPlaybackSpinnerPeek.visibility = View.GONE
        chromecastPlaybackButtonPeek.setImageDrawable(ResourcesCompat.getDrawable(resources, drawableResId, null))
    }

    override fun updateProgress(position: Long, duration: Long) {
        chromecastSeekbarFull.setOnSeekBarChangeListener(null)
        chromecastSeekbarFull.max = duration.toInt()
        chromecastSeekbarFull.progress = position.toInt()
        chromecastSeekbarFull.setOnSeekBarChangeListener(seeckBarChangedListener)
    }

    fun onDestroy() {
        presenter.detachView()
    }

    fun onResume() {
        MyApplication.castHandler.addSessionListener()
    }

    fun onPause() {
        MyApplication.castHandler.removeSessionListener()
    }

    val seeckBarChangedListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, positions: Int, changed: Boolean) {
            presenter.seek(positions.toLong())
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }
}