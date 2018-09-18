package com.expansion.lg.kimaru.expansion.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log

import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

/**
 * Created by kimaru on 4/13/17.
 */

class WifiState(internal var context: Context) {
    internal var connectivityManager: ConnectivityManager? = null
    internal var wifiCheck: NetworkInfo? = null

    val isWifiConnected: Boolean
        get() {
            val wifi: Boolean
            try {
                return wifiCheck!!.type == ConnectivityManager.TYPE_WIFI
            } catch (e: Exception) {
                return false
            }

        }

    init {
        this.connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        this.wifiCheck = if (connectivityManager != null) connectivityManager!!.activeNetworkInfo else null
    }

    fun canReachPeerServer(): Boolean {
        Log.d("Tremap", "Checking Peer connection")
        return isWifiConnected
    }

    fun canReachServer(ip: InetAddress, port: Int): Boolean {
        var canReach = false
        try {
            val socketAddress = InetSocketAddress(ip, port)
            val socket = Socket()
            val timeOutMs = 2000
            socket.connect(socketAddress, timeOutMs)
            canReach = true
        } catch (e: Exception) {

        }

        return canReach
    }


}
