package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class CommunityUnitTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="community_unit";
    public static final String DATABASE_NAME="expansion";
    public static final int DATABASE_VERSION=1;


    public static String varchar_field = " varchar(512) ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "_id";
    public static final String COMMUNITYUNITNAME = "communityunitname";
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
            + COMMUNITYUNITNAME + varchar_field + ","
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


    public CommunityUnitTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion);
        db.execSQL(DATABASE_DROP);
    }

    public long addData(CommunityUnit communityUnit) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, communityUnit.getId());
        cv.put(COMMUNITYUNITNAME, communityUnit.getCommunityUnitName());
        cv.put(MAPPINGID, communityUnit.getMappingId());
        cv.put(LAT, communityUnit.getLat());
        cv.put(LON, communityUnit.getLon());
        cv.put(COUNTRY, communityUnit.getCountry());
        cv.put(SUBCOUNTYID, communityUnit.getSubCountyId());
        cv.put(LINKFACILITYID, communityUnit.getLinkFacilityId());
        cv.put(AREACHIEFNAME, communityUnit.getAreaChiefName());
        cv.put(WARD, communityUnit.getWard());
        cv.put(ECONOMICSTATUS, communityUnit.getEconomicStatus());
        cv.put(PRIVATEFACILITYFORACT, communityUnit.getPrivateFacilityForAct());
        cv.put(PRIVATEFACILITYFORMRDT, communityUnit.getPrivateFacilityForMrdt());
        cv.put(NAMEOFNGODOINGICCM, communityUnit.getNameOfNgoDoingIccm());
        cv.put(NAMEOFNGODOINGMHEALTH, communityUnit.getNameOfNgoDoingMhealth());
        cv.put(DATEADDED, communityUnit.getDateAdded());
        cv.put(ADDEDBY, communityUnit.getAddedBy());
        cv.put(NUMBEROFCHVS, communityUnit.getNumberOfChvs());
        cv.put(HOUSEHOLDPERCHV, communityUnit.getHouseholdPerChv());
        cv.put(NUMBEROFVILLAGES, communityUnit.getNumberOfVillages());
        cv.put(DISTANCETOBRANCH, communityUnit.getDistanceToBranch());
        cv.put(TRANSPORTCOST, communityUnit.getTransportCost());
        cv.put(DISTANCETOMAINROAD, communityUnit.getDistanceTOMainRoad());
        cv.put(NOOFHOUSEHOLDS, communityUnit.getNoOfHouseholds());
        cv.put(MOHPOPLATIONDENSITY, communityUnit.getMohPoplationDensity());
        cv.put(ESTIMATEDPOPULATIONDENSITY, communityUnit.getEstimatedPopulationDensity());
        cv.put(DISTANCETONEARESTHEALTHFACILITY, communityUnit.getDistanceTONearestHealthFacility());
        cv.put(ACTLEVELS, communityUnit.getActLevels());
        cv.put(ACTPRICE, communityUnit.getActPrice());
        cv.put(MRDTLEVELS, communityUnit.getMrdtLevels());
        cv.put(MRDTPRICE, communityUnit.getMrdtPrice());
        cv.put(NOOFDISTIBUTORS, communityUnit.getNoOfDistibutors());
        cv.put(CHVSTRAINED, communityUnit.isChvsTrained());
        cv.put(PRESENCEOFESTATES, communityUnit.isPresenceOfEstates());
        cv.put(PRESENCEOFFACTORIES, communityUnit.isPresenceOfFactories());
        cv.put(PRESENCEOFHOSTELS, communityUnit.isPresenceOfHostels());
        cv.put(TRADERMARKET, communityUnit.isTraderMarket());
        cv.put(LARGESUPERMARKET, communityUnit.isLargeSupermarket());
        cv.put(NGOSGIVINGFREEDRUGS, communityUnit.isNgosGivingFreeDrugs());
        cv.put(NGODOINGICCM, communityUnit.isNgoDoingIccm());
        cv.put(NGODOINGMHEALTH, communityUnit.isNgoDoingMhealth());

        long id=db.insert(TABLE_NAME,null,cv);

        db.close();
        return id;
    }


    public List<CommunityUnit> getCommunityUnitData() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, COMMUNITYUNITNAME, MAPPINGID, LAT, LON, COUNTRY,
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

        List<CommunityUnit> communityUnitList = new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            CommunityUnit communityUnit = new CommunityUnit();

            communityUnit.setId(cursor.getString(0));
            communityUnit.setCommunityUnitName(cursor.getString(1));
            communityUnit.setMappingId(cursor.getString(2));
            communityUnit.setLat(cursor.getString(3));
            communityUnit.setLon(cursor.getString(4));
            communityUnit.setCountry(cursor.getString(5));
            communityUnit.setSubCountyId(cursor.getString(6));
            communityUnit.setLinkFacilityId(cursor.getString(7));
            communityUnit.setAreaChiefName(cursor.getString(8));
            communityUnit.setWard(cursor.getString(9));
            communityUnit.setEconomicStatus(cursor.getString(10));
            communityUnit.setPrivateFacilityForAct(cursor.getString(11));
            communityUnit.setPrivateFacilityForMrdt(cursor.getString(12));
            communityUnit.setNameOfNgoDoingIccm(cursor.getString(13));
            communityUnit.setNameOfNgoDoingMhealth(cursor.getString(14));
            communityUnit.setDateAdded(cursor.getInt(15));
            communityUnit.setAddedBy(cursor.getInt(16));
            communityUnit.setNumberOfChvs(cursor.getInt(17));
            communityUnit.setHouseholdPerChv(cursor.getInt(18));
            communityUnit.setNumberOfVillages(cursor.getInt(19));
            communityUnit.setDistanceToBranch(cursor.getInt(20));
            communityUnit.setTransportCost(cursor.getInt(21));
            communityUnit.setDistanceTOMainRoad(cursor.getInt(22));
            communityUnit.setNoOfHouseholds(cursor.getInt(23));
            communityUnit.setMohPoplationDensity(cursor.getInt(24));
            communityUnit.setEstimatedPopulationDensity(cursor.getInt(25));
            communityUnit.setDistanceTONearestHealthFacility(cursor.getInt(26));
            communityUnit.setActLevels(cursor.getInt(27));
            communityUnit.setActPrice(cursor.getInt(28));
            communityUnit.setMrdtLevels(cursor.getInt(29));
            communityUnit.setMrdtPrice(cursor.getInt(30));
            communityUnit.setNoOfDistibutors(cursor.getInt(31));
            communityUnit.setChvsTrained((cursor.getInt(32) ==  1));
            communityUnit.setPresenceOfEstates((cursor.getInt(33) == 1));
            communityUnit.setPresenceOfFactories((cursor.getInt(34) ==  1));
            communityUnit.setPresenceOfHostels((cursor.getInt(35) ==  1));
            communityUnit.setTraderMarket((cursor.getInt(36) ==  1));
            communityUnit.setLargeSupermarket((cursor.getInt(37) ==  1));
            communityUnit.setNgosGivingFreeDrugs((cursor.getInt(38) ==  1));
            communityUnit.setNgoDoingIccm((cursor.getInt(39) ==  1));
            communityUnit.setNgoDoingMhealth((cursor.getInt(40) ==  1));

            communityUnitList.add(communityUnit);
        }
        db.close();

        return communityUnitList;
    }

    public Cursor getCommunityUnitDataCursor() {
        SQLiteDatabase db=getReadableDatabase();
        String [] columns=new String[]{ID, COMMUNITYUNITNAME, MAPPINGID, LAT, LON, COUNTRY,
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

