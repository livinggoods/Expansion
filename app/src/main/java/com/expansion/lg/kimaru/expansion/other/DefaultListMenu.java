package com.expansion.lg.kimaru.expansion.other;

/**
 * Created by kimaru on 3/30/17.
 */


public class DefaultListMenu {

    private String title;
    private String instruction;
    private Class clazz;

    public DefaultListMenu(String title, Class clazz) {
        this.title = title;
        this.clazz = clazz;
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
}
