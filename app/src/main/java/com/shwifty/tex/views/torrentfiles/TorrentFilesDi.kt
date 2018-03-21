package com.shwifty.tex.views.torrentfiles


import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by arran on 15/02/2017.
 */
@Module
abstract class TorrentFileFragmentBuilder {
    @ContributesAndroidInjector
    internal abstract fun torrentFilesFragment(): TorrentFilesFragment
}


