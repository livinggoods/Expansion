package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class SubCountyTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="sub_county";
    public static final String DATABASE_NAME="expansion";
    public static final int DATABASE_VERSION=1;


    public static String varchar_field = " varchar(512) ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String SUBCOUNTYNAME = "name";
    public static final String COUNTYID = "countyID";
    public static final String COUNTRY = "country";
    public static final String MAPPINGID = "mappingId";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String CONTACTPERSON = "contactPerson";
    public static final String CONTACTPERSONPHONE = "contactPersonPhone";
    public static final String MAINTOWN = "mainTown";
    public static final String COUNTYSUPPORT = "countySupport";
    public static final String SUBCOUNTYSUPPORT = "subcountySupport";
    public static final String CHVACTIVITYLEVEL = "chv_activity_level";
    public static final String COUNTYPOPULATION = "countyPopulation";
    public static final String SUBCOUNTYPOPULATION = "subCountyPopulation";
    public static final String NOOFVILLAGES = "noOfVillages";
    public static final String MAINTOWNPOPULATION = "mainTownPopulation";
    public static final String SERVICEPOPULATION = "servicePopulation";
    public static final String POPULATIONDENSITY = "populationDensity";
    public static final String TRANSPORTCOST = "transportCost";
    public static final String MAJORROADS = "majorRoads";
    public static final String HEALTFACILITIES = "healtFacilities";
    public static final String PRIVATECLINICSINTOWN = "privateClinicsInTown";
    public static final String PRIVATECLINICSINRADIUS = "privateClinicsInRadius";
    public static final String COMMUNITYUNITS = "communityUnits";
    public static final String MAINSUPERMARKETS = "mainSupermarkets";
    public static final String MAINBANKS = "mainBanks";
    public static final String ANYMAJORBUSINESS = "anyMajorBusiness";
    public static final String COMMENTS = "comments";
    public static final String RECOMMENDATION = "recommendation";
    public static final String DATEADDED = "dateAdded";
    public static final String ADDEDBY = "addedBy";

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ","
            + SUBCOUNTYNAME + varchar_field + ","
            + COUNTYID + varchar_field + ","
            + COUNTRY + varchar_field + ","
            + MAPPINGID + varchar_field + ","
            + LAT + varchar_field + ","
            + LON + varchar_field + ","
            + CONTACTPERSON + varchar_field + ","
            + CONTACTPERSONPHONE + varchar_field + ","
            + MAINTOWN + varchar_field + ","
            + COUNTYSUPPORT + varchar_field + ","
            + SUBCOUNTYSUPPORT + varchar_field + ","
            + CHVACTIVITYLEVEL + varchar_field + ","
            + COUNTYPOPULATION + varchar_field + ","
            + SUBCOUNTYPOPULATION + varchar_field + ","
            + NOOFVILLAGES + varchar_field + ","
            + MAINTOWNPOPULATION + varchar_field + ","
            + SERVICEPOPULATION + varchar_field + ","
            + POPULATIONDENSITY + varchar_field + ","
            + TRANSPORTCOST + varchar_field + ","
            + MAJORROADS + varchar_field + ","
            + HEALTFACILITIES + varchar_field + ","
            + PRIVATECLINICSINTOWN + varchar_field + ","
            + PRIVATECLINICSINRADIUS + varchar_field + ","
            + COMMUNITYUNITS + varchar_field + ","
            + MAINSUPERMARKETS + varchar_field + ","
            + MAINBANKS + varchar_field + ","
            + ANYMAJORBUSINESS + varchar_field + ","
            + COMMENTS + varchar_field + ","
            + RECOMMENDATION + integer_field + ","
            + DATEADDED + integer_field + ","
            + ADDEDBY + varchar_field + ")";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;


    public SubCountyTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("VillageTable", "upgrading database from" + oldVersion + "to" + newVersion);
        db.execSQL(DATABASE_DROP);
    }

    public long addData(SubCounty subCounty) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(ID, subCounty.getId());
        cv.put(SUBCOUNTYNAME, subCounty.getSubCountyName());
        cv.put(COUNTYID, subCounty.getCountyID());
        cv.put(COUNTRY, subCounty.getCountry());
        cv.put(MAPPINGID, subCounty.getMappingId());
        cv.put(LAT, subCounty.getLat());
        cv.put(LON, subCounty.getLon());
        cv.put(CONTACTPERSON, subCounty.getContactPerson());
        cv.put(CONTACTPERSONPHONE, subCounty.getContactPersonPhone());
        cv.put(MAINTOWN, subCounty.getMainTown());
        cv.put(COUNTYSUPPORT, subCounty.getCountySupport());
        cv.put(SUBCOUNTYSUPPORT, subCounty.getSubcountySupport());
        cv.put(CHVACTIVITYLEVEL, subCounty.getChvActivityLevel());
        cv.put(COUNTYPOPULATION, subCounty.getCountyPopulation());
        cv.put(SUBCOUNTYPOPULATION, subCounty.getSubCountyPopulation());
        cv.put(NOOFVILLAGES, subCounty.getNoOfVillages());
        cv.put(MAINTOWNPOPULATION, subCounty.getMainTownPopulation());
        cv.put(SERVICEPOPULATION, subCounty.getServicePopulation());
        cv.put(POPULATIONDENSITY, subCounty.getPopulationDensity());
        cv.put(TRANSPORTCOST, subCounty.getTransportCost());
        cv.put(MAJORROADS, subCounty.getMajorRoads());
        cv.put(HEALTFACILITIES, subCounty.getHealtFacilities());
        cv.put(PRIVATECLINICSINTOWN, subCounty.getPrivateClinicsInTown());
        cv.put(PRIVATECLINICSINRADIUS, subCounty.getPrivateClinicsInRadius());
        cv.put(COMMUNITYUNITS, subCounty.getCommunityUnits());
        cv.put(MAINSUPERMARKETS, subCounty.getMainSupermarkets());
        cv.put(MAINBANKS, subCounty.getMainBanks());
        cv.put(ANYMAJORBUSINESS, subCounty.getAnyMajorBusiness());
        cv.put(COMMENTS, subCounty.getComments());
        cv.put(RECOMMENDATION, subCounty.isRecommended() ? 1 : 0);
        cv.put(DATEADDED, subCounty.getDateAdded());
        cv.put(ADDEDBY, subCounty.getAddedBy());

        long id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        return id;
    }


    public List<SubCounty> getSubCountyData() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, SUBCOUNTYNAME, COUNTYID, COUNTRY, MAPPINGID, LAT, LON,
                CONTACTPERSON, CONTACTPERSONPHONE, MAINTOWN, COUNTYSUPPORT, SUBCOUNTYSUPPORT,
                CHVACTIVITYLEVEL, COUNTYPOPULATION, SUBCOUNTYPOPULATION, NOOFVILLAGES,
                MAINTOWNPOPULATION, SERVICEPOPULATION, POPULATIONDENSITY, TRANSPORTCOST, MAJORROADS,
                HEALTFACILITIES, PRIVATECLINICSINTOWN, PRIVATECLINICSINRADIUS, COMMUNITYUNITS,
                MAINSUPERMARKETS, MAINBANKS, ANYMAJORBUSINESS, COMMENTS,
                RECOMMENDATION, DATEADDED, ADDEDBY};

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<SubCounty> subCounties = new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            SubCounty subCounty = new SubCounty();

            subCounty.setId(cursor.getString(0));
            subCounty.setSubCountyName(cursor.getString(1));
            subCounty.setCountyID(cursor.getString(2));
            subCounty.setCountry(cursor.getString(3));
            subCounty.setMappingId(cursor.getString(4));
            subCounty.setLat(cursor.getString(5));
            subCounty.setLon(cursor.getString(6));
            subCounty.setContactPerson(cursor.getString(7));
            subCounty.setContactPersonPhone(cursor.getString(8));
            subCounty.setMainTown(cursor.getString(9));
            subCounty.setCountySupport(cursor.getString(10));
            subCounty.setSubcountySupport(cursor.getString(11));
            subCounty.setChvActivityLevel(cursor.getString(12));
            subCounty.setCountyPopulation(cursor.getString(13));
            subCounty.setSubCountyPopulation(cursor.getString(14));
            subCounty.setNoOfVillages(cursor.getString(15));
            subCounty.setMainTownPopulation(cursor.getString(16));
            subCounty.setServicePopulation(cursor.getString(17));
            subCounty.setPopulationDensity(cursor.getString(18));
            subCounty.setTransportCost(cursor.getString(19));
            subCounty.setMajorRoads(cursor.getString(20));
            subCounty.setHealtFacilities(cursor.getString(21));
            subCounty.setPrivateClinicsInTown(cursor.getString(22));
            subCounty.setPrivateClinicsInRadius(cursor.getString(23));
            subCounty.setCommunityUnits(cursor.getString(24));
            subCounty.setMainSupermarkets(cursor.getString(25));
            subCounty.setMainBanks(cursor.getString(26));
            subCounty.setAnyMajorBusiness(cursor.getString(27));
            subCounty.setComments(cursor.getString(28));
            subCounty.setRecommended((cursor.getInt(29) == 1));
            subCounty.setDateAdded(cursor.getInt(30));
            subCounty.setAddedBy(cursor.getInt(31));


            subCounties.add(subCounty);
        }
        db.close();

        return subCounties;
    }

    public Cursor getSubCountyCursor() {
        SQLiteDatabase db=getReadableDatabase();
        String [] columns=new String[]{ID, SUBCOUNTYNAME, COUNTYID, COUNTRY, MAPPINGID, LAT, LON,
                CONTACTPERSON, CONTACTPERSONPHONE, MAINTOWN, COUNTYSUPPORT, SUBCOUNTYSUPPORT,
                CHVACTIVITYLEVEL, COUNTYPOPULATION, SUBCOUNTYPOPULATION, NOOFVILLAGES,
                MAINTOWNPOPULATION, SERVICEPOPULATION, POPULATIONDENSITY, TRANSPORTCOST, MAJORROADS,
                HEALTFACILITIES, PRIVATECLINICSINTOWN, PRIVATECLINICSINRADIUS, COMMUNITYUNITS,
                MAINSUPERMARKETS, MAINBANKS, ANYMAJORBUSINESS, COMMENTS,
                RECOMMENDATION, DATEADDED, ADDEDBY};
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
    }
}

