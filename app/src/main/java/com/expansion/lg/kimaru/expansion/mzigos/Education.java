package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class Education {

    Integer id, hierachy;
    String levelName, levelType, country;

    public Education(){

    }

    public Education(Integer id, String levelName, String levelType, Integer hierachy, String country ) {
        this.id = id;
        this.hierachy = hierachy;
        this.levelName = levelName;
        this.levelType = levelType;
        this.country = country;
    }


    // Get Methods

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getHierachy() {
        return hierachy;
    }

    public String getCountry() {
        return country;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getLevelType() {
        return levelType;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setHierachy(Integer hierachy) {
        this.hierachy = hierachy;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }
}
