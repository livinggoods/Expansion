package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;

import com.expansion.lg.kimaru.expansion.activity.SessionManagement;

/**
 * Created by kimaru on 4/12/17.
 */

public class Constants {
    Context context;
    SessionManagement session;
    public static final String PEER_SERVER = "http://192.168.43.1";
    public static final int PEER_SERVER_PORT = 8090;
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "expansion";

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";
    public static String real_field = " REAL ";


    public static int SYNC_STATUS_SYNCED = 1;
    public static int SYNC_STATUS_UNSYNCED = 0;
    public static int SYNC_PAGINATION_SIZE = 1;

    public static String API_TRAINING = "trainings";

    public static final String[] PERMISSIONS = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    public Constants(Context context) {
        this.context = context;
        this.session = new SessionManagement(context);
    }

    public String getCloudAddress() {
        return session.getCloudUrl();
    }

    public String getApiServer() {
        String apiPrefix = session.getApiPrefix() == null ? "" : session.getApiPrefix() + "/";
        String apiVersion = session.getApiVersion() == null ? "" : session.getApiVersion() + "/";
        String apiSuffix = session.getApiSuffix() == null ? "" : session.getApiSuffix();
        return getCloudAddress() + "/" + apiPrefix + apiVersion + apiSuffix;
    }

    public String getPeerServer() {
        return session.getPeerServer();
    }

    public String getPeerServerPort() {

        return session.getPeerServerPort();
    }

    public String getApiTrainingEndpoint() {
        return session.getApiTrainingEndpoint();
    }

    public boolean isValidPhone(String phoneNumber) {
        phoneNumber = phoneNumber.trim();
        if (phoneNumber.trim().equals("")) {
            return false;
        }
        if (phoneNumber.trim().startsWith("+")) {
            if (phoneNumber.length() == 13) {
                return true;
            } else {
                return false;
            }
        }

        if (phoneNumber.length() != 10) {
            return false;
        } else {
            return true;
        }
    }
}
