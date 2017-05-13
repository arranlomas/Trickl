package com.schiwfty.tex.views.showtorrent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.schiwfty.tex.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_show_torrent.*


/**
 * Created by arran on 7/05/2017.
 */
class ShowTorrentActivity : AppCompatActivity(), ShowTorrentContract.View {


    lateinit var presenter: ShowTorrentContract.Presenter

    companion object {
        val ARG_TORRENT_HASH = "arg_torrent_hash"
        val ARG_TORRENT_MAGNET = "arg_torrent_magnet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_torrent)
        presenter = ShowTorrentPresenter()
        presenter.setup(this, this, intent.extras)

        setSupportActionBar(showTorrentToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        showTorrentToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        presenter.fetchTorrent()

        if (presenter.torrentName != null) {
            showTorrentLoadingText.text = getString(R.string.loading_torrent_info_for, presenter.torrentName)
        }
    }

    override fun showError(stringId: Int) {
        Toasty.error(this, getString(stringId))
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(this, getString(stringId))
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(this, getString(stringId))
    }

    override fun notifyTorrentAdded() {
        supportActionBar?.title = presenter.torrentName
        showTorrentProgressBar.visibility = View.GONE
        showTorrentLoadingText.visibility = View.GONE
        showTorrentViewPager.visibility = View.VISIBLE
        showTorrentSmartTab.visibility = View.VISIBLE
        val adapter = ShowTorrentPagerAdapter(supportFragmentManager, presenter.torrentHash)
        showTorrentViewPager.adapter = adapter
        showTorrentSmartTab.setViewPager(showTorrentViewPager)
    }
}