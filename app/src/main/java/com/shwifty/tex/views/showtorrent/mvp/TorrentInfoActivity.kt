package com.shwifty.tex.views.showtorrent.mvp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.base.BaseActivity
import com.shwifty.tex.views.showtorrent.list.ShowTorrentPagerAdapter
import es.dmoral.toasty.Toasty


/**
 * Created by arran on 7/05/2017.
 */
class TorrentInfoActivity : BaseActivity(), com.shwifty.tex.views.showtorrent.TorrentInfoContract.View {

    lateinit var presenter: com.shwifty.tex.views.showtorrent.TorrentInfoContract.Presenter

    companion object {
        val ARG_TORRENT_HASH = "arg_torrent_hash"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_torrent)
        presenter = com.shwifty.tex.views.showtorrent.TorrentInfoPresenter()
        presenter.attachView(this)
        presenter.setup(intent.extras)

        setSupportActionBar(kotlinx.android.synthetic.main.activity_show_torrent.showTorrentToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        kotlinx.android.synthetic.main.activity_show_torrent.showTorrentToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        presenter.fetchTorrent()

        if (presenter.torrentName != null) {
            kotlinx.android.synthetic.main.activity_show_torrent.showTorrentLoadingText.text = getString(R.string.loading_torrent_info_for, presenter.torrentName)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let { presenter.optionsItemSelected(it) }
        return true
    }


    override fun showError(stringId: Int) {
        Toasty.error(this, getString(stringId)).show()
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(this, getString(stringId)).show()
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(this, getString(stringId)).show()
    }

    override fun notifyTorrentAdded() {
        supportActionBar?.title = presenter.torrentName
        kotlinx.android.synthetic.main.activity_show_torrent.showTorrentProgressBar.visibility = View.GONE
        kotlinx.android.synthetic.main.activity_show_torrent.showTorrentLoadingText.visibility = View.GONE
        kotlinx.android.synthetic.main.activity_show_torrent.showTorrentViewPager.visibility = View.VISIBLE
        kotlinx.android.synthetic.main.activity_show_torrent.showTorrentSmartTab.visibility = View.VISIBLE
        val adapter = ShowTorrentPagerAdapter(supportFragmentManager, presenter.torrentHash)
        kotlinx.android.synthetic.main.activity_show_torrent.showTorrentViewPager.adapter = adapter
        kotlinx.android.synthetic.main.activity_show_torrent.showTorrentSmartTab.setViewPager(kotlinx.android.synthetic.main.activity_show_torrent.showTorrentViewPager)
    }

    override fun notifyTorrentDeleted() {
        presenter.torrentInfo?.let {
            TricklComponent.dialogManager.showDeleteTorrentDialog(this, it, {
                showError(R.string.error_deleting_torrent)
            })
        }
    }
}