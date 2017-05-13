package com.schiwfty.tex.views.main.mvp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.schiwfty.tex.R
import com.schiwfty.tex.views.addtorrent.AddTorrentActivity
import com.schiwfty.tex.views.all.mvp.AllFragment
import com.schiwfty.tex.views.main.DialogManager
import com.schiwfty.tex.views.main.IDialogManager
import com.schiwfty.tex.views.main.MainPagerAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {

    lateinit var presenter: MainContract.Presenter
    lateinit var dialogManager: IDialogManager
    val fragAdapter = MainPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter()
        presenter.setup(this, this)
        dialogManager = DialogManager()


        setSupportActionBar(mainToolbar)
        supportActionBar?.title = getString(R.string.app_name)


        mainViewPager.adapter = fragAdapter
        mainSmartTab.setViewPager(mainViewPager)

        addMagnetFab.setOnClickListener {
            dialogManager.showAddMagnetDialog(fragmentManager)
        }

        addHashFab.setOnClickListener {
            dialogManager.showAddHashDialog(fragmentManager)
        }

    }

    override fun showError(stringId: Int) {
        Toasty.error(this, getString(stringId))
    }

    override fun showInfo(stringId: Int) {
        Toasty.info(this, getString(stringId))
    }

    override fun showSuccess(stringId: Int) {
        Toasty.success(this, getString(stringId))
    }

    override fun showAddTorrentActivity(hash: String?, magnet: String?, torrentFilePath: String?) {
        val addTorrentIntent = Intent(this, AddTorrentActivity::class.java)
        if(hash!=null) addTorrentIntent.putExtra(AddTorrentActivity.ARG_TORRENT_HASH, hash)
        if(magnet!=null) addTorrentIntent.putExtra(AddTorrentActivity.ARG_TORRENT_MAGNET, magnet)
        if(torrentFilePath!=null) addTorrentIntent.putExtra(AddTorrentActivity.ARG_TORRENT_FILE_PATH, torrentFilePath)
        startActivity(addTorrentIntent)
    }
}
