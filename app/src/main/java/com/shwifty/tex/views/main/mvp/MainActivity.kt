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
import com.shwifty.tex.R
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.navigation.INavigation
import com.shwifty.tex.navigation.NavigationKey
import com.shwifty.tex.utils.*
import com.shwifty.tex.views.base.mvp.BaseDaggerActivity
import com.shwifty.tex.views.chromecast.mvp.ChromecastControllerContract
import com.shwifty.tex.views.main.MainPagerAdapter
import com.shwifty.tex.views.settings.mvi.SettingsActivity
import com.shwifty.tex.views.splash.mvp.SplashActivity
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_main_activity.*
import java.io.File
import javax.inject.Inject

class MainActivity : BaseDaggerActivity(), MainContract.View {
    private val RC_SELECT_FILE = 302

    @Inject
    lateinit var presenter: MainContract.Presenter

    @Inject
    lateinit var chromecastControllerPresenter: ChromecastControllerContract.Presenter

    @Inject
    lateinit var navigation: INavigation

    @Inject
    lateinit var dialogManager: IDialogManager


    private val fragAdapter = MainPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        presenter.attachView(this)
        presenter.initializeCastContext(this)
        chromecastBottomSheet.setup(chromecastControllerPresenter)
        setupBottomSheet()

        setSupportActionBar(mainToolbar)
        supportActionBar?.title = getString(R.string.app_name)

        mainViewPager.adapter = fragAdapter
        mainSmartTab.setViewPager(mainViewPager)

        addMagnetFab.setOnClickListener {
            dialogManager.showAddMagnetDialog(this, {
                navigation.goTo(NavigationKey.AddTorrent(this, magnet = it))
            })
        }

        addHashFab.setOnClickListener {
            dialogManager.showAddHashDialog(this, {
                navigation.goTo(NavigationKey.AddTorrent(this, hash = it))
            })
        }

        mainViewPager.offscreenPageLimit = 2

        if (intent.hasExtra(SplashActivity.TAG_MAGNET_FROM_INTENT)) {
            navigation.goTo(NavigationKey.AddTorrent(this, magnet = intent.getStringExtra(SplashActivity.TAG_MAGNET_FROM_INTENT)))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chromecastBottomSheet.onDestroy()
        presenter.detachView()
    }

    override fun onResume() {
        presenter.addSessionListener()
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
        return when (item?.itemId) {
            R.id.openDirectory -> {
                FileBrowserActivity.startActivity(this, RC_SELECT_FILE, false, Confluence.workingDir)
                true
            }
            R.id.exit -> {
                dialogManager.showExitAppDialog(this, { exitApplication() })
                true
            }
            R.id.settings -> {
                SettingsActivity.startActivity(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
            file.openFileViaIntent(this, { showError(R.string.error_activity_not_found) }, { showError(R.string.error_unexpected_error_opening_file) })
        })
    }

}
