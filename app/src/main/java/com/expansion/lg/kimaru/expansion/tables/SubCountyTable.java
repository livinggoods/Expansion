package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class SubCountyTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="sub_county";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String varchar_field = " varchar(512) ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String JSON_ROOT = "subcounties";


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

    String [] columns=new String[]{ID, SUBCOUNTYNAME, COUNTYID, COUNTRY, MAPPINGID, LAT, LON,
            CONTACTPERSON, CONTACTPERSONPHONE, MAINTOWN, COUNTYSUPPORT, SUBCOUNTYSUPPORT,
            CHVACTIVITYLEVEL, COUNTYPOPULATION, SUBCOUNTYPOPULATION, NOOFVILLAGES,
            MAINTOWNPOPULATION, SERVICEPOPULATION, POPULATIONDENSITY, TRANSPORTCOST, MAJORROADS,
            HEALTFACILITIES, PRIVATECLINICSINTOWN, PRIVATECLINICSINRADIUS, COMMUNITYUNITS,
            MAINSUPERMARKETS, MAINBANKS, ANYMAJORBUSINESS, COMMENTS,
            RECOMMENDATION, DATEADDED, ADDEDBY};

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
        if (oldVersion < 2){
            upgradeVersion2(db);
        }
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
        Log.d("Tremap ", "Subcounty Try savig or updating");
        long id;
        if (isExist(subCounty)){
            id = db.update(TABLE_NAME, cv, ID+"='"+subCounty.getId()+"'", null);
            Log.d("Tremap DB Op", "Sub County updated");
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Tremap DB Op", "SubCounty Created");
        }
        Log.d("Tremap ", "Closing Connection");
        db.close();
        return id;

    }
    public boolean isExist(SubCounty subCounty) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + subCounty.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }
    //editData
    public long editData(SubCounty subCounty) {

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

        long id = db.update(TABLE_NAME, cv, ID+"='"+subCounty.getId()+"'", null);
        db.close();
        return id;
    }

    public SubCounty getSubCountyByCountyAndName(String countyId, String country, String subCountyName) {

        SQLiteDatabase db = getReadableDatabase();

        String whereClause = COUNTYID+" = ? AND " +
                COUNTRY+ " = ? AND " +
                SUBCOUNTYNAME+ " = ? ";

        String[] whereArgs = new String[] {
                String.valueOf(countyId),
                String.valueOf(country),
                String.valueOf(subCountyName),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            db.close();
            return null;
        }else {
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
            subCounty.setDateAdded(cursor.getLong(30));
            subCounty.setAddedBy(cursor.getInt(31));
            db.close();
            return subCounty;
        }
    }


    public List<SubCounty> getSubCountyData() {

        SQLiteDatabase db=getReadableDatabase();

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
            subCounty.setDateAdded(cursor.getLong(30));
            subCounty.setAddedBy(cursor.getInt(31));

            subCounties.add(subCounty);
        }
        db.close();

        return subCounties;
    }

    public Cursor getSubCountyCursor() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        db.close();
        return cursor;
    }

    public List<SubCounty> getSubCountiesByCounty(Integer countyId) {
        SQLiteDatabase db=getReadableDatabase();
        String whereClause = COUNTYID+" = ? ";
        String[] whereArgs = new String[] {
                String.valueOf(countyId)
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

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
            subCounty.setDateAdded(cursor.getLong(30));
            subCounty.setAddedBy(cursor.getInt(31));

            subCounties.add(subCounty);
        }
        db.close();

        return subCounties;
    }
    public SubCounty getSubCountyById(String subCountyId) {

        SQLiteDatabase db = getReadableDatabase();

        String whereClause = ID+" = ? ";

        String[] whereArgs = new String[] {
                String.valueOf(subCountyId)
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            db.close();
            return null;
        }else {
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
            subCounty.setDateAdded(cursor.getLong(30));
            subCounty.setAddedBy(cursor.getInt(31));
            db.close();
            return subCounty;
        }
    }

    public  void fromJson (JSONObject jsonObject){
        Log.d("Tremap", "Subcounty Creating from JSON");
        SubCounty subCounty = new SubCounty();
        try {
            subCounty.setId(jsonObject.getString(ID));
            String name= jsonObject.getString(SUBCOUNTYNAME);
            if (!jsonObject.getString(SUBCOUNTYNAME).equalsIgnoreCase("")){
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
            subCounty.setSubCountyName(name);
            subCounty.setCountyID(jsonObject.getString(COUNTYID));
            String country= jsonObject.getString(COUNTRY);
            if (!jsonObject.getString(COUNTRY).equalsIgnoreCase("")){
                country = country.substring(0, 1).toUpperCase() + country.substring(1);
            }
            subCounty.setCountry(country);

            subCounty.setMappingId(jsonObject.getString(MAPPINGID));
            subCounty.setLat(jsonObject.getString(LAT));
            subCounty.setLon(jsonObject.getString(LON));

            String contactPerson= jsonObject.getString(CONTACTPERSON);
            if (!jsonObject.getString(CONTACTPERSON).equalsIgnoreCase("")){
                contactPerson = contactPerson.substring(0, 1).toUpperCase() + contactPerson.substring(1);
            }
            subCounty.setContactPerson(contactPerson);

            String mainTown= jsonObject.getString(MAINTOWN);
            if (!jsonObject.getString(MAINTOWN).equalsIgnoreCase("")){
                mainTown = mainTown.substring(0, 1).toUpperCase() + mainTown.substring(1);
            }
            subCounty.setMainTown(mainTown);
            subCounty.setCountySupport(jsonObject.getString(COUNTYSUPPORT));
            subCounty.setSubcountySupport(jsonObject.getString(SUBCOUNTYSUPPORT));
            subCounty.setChvActivityLevel(jsonObject.getString(CHVACTIVITYLEVEL));
            subCounty.setCountyPopulation(jsonObject.getString(COUNTYPOPULATION));
            subCounty.setSubCountyPopulation(jsonObject.getString(SUBCOUNTYPOPULATION));
            subCounty.setNoOfVillages(jsonObject.getString(NOOFVILLAGES));
            subCounty.setMainTownPopulation(jsonObject.getString(MAINTOWNPOPULATION));
            subCounty.setServicePopulation(jsonObject.getString(SERVICEPOPULATION));
            subCounty.setPopulationDensity(jsonObject.getString(POPULATIONDENSITY));
            subCounty.setTransportCost(jsonObject.getString(TRANSPORTCOST));
            subCounty.setMajorRoads(jsonObject.getString(MAJORROADS));
            subCounty.setHealtFacilities(jsonObject.getString(HEALTFACILITIES));
            subCounty.setPrivateClinicsInTown(jsonObject.getString(PRIVATECLINICSINTOWN));
            subCounty.setPrivateClinicsInRadius(jsonObject.getString(PRIVATECLINICSINRADIUS));
            subCounty.setCommunityUnits(jsonObject.getString(COMMUNITYUNITS));
            subCounty.setMainSupermarkets(jsonObject.getString(MAINSUPERMARKETS));
            subCounty.setMainBanks(jsonObject.getString(MAINBANKS));
            subCounty.setAnyMajorBusiness(jsonObject.getString(ANYMAJORBUSINESS));
            subCounty.setComments(jsonObject.getString(COMMENTS));
            subCounty.setRecommended(jsonObject.getInt(RECOMMENDATION)==1);
            try{
                String added = jsonObject.getString(DATEADDED);
                if (added.equalsIgnoreCase("") || added.isEmpty()){
                    subCounty.setDateAdded(new Date().getTime());
                }else{
                    subCounty.setDateAdded(jsonObject.getLong(DATEADDED));
                }
            }catch (Exception e){
                subCounty.setDateAdded(new Date().getTime());
                Log.e("Tremap Error", "=======================================");
                Log.e("Tremap Error", "SUBCOUNTY E "+e.getMessage());
                Log.e("Tremap Error", "Using the current time instead");
            }
            try {
                String by = jsonObject.getString(ADDEDBY);
                if (by.equalsIgnoreCase("")){
                    subCounty.setAddedBy(1);
                }else{
                    subCounty.setAddedBy(jsonObject.getInt(ADDEDBY));
                }
            }catch (Exception e){
                subCounty.setAddedBy(1);
                Log.e("Tremap Error", "=======================================");
                Log.e("Tremap Error", "SUBCOUNTY E "+e.getMessage());
                Log.e("Tremap Error", "Using 1 instead");
            }

            addData(subCounty);
            Log.d("Tremap", "Subcounty Object from JSON");
        }catch (Exception e){
            Log.e("Tremap Error", "=======================================");
            Log.e("Tremap Error", "SUBCOUNTY E "+e.getMessage());
        }
    }
    public JSONObject getJson() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        JSONObject results = new JSONObject();

        JSONArray resultSet = new JSONArray();

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            int totalColumns = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i =0; i < totalColumns; i++){
                if (cursor.getColumnName(i) != null){
                    try {
                        if (cursor.getString(i) != null){
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        }else{
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    }catch (Exception e){
                    }
                }
            }
            resultSet.put(rowObject);
            try {
                results.put(JSON_ROOT, resultSet);
            } catch (JSONException e) {

            }
        }
        cursor.close();
        db.close();
        return results;
    }

    private void upgradeVersion2(SQLiteDatabase db) {}
}

