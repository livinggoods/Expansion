package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 7/21/17.
 */

public class Ward {
    String id, name, subCounty;
    Integer archived, county;

    public Ward() {}

    public Ward(String id, String name, String subCounty, Integer archived, Integer county) {
        this.id = id;
        this.name = name;
        this.subCounty = subCounty;
        this.archived = archived;
        this.county = county;
    }

    //GET


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubCounty() {
        return subCounty;
    }

    public Integer getArchived() {
        return archived;
    }

    public Integer getCounty() {
        return county;
    }


    //SET

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubCounty(String subCounty) {
        this.subCounty = subCounty;
    }

    public void setArchived(Integer archived) {
        this.archived = archived;
    }

    public void setCounty(Integer county) {
        this.county = county;
    }
}
