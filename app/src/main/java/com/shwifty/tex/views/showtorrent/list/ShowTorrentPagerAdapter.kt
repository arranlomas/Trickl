package com.shwifty.tex.views.showtorrent.list

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.shwifty.tex.views.torrentdetails.TorrentDetailsFragment
import com.shwifty.tex.views.torrentfiles.mvp.TorrentFilesFragment

/**
 * Created by arran on 9/05/2017.
 */
class ShowTorrentPagerAdapter(fragmentManager: FragmentManager, private val torrentHash: String?) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return TorrentFilesFragment.newInstance(torrentHash)
            1 -> return TorrentDetailsFragment.newInstance(torrentHash)
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return "Torrent Files"
            1 -> return "Torrent Details"
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }
}