package com.shwifty.tex.views.main.mvp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.google.android.gms.cast.framework.CastButtonFactory
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.openFile
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.utils.CONNECTIVITY_STATUS
import com.shwifty.tex.utils.getConnectivityStatus
import com.shwifty.tex.views.addtorrent.AddTorrentActivity
import com.shwifty.tex.views.base.BaseActivity
import com.shwifty.tex.views.main.MainPagerAdapter
import com.shwifty.tex.views.main.di.DaggerMainComponent
import com.shwifty.tex.views.showtorrent.TorrentInfoActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    private val fragAdapter = MainPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMainComponent.builder().torrentRepositoryComponent(TricklComponent.torrentRepositoryComponent).build().inject(this)

        MyApplication.castHandler.initializeCastContext(this)
        setContentView(R.layout.activity_main)
        presenter.attachView(this)
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

    override fun onResume() {
        MyApplication.castHandler.addSessionListener()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        MyApplication.castHandler.removeSessionListener()
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

    override fun getConnectivityStatus(): CONNECTIVITY_STATUS {
        return baseContext.getConnectivityStatus()
    }

    override fun showTorrentInfoActivity(infoHash: String) {
        val intent = Intent(this, TorrentInfoActivity::class.java)
        intent.putExtra(TorrentInfoActivity.ARG_TORRENT_HASH, infoHash)
        startActivity(intent)
    }

    override fun showAddTorrentActivity(hash: String?, magnet: String?, torrentFilePath: String?) {
        AddTorrentActivity.startActivity(this, hash, magnet, torrentFilePath)
    }

    override fun showNoWifiDialog(torrentFile: TorrentFile) {
        TricklComponent.dialogManager.showNoWifiDialog(this, torrentFile)
    }

    override fun startFileDownloading(torrentFile: TorrentFile, torrentRepository: ITorrentRepository) {
        torrentRepository.startFileDownloading(torrentFile, this, true)
    }

    override fun openTorrentFile(torrentFile: TorrentFile, torrentRepository: ITorrentRepository) {
        torrentFile.openFile(this, torrentRepository, {
            showError(R.string.error_no_activity)
        })
    }

    override fun openDeleteTorrentDialog(torrentFile: TorrentFile) {
        TricklComponent.dialogManager.showDeleteFileDialog(fragmentManager, torrentFile)
    }

}
