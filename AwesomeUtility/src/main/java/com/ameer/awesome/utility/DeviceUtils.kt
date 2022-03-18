/*
 * Copyright (c) 2022.
 * Created by Ameer Hamza on 19/3/2022
 * Author Ameer Hamza (ameerhamza7171@gmail.com)
 */

package com.ameer.awesome.utility

import android.app.Activity
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.widget.Toast
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import kotlin.math.ceil

object DeviceUtils {

    fun getMACAddress(interfaceName: String?): String {
        try {
            val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (i in interfaces) {
                if (interfaceName != null) {
                    if (!i.name.equals(interfaceName, ignoreCase = true)) continue
                }
                val mac = i.hardwareAddress ?: return ""
                val buf = StringBuilder()
                for (idx in mac.indices) buf.append(String.format("%02X:", mac[idx]))
                if (buf.isNotEmpty()) buf.deleteCharAt(buf.length - 1)
                return buf.toString()
            }
        } catch (ex: Exception) {
        }
        return ""
    }

    fun getAppVersion(activity: Activity): String {
        val pm: PackageManager = activity.packageManager
        try {
            val pi: PackageInfo = pm.getPackageInfo(activity.packageName, 0)
            return pi.versionCode.toString()
        } catch (ex: PackageManager.NameNotFoundException) {
        }
        return ""
    }

    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%')
                                return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(0, delim).uppercase(Locale.getDefault())
                            }
                        }
                    }
                }
            }
        } catch (ex: java.lang.Exception) {
            return "-"
        }
        return "-"
    }

    fun getAppVersionName(activity: Activity): String {
        val pm: PackageManager = activity.packageManager
        try {
            val pi: PackageInfo = pm.getPackageInfo(activity.packageName, 0)
            return pi.versionName.toString()
        } catch (ex: PackageManager.NameNotFoundException) {
        }
        return ""
    }

    fun screenSize(activity: Activity) {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        val widthPix = ceil(dm.widthPixels * dm.densityDpi / 0.0016)
        Toast.makeText(activity, "" + widthPix, Toast.LENGTH_LONG).show()
    }

    fun screenDensity(activity: Activity): String {
        val density: Float = activity.resources.displayMetrics.density
        val scrDensity: String = when {
            density.toDouble() == 0.75 -> {
                "LDPI"
            }
            density.toDouble() == 1.0 -> {
                "MDPI"
            }
            density.toDouble() == 1.5 -> {
                "HDPI"
            }
            density.toDouble() == 2.0 -> {
                "XHDPI"
            }
            density.toDouble() == 3.0 -> {
                "XXHDPI"
            }
            density.toDouble() == 4.0 -> {
                "XXXHDPI"
            }
            else -> {
                "Unknown screen density."
            }
        }
        return scrDensity
    }
}