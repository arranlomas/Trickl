package com.schiwfty.tex.tools.dagger.main


import com.schiwfty.tex.tools.dagger.network.NetworkModule

import dagger.Module

/**
 * Created by arran on 16/02/2017.
 */

@MainScope
@Module(includes = arrayOf(NetworkModule::class))
class MainModule
