/*
 * Copyright (c) 2022.
 * Created by Ameer Hamza on 15/3/2022
 * Author Ameer Hamza (ameerhamza7171@gmail.com)
 */

package com.ameer.awesome.utility

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class Utility {

    companion object {


//        fun isNetworkAvailable(context: Context): Boolean {
//            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
//                val activeNetworkInfo = connectivityManager.activeNetworkInfo
//                return activeNetworkInfo != null && activeNetworkInfo.isConnected
//            }
//
//            return false
//        }

        fun isValidEmail(email: String?): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()
        }

        /*
        * hide keyboard forcefully
        * */
        fun hideKeyboard(context: Activity) {
            val inputManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(context.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        fun <T> randomizeArray(array: ArrayList<T>): ArrayList<T> {
            val random = Random()
            for (i in 0 until array.size) {
                val randomPosition: Int = random.nextInt(array.size)
                val temp = array[i]
                array[i] = array[randomPosition]
                array[randomPosition] = temp
            }
            return array
        }

        /**
         * this method will return days
         * how many days ago app was installed
         * */
        fun getAppInstallDays(activity: Activity): Int {
            var days: Long = 0
            try {
                val packageManager = activity.packageManager
                val packageInfo = packageManager.getPackageInfo(activity.packageName, PackageManager.GET_PERMISSIONS)
                val timeAgo = Calendar.getInstance().timeInMillis - packageInfo.firstInstallTime
                days = TimeUnit.MILLISECONDS.toDays(timeAgo)
            } catch (e: Exception) {
                Log.d("AppInstallDays", "AppInstallDays error: " + e.message)
            }
            return days.toInt()
        }

        /**
         * this method will restart the app
         * finish all activities start over from start screen
         * provide start screen of your app like splash screen
         * */
        fun restartApp(context: Context, startActivity: Activity) {
            if (context is Activity) context.finishAffinity()
            val mStartActivity = Intent(context, startActivity::class.java)
            val mPendingIntentId = 123456
            val mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT)
            val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mgr[AlarmManager.RTC, System.currentTimeMillis() + 100] = mPendingIntent
            exitProcess(0)
        }

        /**
         * Check sim is available or not in device
         * */
        fun isSimAvailable(context: Context): Boolean {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager //gets the current TelephonyManager
            return tm.simState != TelephonyManager.SIM_STATE_ABSENT
        }

        /**
         * get a random number between two numbers
         * */
        fun getRandomNum(startNum: Long, endNum: Long): Long {
            return (startNum..endNum).random()
        }
    }
}