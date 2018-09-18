package com.expansion.lg.kimaru.expansion.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView

import com.expansion.lg.kimaru.expansion.R
import com.expansion.lg.kimaru.expansion.activity.MainActivity
import com.expansion.lg.kimaru.expansion.activity.SessionManagement

/**
 * Created by kimaru on 3/22/18 using Android Studio.
 * Maintainer: David Kimaru
 *
 * @github https://github.com/kimarudg
 * @email kimarudg@gmail.com
 * @phone +254722384549
 * @web gakuu.co.ke
 */


class FragmentPitch : Fragment() {

    private val mRecyclerView: RecyclerView? = null

    internal lateinit var sessionManagement: SessionManagement


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_pitch, container, false)
        MainActivity.backFragment = RecruitmentViewFragment()
        sessionManagement = SessionManagement(context!!)
        val webView = v.findViewById<View>(R.id.webViewPitch) as WebView
        if (sessionManagement.userDetails[SessionManagement.KEY_USER_COUNTRY].equals("UG", ignoreCase = true)) {
            webView.loadUrl("file:///android_asset/ug_pitch.html")
        } else {
            webView.loadUrl("file:///android_asset/ke_pitch.html")
        }

        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val s = SessionManagement(context!!)
        (activity as AppCompatActivity).supportActionBar!!.title = "Pitch"
    }

}