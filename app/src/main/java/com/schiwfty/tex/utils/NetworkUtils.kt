package com.schiwfty.tex.utils

import java.net.NetworkInterface
import java.util.*

/**
 * Created by arran on 27/05/2017.
 */
fun getIPAddress(): String {
    val useIPv4: Boolean = true
    try {
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            val addrs = Collections.list(intf.inetAddresses)
            for (addr in addrs) {
                if (!addr.isLoopbackAddress) {
                    val sAddr = addr.hostAddress
                    val isIPv4 = sAddr.indexOf(':') < 0

                    if (useIPv4) {
                        if (isIPv4)
                            return sAddr
                    } else {
                        if (!isIPv4) {
                            val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                            return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(0, delim).toUpperCase()
                        }
                    }
                }
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    // for now swallow exceptions
    return ""
}