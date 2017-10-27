package com.shwifty.tex.views.torrentSearch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shwifty.tex.R

class TorrentSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torrent_search)
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, TorrentSearchFragment.newInstance())
                .commit()
    }
}
