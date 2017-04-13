package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    public WifiState(Context context){
        this.connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.wifiCheck = connectivityManager.getActiveNetworkInfo();
    }

    public boolean isWifiConnected(){
        return wifiCheck.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public boolean canReachPeerServer(){
        boolean reachPeerServer = false;

        //must be connected to wifi
        if (this.isWifiConnected()){
            try{
                InetAddress address = InetAddress.getByName(Constants.PEER_SERVER);
                if (canReachServer(address, Constants.PEER_SERVER_PORT)){
                    return true;
                }
            }catch (Exception e){
                return false;
            }
        }

        return reachPeerServer;
    }

    public boolean canReachServer(InetAddress ip, int port){
        boolean canReach = false;
        try{
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            Socket socket = new Socket();
            int timeOutMs = 2000;
            socket.connect(socketAddress, timeOutMs);
            canReach = true;
        }catch (Exception e){}

        return canReach;
    }


}
