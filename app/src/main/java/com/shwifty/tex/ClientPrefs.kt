package com.shwifty.tex

import java.io.File

/**
 * Created by arran on 11/11/2017.
 */
data class ClientPrefs(
        val workingDirectory: File,
        val seed: Boolean,
        val showStopActionInNotificatio: Boolean
)