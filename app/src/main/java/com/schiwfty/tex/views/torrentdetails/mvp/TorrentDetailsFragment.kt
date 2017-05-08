package com.schiwfty.tex.views.torrentdetails.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.schiwfty.tex.R
import com.schiwfty.tex.models.TorrentInfo

/**
 * Created by arran on 7/05/2017.
 */
class TorrentDetailsFragment : Fragment(), TorrentDetailsContract.View {

    lateinit var presenter: TorrentDetailsContract.Presenter

    companion object {
        val ARG_TORRENT_FILE_PATH = "arg_torrent_hash"

        fun newInstance(torrentFilePath: String): Fragment {
            val frag = TorrentDetailsFragment()
            val args = Bundle()
            args.putString(ARG_TORRENT_FILE_PATH, torrentFilePath)
            frag.arguments = args
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if(inflater == null) throw IllegalStateException("Torrent Details Fragment layout inflater is null!")
        val view = inflater.inflate(R.layout.frag_torrent_details, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupViewFromTorrentInfo(torrentInfo: TorrentInfo) {
        if(!isAdded || !isVisible) return
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}