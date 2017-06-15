package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.CommunityUnit;
import com.expansion.lg.kimaru.expansion.mzigos.Partners;
import com.expansion.lg.kimaru.expansion.other.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class CommunityUnitTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="community_unit";
    public static final String PARTNERS_TABLE="partners";
    public static final String CU_PARTNERS_TABLE="cu_partners";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String varchar_field = " varchar(512) ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String NAME = "name";
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
    public static final String ICCM = "iccm";
    public static final String ICCMCOMPONENT = "iccm_component";
    public static final String MHEALTH = "mhealth";
    public static final String COMMENT = "comment";
    public static final String PARTNERID = "partner_id";
    public static final String CUID = "cu_id";
    String [] columns=new String[]{ID, NAME, MAPPINGID, LAT, LON, COUNTRY,
            SUBCOUNTYID, LINKFACILITYID, AREACHIEFNAME, WARD, ECONOMICSTATUS,
            PRIVATEFACILITYFORACT, PRIVATEFACILITYFORMRDT, NAMEOFNGODOINGICCM,
            NAMEOFNGODOINGMHEALTH, DATEADDED, ADDEDBY, NUMBEROFCHVS, HOUSEHOLDPERCHV,
            NUMBEROFVILLAGES, DISTANCETOBRANCH, TRANSPORTCOST, DISTANCETOMAINROAD,
            NOOFHOUSEHOLDS, MOHPOPLATIONDENSITY, ESTIMATEDPOPULATIONDENSITY,
            DISTANCETONEARESTHEALTHFACILITY, ACTLEVELS, ACTPRICE, MRDTLEVELS,
            MRDTPRICE, NOOFDISTIBUTORS, CHVSTRAINED, PRESENCEOFESTATES, PRESENCEOFFACTORIES,
            PRESENCEOFHOSTELS, TRADERMARKET, LARGESUPERMARKET, NGOSGIVINGFREEDRUGS,
            NGODOINGICCM, NGODOINGMHEALTH};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + " varchar(36)" + ","
            + NAME + varchar_field + ","
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

    public static final String CREATE_PARTNERS = "CREATE TABLE " + PARTNERS_TABLE + "("
            + ID + " varchar(36)" + ","
            + NAME + varchar_field + ","
            + ICCM + varchar_field + ","
            + ICCMCOMPONENT + varchar_field + ","
            + MHEALTH + varchar_field + ","
            + COMMENT + text_field + ","
            + DATEADDED + integer_field + ","
            + ADDEDBY + integer_field + "); ";

    public static final String CREATE_CU_PARTNERS = "CREATE TABLE " + CU_PARTNERS_TABLE + "("
            + ID + " varchar(36)" + ","
            + PARTNERID + varchar_field + ","
            + CUID + varchar_field + ");";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;
    public static final String PARTNERS_DROP="DROP TABLE IF EXISTS" + PARTNERS_TABLE;
    public static final String PARTNERS_CU_DROP="DROP TABLE IF EXISTS" + CU_PARTNERS_TABLE;


    public CommunityUnitTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
        db.execSQL(CREATE_PARTNERS);
        db.execSQL(CREATE_CU_PARTNERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion);
        if (oldVersion < 2){
            upgradeVersion2(db);
        }
    }

    public long addCommunityUnitData(CommunityUnit communityUnit) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, communityUnit.getId());
        cv.put(NAME, communityUnit.getCommunityUnitName());
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

        long id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        return id;
    }

    public long addPartnerData(Partners partners) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, partners.getPartnerID());
        cv.put(NAME, partners.getPartnerName());
        cv.put(ICCM, partners.isDoingIccm() ? 1 : 0);
        cv.put(ICCMCOMPONENT, partners.getIccmComponent());
        cv.put(MHEALTH, partners.isDoingMHealth() ? 1 : 0);
        cv.put(COMMENT, partners.getComment());
        cv.put(DATEADDED, partners.getDateAdded());
        cv.put(ADDEDBY, partners.getAddedBy());

        long id = db.insertWithOnConflict(PARTNERS_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return id;
    }
    public long addPartnerCUData(String id, Partners partner, CommunityUnit communityUnit) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        cv.put(PARTNERID, partner.getPartnerID());
        cv.put(CUID, communityUnit.getId());

        long savedId = db.insertWithOnConflict(CU_PARTNERS_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return savedId;
    }

    public List<Partners> getPartnersData() {
        SQLiteDatabase db = getReadableDatabase();
        String [] columns =new String [] {ID, NAME, ICCM, ICCMCOMPONENT, MHEALTH, COMMENT, DATEADDED, ADDEDBY};
        Cursor c = db.query(PARTNERS_TABLE, columns,null,null,null,null,null,null);
        List<Partners> partnersList = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Partners partners = new Partners();
            partners.setPartnerID(c.getString(0));
            partners.setPartnerName(c.getString(1));
            partners.setDoingIccm(c.getInt(2) == 1);
            partners.setIccmComponent(c.getString(3));
            partners.setDoingMHealth(c.getInt(4) == 1);
            partners.setComment(c.getString(5));
            partners.setDateAdded(c.getLong(6));
            partners.setAddedBy(c.getLong(7));

            partnersList.add(partners);
        }
        db.close();
        return partnersList;
    }


    public List<CommunityUnit> getCommunityUnitData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<CommunityUnit> communityUnitList = new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            CommunityUnit communityUnit = new CommunityUnit();

            communityUnit.setId(cursor.getString(0));
            communityUnit.setCommunityUnitName(cursor.getString(1));
            communityUnit.setMappingId(cursor.getString(2));
            communityUnit.setLat(cursor.getDouble(3));
            communityUnit.setLon(cursor.getDouble(4));
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

    public CommunityUnit getCommunityUnitById(String uuid) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = ID+" = ? ";

        String[] whereArgs = new String[] {
                uuid
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else {
            CommunityUnit communityUnit = new CommunityUnit();

            communityUnit.setId(cursor.getString(0));
            communityUnit.setCommunityUnitName(cursor.getString(1));
            communityUnit.setMappingId(cursor.getString(2));
            communityUnit.setLat(cursor.getDouble(3));
            communityUnit.setLon(cursor.getDouble(4));
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

            db.close();
            return communityUnit;
        }
    }

    public CommunityUnit getCommunityUnitByName(String communityUnitName) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = NAME+" = ? ";

        String[] whereArgs = new String[] {
                communityUnitName
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else {
            CommunityUnit communityUnit = new CommunityUnit();

            communityUnit.setId(cursor.getString(0));
            communityUnit.setCommunityUnitName(cursor.getString(1));
            communityUnit.setMappingId(cursor.getString(2));
            communityUnit.setLat(cursor.getDouble(3));
            communityUnit.setLon(cursor.getDouble(4));
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

            db.close();
            return communityUnit;
        }
    }
    //
    public List<Partners> getPartnersDataBySubCounty(String subCountyUUID) {

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = SUBCOUNTYID+" = ? ";
        String[] whereArgs = new String[] {
                subCountyUUID
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        String [] columns =new String [] {ID, NAME, ICCM, ICCMCOMPONENT, MHEALTH, COMMENT, DATEADDED, ADDEDBY};
        Cursor c = db.query(PARTNERS_TABLE, columns,null,null,null,null,null,null);
        List<Partners> partnersList = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Partners partners = new Partners();
            partners.setPartnerID(c.getString(0));
            partners.setPartnerName(c.getString(1));
            partners.setDoingIccm(c.getInt(2) == 1);
            partners.setIccmComponent(c.getString(3));
            partners.setDoingMHealth(c.getInt(4) == 1);
            partners.setComment(c.getString(5));
            partners.setDateAdded(c.getLong(6));
            partners.setAddedBy(c.getLong(7));

            partnersList.add(partners);
        }
        db.close();
        return partnersList;
    }

    public List<CommunityUnit> getCommunityUnitBySubCounty(String subCountyUUID) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = SUBCOUNTYID+" = ? ";

        String[] whereArgs = new String[] {
                subCountyUUID
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<CommunityUnit> communityUnitList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            CommunityUnit communityUnit = new CommunityUnit();

            communityUnit.setId(cursor.getString(0));
            communityUnit.setCommunityUnitName(cursor.getString(1));
            communityUnit.setMappingId(cursor.getString(2));
            communityUnit.setLat(cursor.getDouble(3));
            communityUnit.setLon(cursor.getDouble(4));
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

public List<CommunityUnit> getCommunityUnitByLinkFacility(String linkFacilityId) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = LINKFACILITYID+" = ? ";

        String[] whereArgs = new String[] {
                linkFacilityId
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<CommunityUnit> communityUnitList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            CommunityUnit communityUnit = new CommunityUnit();

            communityUnit.setId(cursor.getString(0));
            communityUnit.setCommunityUnitName(cursor.getString(1));
            communityUnit.setMappingId(cursor.getString(2));
            communityUnit.setLat(cursor.getDouble(3));
            communityUnit.setLon(cursor.getDouble(4));
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
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
    }
    private void upgradeVersion2(SQLiteDatabase db) {}
}

