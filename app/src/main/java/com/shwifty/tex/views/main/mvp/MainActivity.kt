package com.shwifty.tex.views.main.mvp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import com.google.android.gms.cast.framework.CastButtonFactory
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.holder.ColorHolder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.openFile
import com.shwifty.tex.MyApplication
import com.shwifty.tex.R
import com.shwifty.tex.TricklComponent
import com.shwifty.tex.utils.CONNECTIVITY_STATUS
import com.shwifty.tex.utils.getConnectivityStatus
import com.shwifty.tex.views.addtorrent.mvp.AddTorrentActivity
import com.shwifty.tex.views.base.BaseActivity
import com.shwifty.tex.views.chromecast.mvp.ChromecastActivity
import com.shwifty.tex.views.main.MainPagerAdapter
import com.shwifty.tex.views.main.di.DaggerMainComponent
import com.shwifty.tex.views.showtorrent.mvp.TorrentInfoActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.support.annotation.NonNull
import android.support.design.widget.BottomSheetBehavior
import android.util.Log
import android.view.View


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

        setupDrawer()
        setupBottomSheet()
    }

    private fun setupBottomSheet(){
        val behavior = BottomSheetBehavior.from(bottom_sheet)
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                Log.e("onStateChanged", "onStateChanged:" + newState)
//                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    fab.setVisibility(View.GONE)
//                } else {
//                    fab.setVisibility(View.VISIBLE)
//                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.e("onSlide", "onSlide")
            }
        })

        val bottomSheetPeekHeight = resources.getDimensionPixelSize(R.dimen.chromecast_bottom_sheet_peek_height)
        behavior.peekHeight = bottomSheetPeekHeight
    }

    private fun setupDrawer() {
        //if you want to update the items at a later time it is recommended to keep it in a variable
        val item1 = PrimaryDrawerItem().withIdentifier(1)
                .withName(R.string.drawer_item_chromecast)
                .withSelectedTextColorRes(R.color.whiteText).withSelectedColorRes(R.color.colorPrimary)
                .withTextColorRes(R.color.whiteText)

        //create the drawer and remember the `Drawer` result object
        val result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(mainToolbar)
                .withSliderBackgroundColorRes(R.color.colorPrimary)
                .addDrawerItems(
                        item1,
                        DividerDrawerItem()
                )
                .withOnDrawerItemClickListener { view, position, drawerItem ->
                    ChromecastActivity.startActivity(this)
                    true
                }
                .build()
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
