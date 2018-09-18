package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by kimaru on 4/13/17.
 */

public class WifiState {
    ConnectivityManager connectivityManager;
    NetworkInfo wifiCheck;
    Context context;

    public WifiState(Context context){
        this.context = context;
        this.connectivityManager = (ConnectivityManager)
                context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
            this.wifiCheck = connectivityManager.getActiveNetworkInfo();
    }

    public boolean isWifiConnected(){
        boolean wifi;
        try {
            return wifiCheck.getType() == ConnectivityManager.TYPE_WIFI;
        }catch (Exception e){
            return false;
        }

    }

    public boolean canReachPeerServer(){
        Log.d("Tremap", "Checking Peer connection");
        return isWifiConnected();
    }

    public boolean canReachServer(InetAddress ip, int port){
        boolean canReach = false;
        try{
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            Socket socket = new Socket();
            int timeOutMs = 2000;
            socket.connect(socketAddress, timeOutMs);
            canReach = true;
        }catch (Exception e){

        }

        return canReach;
    }


}
