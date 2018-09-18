package com.expansion.lg.kimaru.expansion.other


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

import android.content.Context.CONNECTIVITY_SERVICE

/**
 * Created by kimaru on 9/4/17.
 */

class InternetCheck(internal var context: Context) {

    val isConnected: Boolean
        get() {

            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
}