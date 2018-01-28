package com.shwifty.tex.views.showtorrent.mvp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.views.base.mvp.BaseActivity
import com.shwifty.tex.views.showtorrent.di.DaggerTorrentInfoComponent
import com.shwifty.tex.views.showtorrent.list.ShowTorrentPagerAdapter
import kotlinx.android.synthetic.main.activity_show_torrent.*
import javax.inject.Inject


/**
 * Created by arran on 7/05/2017.
 */
class TorrentInfoActivity : BaseActivity(), TorrentInfoContract.View {

    @Inject
    lateinit var presenter: TorrentInfoContract.Presenter

    companion object {
        val ARG_TORRENT_HASH = "arg_torrent_hash"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_torrent)
        DaggerTorrentInfoComponent.builder().tricklComponent(Trickl.tricklComponent).build().inject(this)
        presenter.attachView(this)
        presenter.setup(intent.extras)

        setSupportActionBar(showTorrentToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showTorrentToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        presenter.fetchTorrent()

        if (presenter.torrentName != null) {
            showTorrentLoadingText.text = getString(R.string.loading_torrent_info_for, presenter.torrentName)
        }
    }

    override fun dismiss() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_show_torrent, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        presenter.optionsItemSelected(item)
        return true
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

    override fun notifyTorrentDeleted() {
        presenter.torrentInfo?.let {
            Trickl.dialogManager.showDeleteTorrentDialog(this, it, {
                showError(R.string.error_deleting_torrent)
            })
        }
    }
}