package com.shwifty.tex.views.torrentSearch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shwifty.tex.R
import com.shwifty.tex.Trickl
import com.shwifty.tex.views.base.BaseFragment
import com.shwifty.tex.views.torrentSearch.di.DaggerTorrentSearchComponent
import javax.inject.Inject

/**
 * Created by arran on 27/10/2017.
 */
class TorrentSearchFragment : BaseFragment(), TorrentSearchContract.View {

    @Inject
    lateinit var presenter: TorrentSearchContract.Presenter

    companion object {
        fun newInstance(): Fragment {
            val frag = TorrentSearchFragment()
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerTorrentSearchComponent.builder().networkComponent(Trickl.networkComponent).build().inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (inflater == null) throw IllegalStateException("Torrent Details Fragment layout inflater is null!")
        val view = inflater.inflate(R.layout.frag_torrent_details, container, false)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}