package com.shwifty.tex.views.addtorrent.mvi

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findNameFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.shwifty.tex.R
import com.shwifty.tex.utils.setVisible
import com.shwifty.tex.views.addtorrent.list.AddTorrentPagerAdapter
import com.shwifty.tex.views.base.mvi.BaseDaggerMviActivity
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_add_torrent.*
import java.net.URLDecoder
import javax.inject.Inject


/**
 * Created by arran on 7/05/2017.
 */
class AddTorrentActivity : BaseDaggerMviActivity<AddTorrentIntent, AddTorrentViewState>() {

    lateinit var viewModel: AddTorrentContract.ViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val ARG_ADD_TORRENT_RESULT = "arg_torrent_hash_result"
        const val ARG_TORRENT_HASH = "arg_torrent_hash"
        const val ARG_TORRENT_MAGNET = "arg_torrent_magnet"
        const val ARG_TORRENT_FILE_PATH = "arg_torrent_file_path"

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
        super.attachIntents(intents(), AddTorrentIntent.LoadIntent::class.java)

//        addTorrentFab.setOnClickListener {
//            val returnIntent = Intent()
//            if (presenter.torrentHash != null) returnIntent.putExtra(ARG_ADD_TORRENT_RESULT, presenter.torrentHash)
//            setResult(Activity.RESULT_OK, returnIntent)
//            finish()
//        }

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

    private fun intents() = Observable.just(initialIntent())

    private fun initialIntent(): AddTorrentIntent {
        val hash = getHashFromIntent() ?: getMagnetFromIntent()?.findHashFromMagnet()
        ?: throw IllegalArgumentException("Must provide hash or magnet")
        return AddTorrentIntent.LoadIntent(hash)
    }

    private fun getTorrentNameFromMagnet(): String? = getMagnetFromIntent()?.findNameFromMagnet()

    private fun getTrackersFromMagnet(): List<String>? = getMagnetFromIntent()?.findTrackersFromMagnet()

    private fun getHashFromIntent(): String? {
        val arguments = intent.extras
        return if (arguments.containsKey(AddTorrentActivity.Companion.ARG_TORRENT_HASH)) {
            arguments.getString(AddTorrentActivity.Companion.ARG_TORRENT_HASH)
        } else null
    }

    private fun getMagnetFromIntent(): String? {
        val arguments = intent.extras
        return if (arguments.containsKey(AddTorrentActivity.Companion.ARG_TORRENT_MAGNET)) {
            arguments.getString(AddTorrentActivity.Companion.ARG_TORRENT_MAGNET)
        } else null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (viewModel.getLastState()?.torrentAlreadyExisted != true) {
//            torrentRepository.getAllTorrentsFromStorage()
//                    .subscribe(object : BaseSubscriber<List<ParseTorrentResult>>() {
//                        override fun onNext(torrents: List<ParseTorrentResult>) {
//                            torrents.forEach { result ->
//                                result.unwrapIfSuccess {
//                                    if (it.info_hash == torrentHash) torrentRepository.deleteTorrentInfoFromStorage(it)
//                                } ?: let { result.logTorrentParseError() }
//                            }
//                        }
//                    })
        }
    }

    override fun render(state: AddTorrentViewState) {
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
            addTorrentFab.visibility = View.GONE
        } else {
            addTorrentProgressBar.visibility = View.GONE
            addTorrentLoadingText.visibility = View.GONE
            addTorrentViewPager.visibility = View.VISIBLE
            addTorrentSmartTab.visibility = View.VISIBLE
            addTorrentFab.visibility = View.VISIBLE
        }
    }
}