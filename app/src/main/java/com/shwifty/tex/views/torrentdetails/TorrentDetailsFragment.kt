package com.shwifty.tex.views.torrentdetails

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.formatBytesAsSize
import com.shwifty.tex.R
import com.shwifty.tex.utils.ARG_TORRENT_HASH
import com.shwifty.tex.utils.getHashFromIntent
import com.shwifty.tex.utils.getMagnetFromIntent
import com.shwifty.tex.views.base.mvi.BaseDaggerMviFragment
import com.shwifty.tex.views.showtorrent.mvi.*
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import kotlinx.android.synthetic.main.frag_torrent_details.*
import java.io.File
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentDetailsFragment : BaseDaggerMviFragment<TorrentInfoIntent, TorrentInfoActions, TorrentInfoResult, TorrentInfoViewState>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    companion object {
        fun newInstance(torrentHash: String?): Fragment {
            val frag = TorrentDetailsFragment()
            val args = Bundle()
            args.putString(ARG_TORRENT_HASH, torrentHash)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_torrent_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TorrentInfoViewModel::class.java)

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
        state.result?.let { torrentInfo ->
            summaryName.text = torrentInfo.name
            summaryStoragePath.text = "${Confluence.torrentInfoStorage.absolutePath}${File.separator}${torrentInfo.info_hash}.torrent"
            summarySize.text = torrentInfo.totalSize.formatBytesAsSize()
            summaryFileCount.text = torrentInfo.fileList.size.toString()
            summaryHash.text = torrentInfo.info_hash

        }
    }
}