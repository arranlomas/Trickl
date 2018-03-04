package com.shwifty.tex.views.showtorrent.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.shwifty.tex.R
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.views.base.mvp.BaseDaggerActivity
import com.shwifty.tex.views.showtorrent.list.ShowTorrentPagerAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_show_torrent.*
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentInfoActivity : BaseDaggerActivity(), TorrentInfoContract.View {

    @Inject
    lateinit var presenter: TorrentInfoContract.Presenter

    @Inject
    lateinit var dialogManager: IDialogManager

    companion object {
        val ARG_TORRENT_HASH = "arg_torrent_hash"

        fun open(context: Context, infoHash: String) {
            val intent = Intent(context, TorrentInfoActivity::class.java)
            intent.putExtra(TorrentInfoActivity.ARG_TORRENT_HASH, infoHash)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_torrent)
        AndroidInjection.inject(this)
//        DaggerTorrentInfoComponent.builder().tricklComponent(Trickl.tricklComponent).build().inject(this)
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
            dialogManager.showDeleteTorrentDialog(this, it, {
                showError(R.string.error_deleting_torrent)
            })
        }
    }
}