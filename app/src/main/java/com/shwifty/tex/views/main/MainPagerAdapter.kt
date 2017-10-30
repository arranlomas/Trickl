package com.shwifty.tex.views.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.shwifty.tex.views.all.mvp.AllFragment
import com.shwifty.tex.views.base.BaseFragmentStatePagerAdapter
import com.shwifty.tex.views.browse.mvp.TorrentBrowseFragment
import com.shwifty.tex.views.downloads.mvp.FileDownloadFragment

/**
 * Created by arran on 9/05/2017.
 */
class MainPagerAdapter(fragmentManager: FragmentManager) : BaseFragmentStatePagerAdapter(fragmentManager) {
    val browseFragment = TorrentBrowseFragment.newInstance()
    val allTorrentsFragment = AllFragment.newInstance()
    val fileDownloadFragment = FileDownloadFragment.newInstance()

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return browseFragment
            1 -> return allTorrentsFragment
            2 -> return fileDownloadFragment
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return "Browse"
            1 -> return "My torrents"
            2 -> return "Downloads"
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }
}