package com.shwifty.tex.views.browse.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import kotlinx.android.synthetic.main.activity_torrent_browse.*

class TorrentBrowseActivity : AppCompatActivity() {

    val torrentBrowseFragment = TorrentBrowseFragment.newInstance() as TorrentBrowseFragment

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        torrentBrowseToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, torrentBrowseFragment)
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_browse, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_browse_filter -> {
                Trickl.dialogManager.showBrowseFilterDialog(this,
                        { sortType, category ->
                            torrentBrowseFragment.updateFilter(sortType, category)
                        })
                return true
            }
        }
        return false
    }
}
