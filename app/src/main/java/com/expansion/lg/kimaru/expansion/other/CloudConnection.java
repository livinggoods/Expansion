package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;

import com.expansion.lg.kimaru.expansion.activity.SessionManagement;

/**
 * Created by kimaru on 7/18/17.
 */

public class CloudConnection {
    Context context;

    public CloudConnection(Context mContext){
        this.context = mContext;
    }

    public String ServerUrl(){
        SessionManagement sessionManagement = new SessionManagement(context);
        String serverUrl = sessionManagement.getCloudUrl();
        return serverUrl;
    }
}
