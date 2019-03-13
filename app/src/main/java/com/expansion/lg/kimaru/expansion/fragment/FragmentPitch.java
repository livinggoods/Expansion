package com.expansion.lg.kimaru.expansion.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;
import com.expansion.lg.kimaru.expansion.R;
import com.expansion.lg.kimaru.expansion.activity.MainActivity;
import com.expansion.lg.kimaru.expansion.activity.SessionManagement;

import io.fabric.sdk.android.Fabric;

/**
 * Created by kimaru on 3/22/18 using Android Studio.
 * Maintainer: David Kimaru
 *
 * @github https://github.com/kimarudg
 * @email kimarudg@gmail.com
 * @phone +254722384549
 * @web gakuu.co.ke
 */


public class FragmentPitch extends Fragment {

    private RecyclerView mRecyclerView;

    SessionManagement sessionManagement;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        Fabric.with(getContext(), new Crashlytics());
        View v = inflater.inflate(R.layout.fragment_pitch, container, false);
        MainActivity.backFragment = new RecruitmentViewFragment();
        sessionManagement = new SessionManagement(getContext());
        WebView webView = (WebView) v.findViewById(R.id.webViewPitch);
        if (sessionManagement.getUserDetails().get(SessionManagement.KEY_USER_COUNTRY).equalsIgnoreCase("UG")){
            webView.loadUrl("file:///android_asset/ug_pitch.html");
        }else{
            webView.loadUrl("file:///android_asset/ke_pitch.html");
        }

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SessionManagement s = new SessionManagement(getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Pitch");
    }

}