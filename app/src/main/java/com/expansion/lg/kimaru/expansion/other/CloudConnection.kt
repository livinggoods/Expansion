package com.expansion.lg.kimaru.expansion.other

import android.content.Context

import com.expansion.lg.kimaru.expansion.activity.SessionManagement

/**
 * Created by kimaru on 7/18/17.
 */

class CloudConnection(internal var context: Context) {

    fun ServerUrl(): String {
        val sessionManagement = SessionManagement(context)
        return sessionManagement.cloudUrl
    }
}
