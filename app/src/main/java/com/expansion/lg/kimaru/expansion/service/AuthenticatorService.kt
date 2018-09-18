package com.expansion.lg.kimaru.expansion.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * Created by kimaru on 4/11/17.
 */

class AuthenticatorService : Service() {

    // Instance field that stores the authenticator object
    private var expansionAuthenticator: ExpansionAuthenticator? = null

    override fun onCreate() {
        Log.d("MyAuthenticatorService", "onCreate")
        // Create a new authenticator object
        expansionAuthenticator = ExpansionAuthenticator(this)
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    override fun onBind(intent: Intent): IBinder? {
        Log.d("MyAuthenticatorService", "onBind")
        return expansionAuthenticator!!.iBinder
    }

}
