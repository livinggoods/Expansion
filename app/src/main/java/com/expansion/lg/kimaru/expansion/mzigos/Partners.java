package com.expansion.lg.kimaru.expansion.mzigos;

/**
 * Created by kimaru on 3/28/17.
 */

public class Partners {
    String partnerID;
    String partnerName;
    boolean doingIccm;
    String iccmComponent;
    boolean doingMHealth;
    String villageID;
    String comment;

    //empty constructor
    public Partners(){}

    public Partners (String partnerID, String partnerName, String iccmComponent, String villageID,
                     boolean doingIccm, boolean doingMHealth, String comment){
        this.doingIccm = doingIccm;
        this.partnerName = partnerName;
        this.iccmComponent = iccmComponent;
        this.doingMHealth = doingMHealth;
        this.villageID = villageID;
        this.comment = comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDoingIccm(boolean doingIccm) {
        this.doingIccm = doingIccm;
    }

    public void setDoingMHealth(boolean doingMHealth) {
        this.doingMHealth = doingMHealth;
    }

    public void setIccmComponent(String iccmComponent) {
        this.iccmComponent = iccmComponent;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public void setVillageID(String villageID) {
        this.villageID = villageID;
    }

}
