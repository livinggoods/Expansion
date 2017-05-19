package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.KeCounty;
import com.expansion.lg.kimaru.expansion.other.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kimaru on 5/18/17.
 */

public class KeCountyTable extends SQLiteOpenHelper {
    public static final String TABLE_NAME="interview";
    public static final String JSON_ROOT="interviews";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";
    public static String real_field = " REAL ";

    //the fields that I need
    public static String ID = "id";
    public static String COUNTYNAME = "county_name";
    public static String COUNTRY = "country";
    public static String LAT = "lat";
    public static String LON = "lon";
    public static String CONTACTPERSON = "contact_person";
    public static String COUNTYCODE = "county_code";
    public static String CONTACTPERSONPHONE = "contact_person_phone";
    public static String MAINTOWN = "main_town";
    public static String COUNTYSUPPORT = "county_support";
    public static String CHVACTIVITY = "chv_activity";
    public static String CHVACTIVITYLEVEL = "chv_activity_level";
    public static String COUNTYPOPULATION = "county_population";
    public static String NOOFVILLAGES = "no_of_villages";
    public static String MAINTOWNPOPULATION = "main_town_population";
    public static String SERVICEPOPULATION = "service_population";
    public static String POPULATIONDENSITY = "population_density";
    public static String TRANSPORTCOST = "transport_cost";
    public static String MAJORROADS = "major_roads";
    public static String HEALTFACILITIES = "health_facilities";
    public static String PRIVATECLINICSINTOWN = "private_clinics_in_town";
    public static String PRIVATECLINICSINRADIUS = "private_clinics";
    public static String COMMUNITYUNITS = "community_units";
    public static String MAINSUPERMARKETS = "main_supermarkets";
    public static String MAINBANKS = "main_banks";
    public static String ANYMAJORBUSINESS = "any_major_business";
    public static String COMMENTS = "comments";
    public static String RECOMMENDED = "recommended";
    public static String DATEADDED = "date_added";
    public static String ADDEDBY = "added_by";
    public static String LGPRESENT = "lg_present";
    public static String SYNCED = "synced";

    public static  String [] counties = {"Mombasa", "Kwale", "Kilifi", "Tana River", "Lamu",
            "Taita-Taveta", "Garissa", "Wajir", "Mandera", "Marsabit", "Isiolo", "Meru",
            "Tharaka Nithi", "Embu", "Kitui", "Machakos", "Makueni", "Nyandarua", "Nyeri",
            "Kirinyaga", "Murang'a", "Kiambu", "Turkana", "West Pokot", "Samburu", "Trans Nzoia",
            "Uasin Gishu", "Elgeyo Marakwet", "Nandi", "Baringo", "Laikipia", "Nakuru", "Narok",
            "Kajiado", "Kericho", "Bomet", "Kakamega", "Vihiga", "Bungoma", "Busia", "Siaya",
            "Kisumu", "Homa Bay", "Migori", "Kisii", "Nyamira", "Nairobi City"};
    String[] columns  = {ID, COUNTYNAME, COUNTRY, LAT, LON, CONTACTPERSON, COUNTYCODE,
            CONTACTPERSONPHONE, MAINTOWN, COUNTYSUPPORT, CHVACTIVITY, CHVACTIVITYLEVEL,
            COUNTYPOPULATION, NOOFVILLAGES, MAINTOWNPOPULATION, SERVICEPOPULATION,
            POPULATIONDENSITY, TRANSPORTCOST, MAJORROADS, HEALTFACILITIES, PRIVATECLINICSINTOWN,
            PRIVATECLINICSINRADIUS, COMMUNITYUNITS, MAINSUPERMARKETS, MAINBANKS, ANYMAJORBUSINESS,
            COMMENTS, RECOMMENDED, DATEADDED, ADDEDBY, LGPRESENT, SYNCED};
    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + primary_field + ", "
            + COUNTYNAME + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + LAT + real_field + ", "
            + LON + real_field + ", "
            + CONTACTPERSON + varchar_field + ", "
            + COUNTYCODE + varchar_field + ", "
            + CONTACTPERSONPHONE + varchar_field + ", "
            + MAINTOWN + varchar_field + ", "
            + COUNTYSUPPORT + varchar_field + ", "
            + CHVACTIVITY + varchar_field + ", "
            + CHVACTIVITYLEVEL + varchar_field + ", "
            + COUNTYPOPULATION + varchar_field + ", "
            + NOOFVILLAGES + real_field + ", "
            + MAINTOWNPOPULATION + real_field + ", "
            + SERVICEPOPULATION + varchar_field + ", "
            + POPULATIONDENSITY + real_field + ", "
            + TRANSPORTCOST + integer_field + ", "
            + MAJORROADS + varchar_field + ", "
            + HEALTFACILITIES + varchar_field + ", "
            + PRIVATECLINICSINTOWN + varchar_field + ", "
            + PRIVATECLINICSINRADIUS + varchar_field + ", "
            + COMMUNITYUNITS + varchar_field + ", "
            + MAINSUPERMARKETS + varchar_field + ", "
            + MAINBANKS + varchar_field + ", "
            + ANYMAJORBUSINESS + integer_field + ", "
            + COMMENTS + text_field + ", "
            + RECOMMENDED + integer_field + ", "
            + DATEADDED + real_field + ", "
            + ADDEDBY + integer_field + ", "
            + LGPRESENT + integer_field + ", "
            + SYNCED + integer_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public KeCountyTable(Context context){
        super (context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void  onCreate (SQLiteDatabase db){
        db.execSQL(CREATE_DATABASE);
        createKeCounties();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.w("referral", "upgrading database from" + oldVersion + "to" + newVersion);

        if (oldVersion < 2){
            upgradeVersion2(db);
        }
    }

    public long addKeCounty(KeCounty keCounty) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, keCounty.getId());
        cv.put(COUNTYNAME, keCounty.getCountyName());
        cv.put(COUNTRY, keCounty.getCountry());
        cv.put(LAT, keCounty.getLat());
        cv.put(LON, keCounty.getLon());
        cv.put(CONTACTPERSON, keCounty.getContactPerson());
        cv.put(COUNTYCODE, keCounty.getCountyCode());
        cv.put(CONTACTPERSONPHONE, keCounty.getContactPersonPhone());
        cv.put(MAINTOWN, keCounty.getMainTown());
        cv.put(COUNTYSUPPORT, keCounty.getCountySupport());
        cv.put(CHVACTIVITY, keCounty.isChvActivity() ? 1 : 0);
        cv.put(CHVACTIVITYLEVEL, keCounty.getChvActivityLevel());
        cv.put(COUNTYPOPULATION, keCounty.getPopulationDensity());
        cv.put(NOOFVILLAGES, keCounty.getNoOfVillages());
        cv.put(MAINTOWNPOPULATION, keCounty.getMainTownPopulation());
        cv.put(SERVICEPOPULATION, keCounty.getServicePopulation());
        cv.put(POPULATIONDENSITY, keCounty.getPopulationDensity());
        cv.put(TRANSPORTCOST, keCounty.getTransportCost());
        cv.put(MAJORROADS, keCounty.getMajorRoads());
        cv.put(HEALTFACILITIES, keCounty.getHealtFacilities());
        cv.put(PRIVATECLINICSINTOWN, keCounty.getPrivateClinicsInTown());
        cv.put(PRIVATECLINICSINRADIUS, keCounty.getPrivateClinicsInRadius());
        cv.put(COMMUNITYUNITS, keCounty.getCommunityUnits());
        cv.put(MAINSUPERMARKETS, keCounty.getMainSupermarkets());
        cv.put(MAINBANKS, keCounty.getMainBanks());
        cv.put(ANYMAJORBUSINESS, keCounty.getAnyMajorBusiness());
        cv.put(COMMENTS, keCounty.getComments());
        cv.put(RECOMMENDED, keCounty.isRecommended() ? 1 : 0);
        cv.put(DATEADDED, keCounty.getDateAdded());
        cv.put(ADDEDBY, keCounty.getAddedBy());
        cv.put(LGPRESENT, keCounty.isLgPresent() ? 1 : 0);
        long id;
        if (isExist(keCounty)){
            cv.put(SYNCED, 0);
            id = db.update(TABLE_NAME, cv, ID+"='"+keCounty.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;

    }
    public boolean isExist(KeCounty keCounty) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + keCounty.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }
    public List<KeCounty> getCounties() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<KeCounty> keCountyList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            KeCounty keCounty = new KeCounty();
            keCounty.setId(cursor.getInt(0));
            keCounty.setCountyName(cursor.getString(1));
            keCounty.setCountry(cursor.getString(2));
            keCounty.setLat(cursor.getDouble(3));
            keCounty.setLon(cursor.getDouble(4));
            keCounty.setContactPerson(cursor.getString(5));
            keCounty.setCountyCode(cursor.getString(6));
            keCounty.setContactPersonPhone(cursor.getString(7));
            keCounty.setMainTown(cursor.getString(8));
            keCounty.setCountySupport(cursor.getString(9));
            keCounty.setChvActivityLevel(cursor.getString(11));
            keCounty.setCountyPopulation(cursor.getString(12));
            keCounty.setNoOfVillages(cursor.getLong(13));
            keCounty.setMainTownPopulation(cursor.getLong(14));
            keCounty.setServicePopulation(cursor.getLong(15));
            keCounty.setPopulationDensity(cursor.getLong(16));
            keCounty.setTransportCost(cursor.getInt(17));
            keCounty.setMajorRoads(cursor.getString(18));
            keCounty.setHealtFacilities(cursor.getString(19));
            keCounty.setPrivateClinicsInTown(cursor.getString(20));
            keCounty.setPrivateClinicsInRadius(cursor.getString(21));
            keCounty.setCommunityUnits(cursor.getString(22));
            keCounty.setMainSupermarkets(cursor.getString(23));
            keCounty.setMainBanks(cursor.getString(24));
            keCounty.setAnyMajorBusiness(cursor.getInt(25));
            keCounty.setComments(cursor.getString(26));
            keCounty.setRecommended(cursor.getInt(27) == 1);
            keCounty.setDateAdded(cursor.getLong(28));
            keCounty.setAddedBy(cursor.getInt(29));
            keCounty.setLgPresent(cursor.getInt(30) ==1);
            keCounty.setSynced(cursor.getInt(31) == 1);

            keCountyList.add(keCounty);
        }
        db.close();
        return keCountyList;
    }


    public void createKeCounties(){
        // this should be called once during the installation process.
        int x = 1;
        Long currentDate =  new Date().getTime();
        for (String county: counties) {
            KeCounty countyDetails = new KeCounty(x, county, String.valueOf(x), "KE", 0D, 0D, "", "","","","","",0L
                    ,0L,0L,0L,0,"","","","","","","",0,"",false,currentDate, 1, true, false);
            this.addKeCounty(countyDetails);
            x++;
        }

    }
    private void upgradeVersion2(SQLiteDatabase db){}
}