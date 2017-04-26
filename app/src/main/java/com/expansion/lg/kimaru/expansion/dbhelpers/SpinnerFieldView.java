package com.expansion.lg.kimaru.expansion.dbhelpers;

import android.support.annotation.Nullable;

/**
 * Created by kimaru on 4/25/17.
 */

public class SpinnerFieldView {
    private String text;
    private int id;
    private String uuid;

    public SpinnerFieldView(String text, @Nullable int id, @Nullable String uuid){
        this.text = text;
        this.id = id;
        this.uuid = uuid;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
