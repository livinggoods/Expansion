package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/2/17.
 */

public class Mapping {

    String id, mappingName, country, contactPerson, contactPersonPhone;
    Integer dateAdded, addedBy;

    public Mapping(){

    }

    public Mapping(String id, String mappingName, String country, Integer dateAdded,
                   Integer addedBy, String contactPerson, String contactPersonPhone) {
        this.id = id;
        this.mappingName = mappingName;
        this.country = country;
        this.dateAdded = dateAdded;
        this.country = country;
        this.addedBy = addedBy;
        this.contactPerson = contactPerson;
        this.contactPersonPhone = contactPersonPhone;
    }


    // Get Methods

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public Integer getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Integer addedBy) {
        this.addedBy = addedBy;
    }

    public Integer getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Integer dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }
}
