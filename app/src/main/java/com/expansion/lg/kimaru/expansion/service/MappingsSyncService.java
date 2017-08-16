package com.expansion.lg.kimaru.expansion.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by kimaru on 4/11/17.
 */

public class MappingsSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static MappingsSyncServiceAdapter syncServiceAdapter = null;

    @Override
    public void onCreate(){
        Log.d("RecruitmentsSyncService", "onCreate fn()");
        synchronized (sSyncAdapterLock){
            if (syncServiceAdapter == null){
                syncServiceAdapter = new MappingsSyncServiceAdapter(getApplicationContext(), true);
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyServiceSync", "onBind");
        return syncServiceAdapter.getSyncAdapterBinder();
    }
}
