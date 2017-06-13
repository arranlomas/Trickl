package com.shwifty.tex.views.addtorrent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.shwifty.tex.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_add_torrent.*


/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentActivity : AppCompatActivity(), AddTorrentContract.View {

    lateinit var presenter: AddTorrentContract.Presenter

    companion object {
        val ADD_TORRENT_REQUEST_CODE = 101
        val ARG_ADD_TORRENT_RESULT = "arg_torrent_hash_result"
        val ARG_TORRENT_HASH = "arg_torrent_hash"
        val ARG_TORRENT_MAGNET = "arg_torrent_magnet"
        val ARG_TORRENT_FILE_PATH = "arg_torrent_file_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_torrent)
        presenter = AddTorrentPresenter()
        presenter.setup(this, this, intent.extras)
        presenter.fetchTorrent()

        addTorrentFab.setOnClickListener {
            val returnIntent = Intent()
            if(presenter.torrentHash!=null) returnIntent.putExtra(ARG_ADD_TORRENT_RESULT, presenter.torrentHash)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        setSupportActionBar(addTorrentToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.add_torrent_title)
        addTorrentToolbar.setNavigationOnClickListener{
            super.onBackPressed()
        }

        if(presenter.torrentName!=null){
            addTorrentLoadingText.text = getString(R.string.loading_torrent_info_for, presenter.torrentName)
        }
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
        addTorrentProgressBar.visibility = View.GONE
        addTorrentLoadingText.visibility = View.GONE
        addTorrentViewPager.visibility = View.VISIBLE
        addTorrentSmartTab.visibility = View.VISIBLE
        addTorrentFab.visibility = View.VISIBLE
        val adapter = AddTorrentPagerAdapter(supportFragmentManager, presenter.torrentHash)
        addTorrentViewPager.adapter = adapter
        addTorrentSmartTab.setViewPager(addTorrentViewPager)
    }
}