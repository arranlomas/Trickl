package com.shwifty.tex.views.torrentSearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shwifty.tex.R

class TorrentSearchActivity : AppCompatActivity() {

    companion object{
        fun startActivity(context: Context){
            val intent = Intent(context, TorrentSearchActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torrent_search)
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, TorrentSearchFragment.newInstance())
                .commit()
    }
}
