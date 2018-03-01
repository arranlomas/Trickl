package com.shwifty.tex.views.addtorrent.mvp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.shwifty.tex.R
import com.shwifty.tex.views.addtorrent.list.AddTorrentPagerAdapter
import com.shwifty.tex.views.base.mvp.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_torrent.*
import javax.inject.Inject


/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentActivity : BaseActivity(), AddTorrentContract.View {

    @Inject
    lateinit var presenter: AddTorrentContract.Presenter

    companion object {
        val ARG_ADD_TORRENT_RESULT = "arg_torrent_hash_result"
        val ARG_TORRENT_HASH = "arg_torrent_hash"
        val ARG_TORRENT_MAGNET = "arg_torrent_magnet"
        val ARG_TORRENT_FILE_PATH = "arg_torrent_file_path"

        fun startActivity(context: Context, hash: String?, magnet: String?, torrentFilePath: String?) {
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
        AndroidInjection.inject(this)
//        DaggerAddTorrentComponent.builder().tricklComponent(Trickl.tricklComponent).build().inject(this)
        presenter.attachView(this)
        presenter.setup(intent.extras)

        addTorrentFab.setOnClickListener {
            val returnIntent = Intent()
            if (presenter.torrentHash != null) returnIntent.putExtra(ARG_ADD_TORRENT_RESULT, presenter.torrentHash)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        setSupportActionBar(addTorrentToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_torrent_title)
        addTorrentToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (presenter.torrentName != null) {
            addTorrentLoadingText.text = getString(R.string.loading_torrent_info_for, presenter.torrentName)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onBackPressed() {
        presenter.notifyBackPressed()
        super.onBackPressed()
    }

    override fun notifyTorrentAdded() {
        val adapter = AddTorrentPagerAdapter(supportFragmentManager, presenter.torrentHash)
        addTorrentViewPager.adapter = adapter
        addTorrentSmartTab.setViewPager(addTorrentViewPager)
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            addTorrentProgressBar.visibility = View.VISIBLE
            addTorrentLoadingText.visibility = View.VISIBLE
            addTorrentViewPager.visibility = View.GONE
            addTorrentSmartTab.visibility = View.GONE
            addTorrentFab.visibility = View.GONE
        } else {
            addTorrentProgressBar.visibility = View.GONE
            addTorrentLoadingText.visibility = View.GONE
            addTorrentViewPager.visibility = View.VISIBLE
            addTorrentSmartTab.visibility = View.VISIBLE
            addTorrentFab.visibility = View.VISIBLE
        }
    }
}