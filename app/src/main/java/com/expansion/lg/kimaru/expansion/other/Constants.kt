package com.expansion.lg.kimaru.expansion.other

import android.content.Context

import com.expansion.lg.kimaru.expansion.activity.SessionManagement

import java.net.InetAddress

/**
 * Created by kimaru on 4/12/17.
 */

class Constants(internal var context: Context) {
    internal var session: SessionManagement

    val cloudAddress: String
        get() = session.cloudUrl

    val apiServer: String
        get() {
            val apiPrefix = if (session.apiPrefix == null) "" else session.apiPrefix + "/"
            val apiVersion = if (session.apiVersion == null) "" else session.apiVersion + "/"
            val apiSuffix = if (session.apiSuffix == null) "" else session.apiSuffix
            return "$cloudAddress/$apiPrefix$apiVersion$apiSuffix"
        }

    val peerServer: String
        get() = session.peerServer

    val peerServerPort: String
        get() = session.peerServerPort

    val apiTrainingEndpoint: String
        get() = session.apiTrainingEndpoint

    init {
        this.session = SessionManagement(context)
    }

    fun isValidPhone(phoneNumber: String): Boolean {
        var phoneNumber = phoneNumber
        phoneNumber = phoneNumber.trim { it <= ' ' }
        if (phoneNumber.trim { it <= ' ' } == "") {
            return false
        }
        if (phoneNumber.trim { it <= ' ' }.startsWith("+")) {
            return if (phoneNumber.length == 13) {
                true
            } else {
                false
            }
        }

        return if (phoneNumber.length != 10) {
            false
        } else {
            true
        }
    }

    companion object {
        val PEER_SERVER = "http://192.168.43.1"
        val PEER_SERVER_PORT = 8090
        val DATABASE_VERSION = 2
        val DATABASE_NAME = "expansion"

        var varchar_field = " varchar(512) "
        var primary_field = " id INTEGER PRIMARY KEY AUTOINCREMENT "
        var integer_field = " integer default 0 "
        var text_field = " text "
        var real_field = " REAL "


        var API_TRAINING = "trainings"
    }
}
