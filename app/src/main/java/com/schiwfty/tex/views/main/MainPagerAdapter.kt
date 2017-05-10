package com.schiwfty.tex.views.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.schiwfty.tex.views.all.mvp.AllFragment

/**
 * Created by arran on 9/05/2017.
 */
class MainPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return AllFragment.newInstance()
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }

    override fun getCount(): Int {
        return 1
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return "All torrents"
            else -> throw IllegalStateException("No more that 2 fregments required")
        }
    }
}