package com.schiwfty.tex.views.main.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.schiwfty.tex.R
import com.schiwfty.tex.TricklComponent
import com.schiwfty.tex.models.Torrent
import com.schiwfty.tex.models.TorrentFile
import com.schiwfty.tex.models.TorrentInfo
import com.schiwfty.tex.views.addtorrent.AddTorrentActivity
import com.schiwfty.tex.views.main.DialogManager
import com.schiwfty.tex.views.main.IDialogManager
import com.schiwfty.tex.views.main.MainPagerAdapter
import com.schiwfty.tex.views.showtorrent.TorrentInfoActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    val dialogManager: IDialogManager = DialogManager()
    val fragAdapter = MainPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TricklComponent.mainComponent.inject(this)
        setContentView(R.layout.activity_main)
        Log.v("Arran", presenter.toString())
        presenter.setup(this, this)

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

    override fun showTorrentInfoActivity(infoHash: String){
        val intent = Intent(this, TorrentInfoActivity::class.java)
        intent.putExtra(TorrentInfoActivity.ARG_TORRENT_HASH, infoHash)
        startActivity(intent)
    }

    override fun showAddTorrentActivity(hash: String?, magnet: String?, torrentFilePath: String?) {
        val addTorrentIntent = Intent(this, AddTorrentActivity::class.java)
        if(hash!=null) addTorrentIntent.putExtra(AddTorrentActivity.ARG_TORRENT_HASH, hash)
        if(magnet!=null) addTorrentIntent.putExtra(AddTorrentActivity.ARG_TORRENT_MAGNET, magnet)
        if(torrentFilePath!=null) addTorrentIntent.putExtra(AddTorrentActivity.ARG_TORRENT_FILE_PATH, torrentFilePath)
        startActivity(addTorrentIntent)
    }
}
