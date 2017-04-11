package com.expansion.lg.kimaru.expansion.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by kimaru on 4/11/17.
 */

public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private ExpansionAuthenticator expansionAuthenticator;

    @Override
    public void onCreate(){
        Log.d("MyAuthenticatorService", "onCreate");
        // Create a new authenticator object
        expansionAuthenticator = new ExpansionAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyAuthenticatorService", "onBind");
        return expansionAuthenticator.getIBinder();
    }

}
