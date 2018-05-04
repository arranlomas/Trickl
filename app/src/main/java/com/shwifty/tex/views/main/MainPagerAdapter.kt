package com.shwifty.tex.views.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.shwifty.tex.views.all.mvp.AllFragment
import com.shwifty.tex.views.base.mvp.BaseFragmentStatePagerAdapter
import com.shwifty.tex.views.browse.mvp.TorrentBrowseFragment
import com.shwifty.tex.views.downloads.mvp.FileDownloadFragment
import com.shwifty.tex.views.search.mvi.TorrentSearchFragment

/**
 * Created by arran on 9/05/2017.
 */
class MainPagerAdapter(fragmentManager: FragmentManager) : BaseFragmentStatePagerAdapter(fragmentManager) {
    private val browseFragment = TorrentBrowseFragment.newInstance()
    private val searchFragment = TorrentSearchFragment.newInstance()
    private val allTorrentsFragment = AllFragment.newInstance()
    private val fileDownloadFragment = FileDownloadFragment.newInstance()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> browseFragment
            1 -> searchFragment
            2 -> allTorrentsFragment
            3 -> fileDownloadFragment
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Browse"
            1 -> "Search"
            2 -> "My torrents"
            3 -> "Downloads"
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }
}