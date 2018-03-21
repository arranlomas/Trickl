package com.shwifty.tex.views.torrentfiles

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.torrentwrapper.models.TorrentFile
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.shwifty.tex.R
import com.shwifty.tex.actions.IActionManager
import com.shwifty.tex.utils.ARG_TORRENT_HASH
import com.shwifty.tex.utils.getHashFromIntent
import com.shwifty.tex.utils.getMagnetFromIntent
import com.shwifty.tex.views.base.mvi.BaseDaggerMviFragment
import com.shwifty.tex.views.showtorrent.mvi.*
import com.shwifty.tex.views.torrentfiles.list.TorrentFilesAdapter
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import kotlinx.android.synthetic.main.frag_torrent_files.*
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentFilesFragment : BaseDaggerMviFragment<TorrentInfoIntent, TorrentInfoActions, TorrentInfoResult, TorrentInfoViewState>() {

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
                actionManager.startDownload(context!!, torrentFile, onError)
                activity?.finish()
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
        torrentFilesRecyclerView.layoutManager = llm as RecyclerView.LayoutManager?
        super.setup(viewModel, { error ->
            Toasty.error(context!!, error.localizedMessage).show()
        })
        super.attachIntents(intents(), TorrentInfoIntent.LoadInfoIntent::class.java)
    }

    private fun intents() = Observable.merge(observables())

    private fun observables(): List<Observable<TorrentInfoIntent>> = listOf(initialIntent())

    private fun initialIntent(): Observable<TorrentInfoIntent> {
        val hash = getHashFromIntent() ?: getMagnetFromIntent()?.findHashFromMagnet()
        ?: throw IllegalArgumentException("Must provide hash or magnet")
        return Observable.just(TorrentInfoIntent.LoadInfoIntent(hash))
    }

    override fun render(state: TorrentInfoViewState) {
        state.result?.let {
            filesAdapter.torrentFiles = it.fileList
            filesAdapter.notifyDataSetChanged()
        }
    }
}