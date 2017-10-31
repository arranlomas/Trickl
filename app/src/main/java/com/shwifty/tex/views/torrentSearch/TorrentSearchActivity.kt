package com.shwifty.tex.views.torrentSearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shwifty.tex.R
import kotlinx.android.synthetic.main.activity_torrent_search.*

class TorrentSearchActivity : AppCompatActivity() {


    companion object {
        val ARG_INITIAL_QUERY = "arg_initial_query"

        fun startActivity(context: Context, initialQuery: String? = null) {
            val intent = Intent(context, TorrentSearchActivity::class.java)
            intent.putExtra(ARG_INITIAL_QUERY, initialQuery)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torrent_search)
        setSupportActionBar(torrentSearchToolbar)
        supportActionBar?.title = getString(R.string.search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        torrentSearchToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        val initialQuery = intent.getStringExtra(ARG_INITIAL_QUERY)
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, TorrentSearchFragment.newInstance())
                .commit()
    }
}
