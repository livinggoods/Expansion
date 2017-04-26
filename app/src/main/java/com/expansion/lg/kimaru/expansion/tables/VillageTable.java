package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Village;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class VillageTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="village";
    public static final String DATABASE_NAME="expansion";
    public static final int DATABASE_VERSION=1;


    public static String varchar_field = " varchar(512) ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String VILLAGENAME = "villagename";
    public static final String MAPPINGID = "mappingid";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String COUNTRY = "country";
    public static final String SUBCOUNTYID = "subcountyid";
    public static final String LINKFACILITYID = "linkfacilityid";
    public static final String AREACHIEFNAME = "areachiefname";
    public static final String WARD = "ward";
    public static final String ECONOMICSTATUS = "economicstatus";
    public static final String PRIVATEFACILITYFORACT = "privatefacilityforact";
    public static final String PRIVATEFACILITYFORMRDT = "privatefacilityformrdt";
    public static final String NAMEOFNGODOINGICCM = "nameofngodoingiccm";
    public static final String NAMEOFNGODOINGMHEALTH = "nameofngodoingmhealth";
    public static final String DATEADDED = "dateadded";
    public static final String ADDEDBY = "addedby";
    public static final String NUMBEROFCHVS = "numberofchvs";
    public static final String HOUSEHOLDPERCHV = "householdperchv";
    public static final String NUMBEROFVILLAGES = "numberofvillages";
    public static final String DISTANCETOBRANCH = "distancetobranch";
    public static final String TRANSPORTCOST = "transportcost";
    public static final String DISTANCETOMAINROAD = "distancetomainroad";
    public static final String NOOFHOUSEHOLDS = "noofhouseholds";
    public static final String MOHPOPLATIONDENSITY = "mohpoplationdensity";
    public static final String ESTIMATEDPOPULATIONDENSITY = "estimatedpopulationdensity";
    public static final String DISTANCETONEARESTHEALTHFACILITY = "distancetonearesthealthfacility";
    public static final String ACTLEVELS = "actlevels";
    public static final String ACTPRICE = "actprice";
    public static final String MRDTLEVELS = "mrdtlevels";
    public static final String MRDTPRICE = "mrdtprice";
    public static final String NOOFDISTIBUTORS = "noofdistibutors";
    public static final String CHVSTRAINED = "chvstrained";
    public static final String PRESENCEOFESTATES = "presenceofestates";
    public static final String PRESENCEOFFACTORIES = "presenceoffactories";
    public static final String PRESENCEOFHOSTELS = "presenceofhostels";
    public static final String TRADERMARKET = "tradermarket";
    public static final String LARGESUPERMARKET = "largesupermarket";
    public static final String NGOSGIVINGFREEDRUGS = "ngosgivingfreedrugs";
    public static final String NGODOINGICCM = "ngodoingiccm";
    public static final String NGODOINGMHEALTH = "ngodoingmhealth";

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + " varchar(36)" + ","
            + VILLAGENAME + varchar_field + ","
            + MAPPINGID + " varchar(36)" + ","
            + LAT + varchar_field + ","
            + LON + varchar_field + ","
            + COUNTRY + varchar_field + ","
            + SUBCOUNTYID + " varchar(36)" + ","
            + LINKFACILITYID + " varchar(36)" + ","
            + AREACHIEFNAME + varchar_field + ","
            + WARD + varchar_field + ","
            + ECONOMICSTATUS + varchar_field + ","
            + PRIVATEFACILITYFORACT + varchar_field + ","
            + PRIVATEFACILITYFORMRDT + varchar_field + ","
            + NAMEOFNGODOINGICCM + text_field + ","
            + NAMEOFNGODOINGMHEALTH + text_field + ","
            + DATEADDED + integer_field + ","
            + ADDEDBY + varchar_field + ","
            + NUMBEROFCHVS + integer_field + ","
            + HOUSEHOLDPERCHV + integer_field + ","
            + NUMBEROFVILLAGES + integer_field + ","
            + DISTANCETOBRANCH + integer_field + ","
            + TRANSPORTCOST + integer_field + ","
            + DISTANCETOMAINROAD + integer_field + ","
            + NOOFHOUSEHOLDS + integer_field + ","
            + MOHPOPLATIONDENSITY + integer_field + ","
            + ESTIMATEDPOPULATIONDENSITY + integer_field + ","
            + DISTANCETONEARESTHEALTHFACILITY + integer_field + ","
            + ACTLEVELS + integer_field + ","
            + ACTPRICE + integer_field + ","
            + MRDTLEVELS + integer_field + ","
            + MRDTPRICE + integer_field + ","
            + NOOFDISTIBUTORS + integer_field + ","
            + CHVSTRAINED + integer_field + ","
            + PRESENCEOFESTATES + integer_field + ","
            + PRESENCEOFFACTORIES + integer_field + ","
            + PRESENCEOFHOSTELS + integer_field + ","
            + TRADERMARKET + integer_field + ","
            + LARGESUPERMARKET + integer_field + ","
            + NGOSGIVINGFREEDRUGS + integer_field + ","
            + NGODOINGICCM + integer_field + ","
            + NGODOINGMHEALTH + integer_field + ");";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;


    public VillageTable(Context context) {
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

    public long addData(Village village) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, village.getId());
        cv.put(VILLAGENAME, village.getVillageName());
        cv.put(MAPPINGID, village.getMappingId());
        cv.put(LAT, village.getLat());
        cv.put(LON, village.getLon());
        cv.put(COUNTRY, village.getCountry());
        cv.put(SUBCOUNTYID, village.getSubCountyId());
        cv.put(LINKFACILITYID, village.getLinkFacilityId());
        cv.put(AREACHIEFNAME, village.getAreaChiefName());
        cv.put(WARD, village.getWard());
        cv.put(ECONOMICSTATUS, village.getEconomicStatus());
        cv.put(PRIVATEFACILITYFORACT, village.getPrivateFacilityForAct());
        cv.put(PRIVATEFACILITYFORMRDT, village.getPrivateFacilityForMrdt());
        cv.put(NAMEOFNGODOINGICCM, village.getNameOfNgoDoingIccm());
        cv.put(NAMEOFNGODOINGMHEALTH, village.getNameOfNgoDoingMhealth());
        cv.put(DATEADDED, village.getDateAdded());
        cv.put(ADDEDBY, village.getAddedBy());
        cv.put(NUMBEROFCHVS, village.getNumberOfChvs());
        cv.put(HOUSEHOLDPERCHV, village.getHouseholdPerChv());
        cv.put(NUMBEROFVILLAGES, village.getNumberOfVillages());
        cv.put(DISTANCETOBRANCH, village.getDistanceToBranch());
        cv.put(TRANSPORTCOST, village.getTransportCost());
        cv.put(DISTANCETOMAINROAD, village.getDistanceTOMainRoad());
        cv.put(NOOFHOUSEHOLDS, village.getNoOfHouseholds());
        cv.put(MOHPOPLATIONDENSITY, village.getMohPoplationDensity());
        cv.put(ESTIMATEDPOPULATIONDENSITY, village.getEstimatedPopulationDensity());
        cv.put(DISTANCETONEARESTHEALTHFACILITY, village.getDistanceTONearestHealthFacility());
        cv.put(ACTLEVELS, village.getActLevels());
        cv.put(ACTPRICE, village.getActPrice());
        cv.put(MRDTLEVELS, village.getMrdtLevels());
        cv.put(MRDTPRICE, village.getMrdtPrice());
        cv.put(NOOFDISTIBUTORS, village.getNoOfDistibutors());
        cv.put(CHVSTRAINED, village.isChvsTrained());
        cv.put(PRESENCEOFESTATES, village.isPresenceOfEstates());
        cv.put(PRESENCEOFFACTORIES, village.isPresenceOfFactories());
        cv.put(PRESENCEOFHOSTELS, village.isPresenceOfHostels());
        cv.put(TRADERMARKET, village.isTraderMarket());
        cv.put(LARGESUPERMARKET, village.isLargeSupermarket());
        cv.put(NGOSGIVINGFREEDRUGS, village.isNgosGivingFreeDrugs());
        cv.put(NGODOINGICCM, village.isNgoDoingIccm());
        cv.put(NGODOINGMHEALTH, village.isNgoDoingMhealth());

        long id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        return id;
    }


    public List<Village> getVillageData() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, VILLAGENAME, MAPPINGID, LAT, LON, COUNTRY,
                SUBCOUNTYID, LINKFACILITYID, AREACHIEFNAME, WARD, ECONOMICSTATUS,
                PRIVATEFACILITYFORACT, PRIVATEFACILITYFORMRDT, NAMEOFNGODOINGICCM,
                NAMEOFNGODOINGMHEALTH, DATEADDED, ADDEDBY, NUMBEROFCHVS, HOUSEHOLDPERCHV,
                NUMBEROFVILLAGES, DISTANCETOBRANCH, TRANSPORTCOST, DISTANCETOMAINROAD,
                NOOFHOUSEHOLDS, MOHPOPLATIONDENSITY, ESTIMATEDPOPULATIONDENSITY,
                DISTANCETONEARESTHEALTHFACILITY, ACTLEVELS, ACTPRICE, MRDTLEVELS,
                MRDTPRICE, NOOFDISTIBUTORS, CHVSTRAINED, PRESENCEOFESTATES, PRESENCEOFFACTORIES,
                PRESENCEOFHOSTELS, TRADERMARKET, LARGESUPERMARKET, NGOSGIVINGFREEDRUGS,
                NGODOINGICCM, NGODOINGMHEALTH,};

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Village> villages = new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Village village = new Village();

            village.setId(cursor.getString(0));
            village.setVillageName(cursor.getString(1));
            village.setMappingId(cursor.getString(2));
            village.setLat(cursor.getString(3));
            village.setLon(cursor.getString(4));
            village.setCountry(cursor.getString(5));
            village.setSubCountyId(cursor.getString(6));
            village.setLinkFacilityId(cursor.getString(7));
            village.setAreaChiefName(cursor.getString(8));
            village.setWard(cursor.getString(9));
            village.setEconomicStatus(cursor.getString(10));
            village.setPrivateFacilityForAct(cursor.getString(11));
            village.setPrivateFacilityForMrdt(cursor.getString(12));
            village.setNameOfNgoDoingIccm(cursor.getString(13));
            village.setNameOfNgoDoingMhealth(cursor.getString(14));
            village.setDateAdded(cursor.getInt(15));
            village.setAddedBy(cursor.getInt(16));
            village.setNumberOfChvs(cursor.getInt(17));
            village.setHouseholdPerChv(cursor.getInt(18));
            village.setNumberOfVillages(cursor.getInt(19));
            village.setDistanceToBranch(cursor.getInt(20));
            village.setTransportCost(cursor.getInt(21));
            village.setDistanceTOMainRoad(cursor.getInt(22));
            village.setNoOfHouseholds(cursor.getInt(23));
            village.setMohPoplationDensity(cursor.getInt(24));
            village.setEstimatedPopulationDensity(cursor.getInt(25));
            village.setDistanceTONearestHealthFacility(cursor.getInt(26));
            village.setActLevels(cursor.getInt(27));
            village.setActPrice(cursor.getInt(28));
            village.setMrdtLevels(cursor.getInt(29));
            village.setMrdtPrice(cursor.getInt(30));
            village.setNoOfDistibutors(cursor.getInt(31));
            village.setChvsTrained((cursor.getInt(32) ==  1));
            village.setPresenceOfEstates((cursor.getInt(33) == 1));
            village.setPresenceOfFactories((cursor.getInt(34) ==  1));
            village.setPresenceOfHostels((cursor.getInt(35) ==  1));
            village.setTraderMarket((cursor.getInt(36) ==  1));
            village.setLargeSupermarket((cursor.getInt(37) ==  1));
            village.setNgosGivingFreeDrugs((cursor.getInt(38) ==  1));
            village.setNgoDoingIccm((cursor.getInt(39) ==  1));
            village.setNgoDoingMhealth((cursor.getInt(40) ==  1));

            villages.add(village);
        }
        db.close();

        return villages;
    }

    public Cursor getCommunityUnitDataCursor() {
        SQLiteDatabase db=getReadableDatabase();
        String [] columns=new String[]{ID, VILLAGENAME, MAPPINGID, LAT, LON, COUNTRY,
                SUBCOUNTYID, LINKFACILITYID, AREACHIEFNAME, WARD, ECONOMICSTATUS,
                PRIVATEFACILITYFORACT, PRIVATEFACILITYFORMRDT, NAMEOFNGODOINGICCM,
                NAMEOFNGODOINGMHEALTH, DATEADDED, ADDEDBY, NUMBEROFCHVS, HOUSEHOLDPERCHV,
                NUMBEROFVILLAGES, DISTANCETOBRANCH, TRANSPORTCOST, DISTANCETOMAINROAD,
                NOOFHOUSEHOLDS, MOHPOPLATIONDENSITY, ESTIMATEDPOPULATIONDENSITY,
                DISTANCETONEARESTHEALTHFACILITY, ACTLEVELS, ACTPRICE, MRDTLEVELS, MRDTPRICE,
                NOOFDISTIBUTORS, CHVSTRAINED, PRESENCEOFESTATES, PRESENCEOFFACTORIES,
                PRESENCEOFHOSTELS, TRADERMARKET, LARGESUPERMARKET, NGOSGIVINGFREEDRUGS,
                NGODOINGICCM, NGODOINGMHEALTH};
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
    }
}

