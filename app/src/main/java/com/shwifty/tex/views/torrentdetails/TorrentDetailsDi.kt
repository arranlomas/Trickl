package com.shwifty.tex.views.torrentdetails


import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 15/02/2017.
 */
@Module()
abstract class TorrentDetailsFragmentBuilder {
    @ContributesAndroidInjector
    internal abstract fun bindsTorrentDetailsFragment(): TorrentDetailsFragment
}


