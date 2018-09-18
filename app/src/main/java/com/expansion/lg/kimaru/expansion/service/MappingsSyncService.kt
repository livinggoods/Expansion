package com.expansion.lg.kimaru.expansion.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * Created by kimaru on 4/11/17.
 */

class MappingsSyncService : Service() {

    override fun onCreate() {
        Log.d("RecruitmentsSyncService", "onCreate fn()")
        synchronized(sSyncAdapterLock) {
            if (syncServiceAdapter == null) {
                syncServiceAdapter = MappingsSyncServiceAdapter(applicationContext, true)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d("MyServiceSync", "onBind")
        return syncServiceAdapter!!.syncAdapterBinder
    }

    companion object {
        private val sSyncAdapterLock = Any()
        private var syncServiceAdapter: MappingsSyncServiceAdapter? = null
    }
}
