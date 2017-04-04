package com.expansion.lg.kimaru.expansion.other;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by kimaru on 3/30/17.
 */


public class DefaultListMenu {

    private String title;
    private String instruction;
    private Class clazz;
    private  Fragment fragment;

    public DefaultListMenu(String title, @Nullable Class clazz, @Nullable Fragment fragment) {
        this.title = title;
        this.clazz = clazz;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
