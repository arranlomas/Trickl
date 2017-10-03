package com.shwifty.tex.views.addtorrent.mvp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.shwifty.tex.R
import com.shwifty.tex.views.addtorrent.list.AddTorrentPagerAdapter
import com.shwifty.tex.views.base.BaseActivity


/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentActivity : BaseActivity(), com.shwifty.tex.views.addtorrent.AddTorrentContract.View {

    lateinit var presenter: com.shwifty.tex.views.addtorrent.AddTorrentContract.Presenter

    companion object {
        val ARG_ADD_TORRENT_RESULT = "arg_torrent_hash_result"
        val ARG_TORRENT_HASH = "arg_torrent_hash"
        val ARG_TORRENT_MAGNET = "arg_torrent_magnet"
        val ARG_TORRENT_FILE_PATH = "arg_torrent_file_path"

        fun startActivity(context: Context, hash: String?, magnet: String?, torrentFilePath: String?){
            val addTorrentIntent = Intent(context, AddTorrentActivity::class.java)
            if (hash != null) addTorrentIntent.putExtra(ARG_TORRENT_HASH, hash)
            if (magnet != null) addTorrentIntent.putExtra(ARG_TORRENT_MAGNET, magnet)
            if (torrentFilePath != null) addTorrentIntent.putExtra(ARG_TORRENT_FILE_PATH, torrentFilePath)
            context.startActivity(addTorrentIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_torrent)
        presenter = com.shwifty.tex.views.addtorrent.AddTorrentPresenter()
        presenter.attachView(this)
        presenter.setup(intent.extras)
        presenter.fetchTorrent()

        kotlinx.android.synthetic.main.activity_add_torrent.addTorrentFab.setOnClickListener {
            val returnIntent = Intent()
            if(presenter.torrentHash!=null) returnIntent.putExtra(ARG_ADD_TORRENT_RESULT, presenter.torrentHash)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        setSupportActionBar(kotlinx.android.synthetic.main.activity_add_torrent.addTorrentToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.add_torrent_title)
        kotlinx.android.synthetic.main.activity_add_torrent.addTorrentToolbar.setNavigationOnClickListener{
            super.onBackPressed()
        }

        if(presenter.torrentName!=null){
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentLoadingText.text = getString(R.string.loading_torrent_info_for, presenter.torrentName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun notifyTorrentAdded() {
        val adapter = AddTorrentPagerAdapter(supportFragmentManager, presenter.torrentHash)
        kotlinx.android.synthetic.main.activity_add_torrent.addTorrentViewPager.adapter = adapter
        kotlinx.android.synthetic.main.activity_add_torrent.addTorrentSmartTab.setViewPager(kotlinx.android.synthetic.main.activity_add_torrent.addTorrentViewPager)
    }

    override fun setLoading(loading: Boolean) {
        if(loading){
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentProgressBar.visibility = View.VISIBLE
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentLoadingText.visibility = View.VISIBLE
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentViewPager.visibility = View.GONE
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentSmartTab.visibility = View.GONE
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentFab.visibility = View.GONE
        }else{
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentProgressBar.visibility = View.GONE
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentLoadingText.visibility = View.GONE
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentViewPager.visibility = View.VISIBLE
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentSmartTab.visibility = View.VISIBLE
            kotlinx.android.synthetic.main.activity_add_torrent.addTorrentFab.visibility = View.VISIBLE
        }
    }
}