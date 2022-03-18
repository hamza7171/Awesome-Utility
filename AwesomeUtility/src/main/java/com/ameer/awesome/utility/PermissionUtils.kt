/*
 * Copyright (c) 2022.
 * Created by Ameer Hamza on 18/3/2022
 * Author Ameer Hamza
 */

package com.ameer.awesome.utility

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

object PermissionUtils {

    fun hasPermissionGranted(context: Context?, permissions: Array<String?>): Boolean {
        var hasGranted = false
        for (permission in permissions) {
            hasGranted = ActivityCompat.checkSelfPermission(context!!, permission!!) == PackageManager.PERMISSION_GRANTED
            if (!hasGranted) return false
        }
        return hasGranted
    }

    fun requestPermission(context: Context, permission: String, requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) return true else {
            if (context is AppCompatActivity) {
                context.requestPermissions(arrayOf(permission), requestCode)
            }
        }
        return false
    }

    /**
     * Check and ask for disabled permissions
     * @param activity  Activity calling the method
     * @param permissions   permissions array needed to be checked
     * @param requestCode   request code associated with the request call
     * @return  flag specifying permission are enabled or not
     */
    fun checkAndRequestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        val requiredPerm = ArrayList<String>()
        for (permission in permissions) if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) requiredPerm.add(permission)
        if (requiredPerm.size == 0) return true
        var mPermission: Array<String?> = arrayOfNulls(requiredPerm.size)
        mPermission = requiredPerm.toArray<String>(mPermission)
        if (mPermission != null) activity.requestPermissions(mPermission, requestCode)
        return false
    }
}