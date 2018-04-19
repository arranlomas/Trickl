package com.shwifty.tex.views.addtorrent.mvi

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.shwifty.tex.R
import com.shwifty.tex.utils.ARG_ADD_TORRENT_RESULT
import com.shwifty.tex.utils.ARG_TORRENT_FILE_PATH
import com.shwifty.tex.utils.ARG_TORRENT_HASH
import com.shwifty.tex.utils.ARG_TORRENT_MAGNET
import com.shwifty.tex.utils.getHashFromIntent
import com.shwifty.tex.utils.getMagnetFromIntent
import com.shwifty.tex.utils.getTorrentNameFromMagnet
import com.shwifty.tex.utils.setVisible
import com.shwifty.tex.views.addtorrent.list.AddTorrentPagerAdapter
import com.shwifty.tex.views.base.mvi.BaseDaggerMviActivity
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_add_torrent.*
import java.net.URLDecoder
import javax.inject.Inject


/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentActivity : BaseDaggerMviActivity<AddTorrentActions, AddTorrentResult, AddTorrentViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val removeTorrentActionPublisher = PublishSubject.create<AddTorrentActions>()

    companion object {
        fun startActivity(context: Context, hash: String?, magnet: String?, torrentFilePath: String?) {
            val addTorrentIntent = Intent(context, AddTorrentActivity::class.java)
            if (hash != null) addTorrentIntent.putExtra(ARG_TORRENT_HASH, hash)
            if (magnet != null) addTorrentIntent.putExtra(ARG_TORRENT_MAGNET, magnet)
            if (torrentFilePath != null) addTorrentIntent.putExtra(ARG_TORRENT_FILE_PATH, torrentFilePath)
            context.startActivity(addTorrentIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_torrent)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTorrentViewModel::class.java)
        super.setup(viewModel, { error ->
            Toasty.error(this, error.localizedMessage).show()
        })
        super.attachActions(actions(), AddTorrentActions.Load::class.java)

        addTorrentFab.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(ARG_ADD_TORRENT_RESULT, viewModel.getLastState().torrentHash)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        setSupportActionBar(addTorrentToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_torrent_title)
        addTorrentToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        getTorrentNameFromMagnet()?.let {
            URLDecoder.decode(it, "UTF-8")?.let {
                addTorrentLoadingText.text = getString(R.string.loading_torrent_info_for, it)
            }
        }
    }

    private fun actions() = Observable.merge(observables())

    private fun observables(): List<Observable<AddTorrentActions>> = listOf(initialAction(), deleteAction())

    private fun deleteAction(): Observable<AddTorrentActions> {
        return removeTorrentActionPublisher
    }

    private fun initialAction(): Observable<AddTorrentActions> {
        val hash = getHashFromIntent() ?: getMagnetFromIntent()?.findHashFromMagnet()
        ?: throw IllegalArgumentException("Must provide hash or magnet")
        val trackers = getMagnetFromIntent()?.findTrackersFromMagnet()
        return Observable.just(AddTorrentActions.Load(hash, trackers))
    }

    override fun onBackPressed() {
        if (!viewModel.getLastState().torrentAlreadyExisted) {
            viewModel.getLastState().torrentHash?.let {
                removeTorrentActionPublisher.onNext(AddTorrentActions.RemoveTorrent(it))
            } ?: super.onBackPressed()
        } else super.onBackPressed()
    }

    override fun render(state: AddTorrentViewState) {
        addTorrentFab.setVisible(!state.torrentAlreadyExisted && !state.isLoading)
        if (state.torrentRemovedAndShouldRestart) super.onBackPressed()
        setLoading(state.isLoading)
        state.result?.info_hash?.let {
            notifyTorrentAdded(it)
        }

        errorLayout.setVisible(state.error != null && !state.isLoading)
        state.error?.let { errorText.text = it }
    }

    private fun notifyTorrentAdded(torrentHash: String) {
        val adapter = AddTorrentPagerAdapter(supportFragmentManager, torrentHash)
        addTorrentViewPager.adapter = adapter
        addTorrentSmartTab.setViewPager(addTorrentViewPager)
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            addTorrentProgressBar.visibility = View.VISIBLE
            addTorrentLoadingText.visibility = View.VISIBLE
            addTorrentViewPager.visibility = View.GONE
            addTorrentSmartTab.visibility = View.GONE
        } else {
            addTorrentProgressBar.visibility = View.GONE
            addTorrentLoadingText.visibility = View.GONE
            addTorrentViewPager.visibility = View.VISIBLE
            addTorrentSmartTab.visibility = View.VISIBLE
        }
    }
}