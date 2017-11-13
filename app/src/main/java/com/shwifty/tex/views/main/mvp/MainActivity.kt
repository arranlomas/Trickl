package com.shwifty.tex.views.main.mvp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import com.google.android.gms.cast.framework.CastButtonFactory
import com.schiwfty.kotlinfilebrowser.FileBrowserActivity
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.openFile
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.utils.*
import com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity
import com.shwifty.tex.views.base.mvp.BaseActivity
import com.shwifty.tex.views.main.MainPagerAdapter
import com.shwifty.tex.views.main.di.DaggerMainComponent
import com.shwifty.tex.views.settings.mvp.SettingsActivity
import com.shwifty.tex.views.showtorrent.mvp.TorrentInfoActivity
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_main_activity.*
import java.io.File
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {
    private val RC_SELECT_FILE = 302

    @Inject
    lateinit var presenter: MainContract.Presenter

    private val fragAdapter = MainPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        DaggerMainComponent.builder().tricklComponent(Trickl.tricklComponent).build().inject(this)
        setContentView(R.layout.activity_main)
        presenter.attachView(this)
        presenter.initializeCastContext(this)
        presenter.handleIntent(intent)
        setupBottomSheet()

        setSupportActionBar(mainToolbar)
        supportActionBar?.title = getString(R.string.app_name)

        mainViewPager.adapter = fragAdapter
        mainSmartTab.setViewPager(mainViewPager)

        addMagnetFab.setOnClickListener {
            Trickl.dialogManager.showAddMagnetDialog(this)
        }

        addHashFab.setOnClickListener {
            Trickl.dialogManager.showAddHashDialog(this)
        }

        mainViewPager.offscreenPageLimit = 2
    }

    override fun onDestroy() {
        super.onDestroy()
        chromecastBottomSheet.onDestroy()
        presenter.detachView()
    }

    override fun onResume() {
        presenter.addSessionListener()
        chromecastBottomSheet.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.removeSessionListener()
    }

    private fun setupBottomSheet() {
        val behavior = BottomSheetBehavior.from(bottom_sheet_layout)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                chromecastBottomSheet.notifyBottomSheetStateChanged(newState)
                if (newState == BottomSheetBehavior.STATE_EXPANDED) main_activity_fab.visibility = View.GONE
                else main_activity_fab.visibility = View.VISIBLE
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val bottomSheetPeekHeight = resources.getDimensionPixelSize(R.dimen.chromecast_bottom_sheet_peek_height)
        behavior.peekHeight = bottomSheetPeekHeight
    }

    override fun showChromecastController(show: Boolean) {
        if (show) {
            val lp = main_activity_fab.layoutParams as CoordinatorLayout.LayoutParams
            lp.bottomMargin = resources.getDimensionPixelSize(R.dimen.chromecast_bottom_sheet_peek_height_plus_two_thirds_padding)
            bottom_sheet_layout.visibility = View.VISIBLE
        } else {
            val lp = main_activity_fab.layoutParams as CoordinatorLayout.LayoutParams
            lp.bottomMargin = resources.getDimensionPixelSize(R.dimen.default_medium_padding)
            bottom_sheet_layout.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        if (this.isChromecastAvailable()) {
            menuInflater.inflate(R.menu.toolbar_main_with_chromecast, menu)
            CastButtonFactory.setUpMediaRouteButton(applicationContext,
                    menu,
                    R.id.media_route_menu_item)
            return true
        } else {
            menuInflater.inflate(R.menu.toolbar_main_default, menu)
            return true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.openDirectory -> {
                FileBrowserActivity.startActivity(this, RC_SELECT_FILE, false, Confluence.workingDir)
                return true
            }
            R.id.exit -> {
                Trickl.dialogManager.showExitAppDialog(this, { exitApplication() })
                return true
            }
            R.id.settings -> {
                SettingsActivity.startActivity(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
        Trickl.dialogManager.showNoWifiDialog(this, torrentFile)
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
        Trickl.dialogManager.showDeleteFileDialog(this, torrentFile)
    }

    private fun exitApplication() {
        Confluence.stop()
        finish()
        Thread {
            Thread.sleep(500)
            val id = android.os.Process.myPid()
            android.os.Process.killProcess(id)
        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data.validateOnActivityResult(requestCode, RC_SELECT_FILE, resultCode, Activity.RESULT_OK, {
            val file = it.getSerializable(FileBrowserActivity.ARG_FILE_RESULT) as File
            file.openFileViaIntent(this, { showError(R.string.error_activity_not_found) })
        })
    }

}
