package com.shwifty.tex.views.browse.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shwifty.tex.R
import kotlinx.android.synthetic.main.activity_torrent_browse.*

class TorrentBrowseActivity : AppCompatActivity() {


    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, TorrentBrowseActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torrent_browse)
        setSupportActionBar(torrentBrowseToolbar)
        supportActionBar?.title = getString(R.string.browse)
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, TorrentBrowseFragment.newInstance())
                .commit()
    }
}
