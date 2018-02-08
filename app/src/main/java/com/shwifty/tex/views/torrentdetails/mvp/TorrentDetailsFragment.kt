package com.shwifty.tex.views.torrentdetails.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.utils.formatBytesAsSize
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.views.base.mvp.BaseFragment
import com.shwifty.tex.views.torrentdetails.di.DaggerTorrentDetailsComponent
import kotlinx.android.synthetic.main.frag_torrent_details.*
import java.io.File
import javax.inject.Inject

/**
 * Created by arran on 7/05/2017.
 */
class TorrentDetailsFragment : BaseFragment(), TorrentDetailsContract.View {

    @Inject
    lateinit var presenter: TorrentDetailsContract.Presenter

    companion object {
        val ARG_TORRENT_HASH = "arg_torrent_hash"

        fun newInstance(torrentHash: String?): Fragment {
            val frag = TorrentDetailsFragment()
            val args = Bundle()
            args.putString(ARG_TORRENT_HASH, torrentHash)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerTorrentDetailsComponent.builder().tricklComponent(Trickl.tricklComponent).build().inject(this)
        presenter.attachView(this)
        presenter.setup(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflater == null) throw IllegalStateException("Torrent Details Fragment layout inflater is null!")
        val view = inflater.inflate(R.layout.frag_torrent_details, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadTorrent(presenter.torrentHash)
    }

    override fun setupViewFromTorrentInfo(torrentInfo: TorrentInfo) {
        summaryName.text = torrentInfo.name
        summaryStoragePath.text = "${Confluence.torrentInfoStorage.absolutePath}${File.separator}${torrentInfo.info_hash}.torrent"
        summarySize.text = torrentInfo.totalSize.formatBytesAsSize()
        summaryFileCount.text = torrentInfo.fileList.size.toString()
        summaryHash.text = torrentInfo.info_hash
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}