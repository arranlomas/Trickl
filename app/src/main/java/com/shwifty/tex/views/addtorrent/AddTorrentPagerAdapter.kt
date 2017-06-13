package com.shwifty.tex.views.addtorrent

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.shwifty.tex.views.torrentdetails.mvp.TorrentDetailsFragment

/**
 * Created by arran on 9/05/2017.
 */
class AddTorrentPagerAdapter(fragmentManager: FragmentManager, private val torrentHash: String?) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return TorrentDetailsFragment.Companion.newInstance(torrentHash)
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return "Torrent Details"
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }

}