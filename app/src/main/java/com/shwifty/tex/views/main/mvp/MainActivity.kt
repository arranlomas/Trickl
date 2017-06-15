package com.shwifty.tex.views.main.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.google.android.gms.cast.framework.CastButtonFactory
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.views.addtorrent.AddTorrentActivity
import com.shwifty.tex.views.main.MainPagerAdapter
import com.shwifty.tex.views.showtorrent.TorrentInfoActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    private val fragAdapter = MainPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TricklComponent.mainComponent.inject(this)
        MyApplication.castHandler.initializeCastContext(this)
        setContentView(R.layout.activity_main)
        presenter.setup(this, this)
        presenter.handleIntent(intent)

        setSupportActionBar(mainToolbar)
        supportActionBar?.title = getString(R.string.app_name)


        mainViewPager.adapter = fragAdapter
        mainSmartTab.setViewPager(mainViewPager)

        addMagnetFab.setOnClickListener {
            TricklComponent.dialogManager.showAddMagnetDialog(fragmentManager)
        }

        addHashFab.setOnClickListener {
            TricklComponent.dialogManager.showAddHashDialog(fragmentManager)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_main, menu)
        CastButtonFactory.setUpMediaRouteButton(applicationContext,
                menu,
                R.id.media_route_menu_item)
        return true
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

    override fun showNoWifiDialog(torrentHash: String, torrentName: String, fileName: String) {
        TricklComponent.dialogManager.showNoWifiDialog(this, torrentHash, torrentName, fileName)
    }
}
