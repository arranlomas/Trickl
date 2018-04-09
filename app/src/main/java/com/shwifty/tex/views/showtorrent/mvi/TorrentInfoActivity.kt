package com.shwifty.tex.views.showtorrent.mvi

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.shwifty.tex.R
import com.shwifty.tex.dialogs.IDialogManager
import com.shwifty.tex.utils.*
import com.shwifty.tex.views.base.mvi.BaseDaggerMviActivity
import com.shwifty.tex.views.showtorrent.list.ShowTorrentPagerAdapter
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_show_torrent.*
import java.net.URLDecoder
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentInfoActivity : BaseDaggerMviActivity<TorrentInfoActions, TorrentInfoResult, TorrentInfoViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dialogManager: IDialogManager

    companion object {
        fun open(context: Context, infoHash: String) {
            val intent = Intent(context, TorrentInfoActivity::class.java)
            intent.putExtra(ARG_TORRENT_HASH, infoHash)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_torrent)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TorrentInfoViewModel::class.java)

        super.setup(viewModel, { error ->
            Toasty.error(this, error.localizedMessage).show()
        })
        super.attachActions(actions(), TorrentInfoActions.Load::class.java)

        setSupportActionBar(showTorrentToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showTorrentToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        getTorrentNameFromMagnet()?.let {
            URLDecoder.decode(it, "UTF-8")?.let {
                showTorrentLoadingText.text = getString(R.string.loading_torrent_info_for, it)
            }
        }
    }

    private fun actions() = Observable.merge(observables())

    private fun observables(): List<Observable<TorrentInfoActions>> = listOf(initialAction())

    private fun initialAction(): Observable<TorrentInfoActions> {
        val hash = getHashFromIntent() ?: getMagnetFromIntent()?.findHashFromMagnet()
        ?: throw IllegalArgumentException("Must provide hash or magnet")
        val trackers = getMagnetFromIntent()?.findTrackersFromMagnet()
        return Observable.just(TorrentInfoActions.Load(hash, trackers))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_show_torrent, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                viewModel.getLastState().result?.let {
                    dialogManager.showDeleteTorrentDialog(this, it, {
                        showError(getString(R.string.error_deleting_torrent))
                    })
                }
            }
        }
        return true
    }

    override fun render(state: TorrentInfoViewState) {
        setLoading(state.isLoading)
        notifyTorrentAdded(state)

        if (state.error != null && !state.isLoading) {
            showError(state.error)
        }
    }

    fun showError(errorString: String) {
        error.setVisible(true)
        error.text = errorString
        showTorrentViewPager.setVisible(false)
        showTorrentSmartTab.setVisible(false)
    }

    private fun notifyTorrentAdded(state: TorrentInfoViewState) {
        state.result?.let {
            supportActionBar?.title = state.result.name
            val adapter = ShowTorrentPagerAdapter(supportFragmentManager, state.result.info_hash)
            showTorrentViewPager.setVisible(true)
            showTorrentViewPager.adapter = adapter
            showTorrentSmartTab.setVisible(true)
            showTorrentSmartTab.setViewPager(showTorrentViewPager)
        }
    }

    private fun setLoading(loading: Boolean) {
        showTorrentProgressBar.setVisible(loading)
        showTorrentLoadingText.setVisible(loading)
        showTorrentViewPager.setVisible(!loading)
        showTorrentSmartTab.setVisible(!loading)
    }
}