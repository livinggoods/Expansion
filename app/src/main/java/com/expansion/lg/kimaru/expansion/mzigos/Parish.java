package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 5/23/17.
 */

public class Parish {
    String id, name, contactPerson, contactPersonPhone, parent, mapping, country, comment;
    Long dateAdded;
    Integer addedBy, synced;
    int color = -1;

    public Parish(){}

    public Parish(String id, String name, String contactPerson, String
                  contactPersonPhone, String parent, String mapping, Long dateAdded,
                  Integer addedBy, Integer synced, String country, String comment){
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.contactPersonPhone = contactPersonPhone;
        this.parent = parent;
        this.mapping = mapping;
        this.dateAdded = dateAdded;
        this.addedBy = addedBy;
        this.synced = synced;
        this.country = country;
        this.comment = comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public void setSynced(Integer synced) {
        this.synced = synced;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setColor(int color) {
        this.color = color;
    }

    // get methods
    public String getId(){
        return id;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public Integer getSynced() {
        return synced;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public String getMapping() {
        return mapping;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public int getColor() {
        return color;
    }

    public String getComment() {
        return comment;
    }

    public String getCountry() {
        return country;
    }
}
