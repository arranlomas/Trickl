package com.shwifty.tex.views.torrentfiles

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.shwifty.tex.R
import com.shwifty.tex.actions.IActionManager
import com.shwifty.tex.utils.ARG_TORRENT_HASH
import com.shwifty.tex.utils.getHashFromIntent
import com.shwifty.tex.utils.getMagnetFromIntent
import com.shwifty.tex.views.base.mvi.BaseDaggerMviFragment
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoActions
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoResult
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoViewModel
import com.shwifty.tex.views.showtorrent.mvi.TorrentInfoViewState
import com.shwifty.tex.views.torrentfiles.list.TorrentFilesAdapter
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import kotlinx.android.synthetic.main.frag_torrent_files.*
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesFragment : BaseDaggerMviFragment<TorrentInfoActions, TorrentInfoResult, TorrentInfoViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var actionManager: IActionManager

    val itemOnClick: (torrentFile: TorrentFile, type: TorrentFilesAdapter.Companion.ClickTypes) -> Unit = { torrentFile, type ->
        val onError = { error: String ->
            //                showError(error)
        }
        when (type) {
            TorrentFilesAdapter.Companion.ClickTypes.DOWNLOAD -> {
                actionManager.startDownload(context!!, torrentFile, false, onError)
            }
            TorrentFilesAdapter.Companion.ClickTypes.OPEN ->
                actionManager.openTorrentFile(context!!, torrentFile, onError)
            TorrentFilesAdapter.Companion.ClickTypes.CHROMECAST ->
                actionManager.startChromecast(context!!, torrentFile, onError)
        }
    }
    val filesAdapter = TorrentFilesAdapter(itemOnClick)

    companion object {
        fun newInstance(torrentFilePath: String?): Fragment {
            val frag = TorrentFilesFragment()
            val args = Bundle()
            args.putString(ARG_TORRENT_HASH, torrentFilePath)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_torrent_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TorrentInfoViewModel::class.java)

        torrentFilesRecyclerView.adapter = filesAdapter
        torrentFilesRecyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        torrentFilesRecyclerView.layoutManager = llm
        super.setup(viewModel, { error ->
            Toasty.error(context!!, error.localizedMessage).show()
        })
        super.attachActions(actions(), TorrentInfoActions.Load::class.java)
    }

    private fun actions() = Observable.merge(observables())

    private fun observables(): List<Observable<TorrentInfoActions>> = listOf(initialAction())

    private fun initialAction(): Observable<TorrentInfoActions> {
        val hash = getHashFromIntent() ?: getMagnetFromIntent()?.findHashFromMagnet()
        ?: throw IllegalArgumentException("Must provide hash or magnet")
        val trackers = getMagnetFromIntent()?.findTrackersFromMagnet()
        return Observable.just(TorrentInfoActions.Load(hash, trackers))
    }

    override fun render(state: TorrentInfoViewState) {
        state.result?.let {
            filesAdapter.torrentFiles = it.fileList
            filesAdapter.notifyDataSetChanged()
        }
    }
}