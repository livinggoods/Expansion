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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static String CU_JSON_ROOT = "community_unit";

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

        long id;
        if (isCommunityUnitExisting(communityUnit)){
            id = db.update(TABLE_NAME, cv, ID+"='"+communityUnit.getId()+"'", null);
            Log.d("Tremap DB OP", "Community Unit updated");
        }else{
            id = db.insert(TABLE_NAME,null,cv);
            Log.d("Tremap DB OP", "Community Unit Created");
        }
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

    public boolean isCommunityUnitExisting(CommunityUnit communityUnit) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + TABLE_NAME + " WHERE " + ID + " = '"
                + communityUnit.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }

    public boolean isPartnerExisting(Partners partner) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + PARTNERS_TABLE + " WHERE " + ID + " = '"
                + partner.getPartnerID() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }
    public boolean isPartnerCuExisting(Partners partner, CommunityUnit communityUnit) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + CU_PARTNERS_TABLE + " WHERE " + PARTNERID + " = '"
                + partner.getPartnerID() + "' AND "+ CUID + "='"+communityUnit.getId()+"'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
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

            communityUnit.setId(cursor.getString(cursor.getColumnIndex(ID)));
            communityUnit.setCommunityUnitName(cursor.getString(cursor.getColumnIndex(NAME)));
            communityUnit.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPINGID)));
            communityUnit.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
            communityUnit.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));
            communityUnit.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            communityUnit.setSubCountyId(cursor.getString(cursor.getColumnIndex(SUBCOUNTYID)));
            communityUnit.setLinkFacilityId(cursor.getString(cursor.getColumnIndex(LINKFACILITYID)));
            communityUnit.setAreaChiefName(cursor.getString(cursor.getColumnIndex(AREACHIEFNAME)));
            communityUnit.setWard(cursor.getString(cursor.getColumnIndex(WARD)));
            communityUnit.setEconomicStatus(cursor.getString(cursor.getColumnIndex(ECONOMICSTATUS)));
            communityUnit.setPrivateFacilityForAct(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORACT)));
            communityUnit.setPrivateFacilityForMrdt(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORMRDT)));
            communityUnit.setNameOfNgoDoingIccm(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGICCM)));
            communityUnit.setNameOfNgoDoingMhealth(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGMHEALTH)));
            communityUnit.setDateAdded(cursor.getInt(cursor.getColumnIndex(DATEADDED)));
            communityUnit.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDEDBY)));
            communityUnit.setNumberOfChvs(cursor.getInt(cursor.getColumnIndex(NUMBEROFCHVS)));
            communityUnit.setHouseholdPerChv(cursor.getInt(cursor.getColumnIndex(HOUSEHOLDPERCHV)));
            communityUnit.setNumberOfVillages(cursor.getInt(cursor.getColumnIndex(NUMBEROFVILLAGES)));
            communityUnit.setDistanceToBranch(cursor.getInt(cursor.getColumnIndex(DISTANCETOBRANCH)));
            communityUnit.setTransportCost(cursor.getInt(cursor.getColumnIndex(TRANSPORTCOST)));
            communityUnit.setDistanceTOMainRoad(cursor.getInt(cursor.getColumnIndex(DISTANCETOMAINROAD)));
            communityUnit.setNoOfHouseholds(cursor.getInt(cursor.getColumnIndex(NOOFHOUSEHOLDS)));
            communityUnit.setMohPoplationDensity(cursor.getInt(cursor.getColumnIndex(MOHPOPLATIONDENSITY)));
            communityUnit.setEstimatedPopulationDensity(cursor.getInt(cursor.getColumnIndex(ESTIMATEDPOPULATIONDENSITY)));
            communityUnit.setDistanceTONearestHealthFacility(cursor.getInt(cursor.getColumnIndex(DISTANCETONEARESTHEALTHFACILITY)));
            communityUnit.setActLevels(cursor.getInt(cursor.getColumnIndex(ACTLEVELS)));
            communityUnit.setActPrice(cursor.getInt(cursor.getColumnIndex(ACTPRICE)));
            communityUnit.setMrdtLevels(cursor.getInt(cursor.getColumnIndex(MRDTLEVELS)));
            communityUnit.setMrdtPrice(cursor.getInt(cursor.getColumnIndex(MRDTPRICE)));
            communityUnit.setNoOfDistibutors(cursor.getInt(cursor.getColumnIndex(NOOFDISTIBUTORS)));
            communityUnit.setChvsTrained((cursor.getInt(cursor.getColumnIndex(CHVSTRAINED)) ==  1));
            communityUnit.setPresenceOfEstates((cursor.getInt(cursor.getColumnIndex(PRESENCEOFESTATES)) == 1));
            communityUnit.setPresenceOfFactories((cursor.getLong(cursor.getColumnIndex(PRESENCEOFFACTORIES))));
            communityUnit.setPresenceOfHostels((cursor.getInt(cursor.getColumnIndex(PRESENCEOFHOSTELS)) ==  1));
            communityUnit.setTraderMarket((cursor.getInt(cursor.getColumnIndex(TRADERMARKET)) ==  1));
            communityUnit.setLargeSupermarket((cursor.getInt(cursor.getColumnIndex(LARGESUPERMARKET)) ==  1));
            communityUnit.setNgosGivingFreeDrugs((cursor.getInt(cursor.getColumnIndex(NGOSGIVINGFREEDRUGS)) ==  1));
            communityUnit.setNgoDoingIccm((cursor.getInt(cursor.getColumnIndex(NGODOINGICCM)) ==  1));
            communityUnit.setNgoDoingMhealth((cursor.getInt(cursor.getColumnIndex(NGODOINGMHEALTH)) ==  1));

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

            communityUnit.setId(cursor.getString(cursor.getColumnIndex(ID)));
            communityUnit.setCommunityUnitName(cursor.getString(cursor.getColumnIndex(NAME)));
            communityUnit.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPINGID)));
            communityUnit.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
            communityUnit.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));
            communityUnit.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            communityUnit.setSubCountyId(cursor.getString(cursor.getColumnIndex(SUBCOUNTYID)));
            communityUnit.setLinkFacilityId(cursor.getString(cursor.getColumnIndex(LINKFACILITYID)));
            communityUnit.setAreaChiefName(cursor.getString(cursor.getColumnIndex(AREACHIEFNAME)));
            communityUnit.setWard(cursor.getString(cursor.getColumnIndex(WARD)));
            communityUnit.setEconomicStatus(cursor.getString(cursor.getColumnIndex(ECONOMICSTATUS)));
            communityUnit.setPrivateFacilityForAct(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORACT)));
            communityUnit.setPrivateFacilityForMrdt(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORMRDT)));
            communityUnit.setNameOfNgoDoingIccm(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGICCM)));
            communityUnit.setNameOfNgoDoingMhealth(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGMHEALTH)));
            communityUnit.setDateAdded(cursor.getInt(cursor.getColumnIndex(DATEADDED)));
            communityUnit.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDEDBY)));
            communityUnit.setNumberOfChvs(cursor.getInt(cursor.getColumnIndex(NUMBEROFCHVS)));
            communityUnit.setHouseholdPerChv(cursor.getInt(cursor.getColumnIndex(HOUSEHOLDPERCHV)));
            communityUnit.setNumberOfVillages(cursor.getInt(cursor.getColumnIndex(NUMBEROFVILLAGES)));
            communityUnit.setDistanceToBranch(cursor.getInt(cursor.getColumnIndex(DISTANCETOBRANCH)));
            communityUnit.setTransportCost(cursor.getInt(cursor.getColumnIndex(TRANSPORTCOST)));
            communityUnit.setDistanceTOMainRoad(cursor.getInt(cursor.getColumnIndex(DISTANCETOMAINROAD)));
            communityUnit.setNoOfHouseholds(cursor.getInt(cursor.getColumnIndex(NOOFHOUSEHOLDS)));
            communityUnit.setMohPoplationDensity(cursor.getInt(cursor.getColumnIndex(MOHPOPLATIONDENSITY)));
            communityUnit.setEstimatedPopulationDensity(cursor.getInt(cursor.getColumnIndex(ESTIMATEDPOPULATIONDENSITY)));
            communityUnit.setDistanceTONearestHealthFacility(cursor.getInt(cursor.getColumnIndex(DISTANCETONEARESTHEALTHFACILITY)));
            communityUnit.setActLevels(cursor.getInt(cursor.getColumnIndex(ACTLEVELS)));
            communityUnit.setActPrice(cursor.getInt(cursor.getColumnIndex(ACTPRICE)));
            communityUnit.setMrdtLevels(cursor.getInt(cursor.getColumnIndex(MRDTLEVELS)));
            communityUnit.setMrdtPrice(cursor.getInt(cursor.getColumnIndex(MRDTPRICE)));
            communityUnit.setNoOfDistibutors(cursor.getInt(cursor.getColumnIndex(NOOFDISTIBUTORS)));
            communityUnit.setChvsTrained((cursor.getInt(cursor.getColumnIndex(CHVSTRAINED)) ==  1));
            communityUnit.setPresenceOfEstates((cursor.getInt(cursor.getColumnIndex(PRESENCEOFESTATES)) == 1));
            communityUnit.setPresenceOfFactories((cursor.getLong(cursor.getColumnIndex(PRESENCEOFFACTORIES))));
            communityUnit.setPresenceOfHostels((cursor.getInt(cursor.getColumnIndex(PRESENCEOFHOSTELS)) ==  1));
            communityUnit.setTraderMarket((cursor.getInt(cursor.getColumnIndex(TRADERMARKET)) ==  1));
            communityUnit.setLargeSupermarket((cursor.getInt(cursor.getColumnIndex(LARGESUPERMARKET)) ==  1));
            communityUnit.setNgosGivingFreeDrugs((cursor.getInt(cursor.getColumnIndex(NGOSGIVINGFREEDRUGS)) ==  1));
            communityUnit.setNgoDoingIccm((cursor.getInt(cursor.getColumnIndex(NGODOINGICCM)) ==  1));
            communityUnit.setNgoDoingMhealth((cursor.getInt(cursor.getColumnIndex(NGODOINGMHEALTH)) ==  1));

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

            communityUnit.setId(cursor.getString(cursor.getColumnIndex(ID)));
            communityUnit.setCommunityUnitName(cursor.getString(cursor.getColumnIndex(NAME)));
            communityUnit.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPINGID)));
            communityUnit.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
            communityUnit.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));
            communityUnit.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            communityUnit.setSubCountyId(cursor.getString(cursor.getColumnIndex(SUBCOUNTYID)));
            communityUnit.setLinkFacilityId(cursor.getString(cursor.getColumnIndex(LINKFACILITYID)));
            communityUnit.setAreaChiefName(cursor.getString(cursor.getColumnIndex(AREACHIEFNAME)));
            communityUnit.setWard(cursor.getString(cursor.getColumnIndex(WARD)));
            communityUnit.setEconomicStatus(cursor.getString(cursor.getColumnIndex(ECONOMICSTATUS)));
            communityUnit.setPrivateFacilityForAct(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORACT)));
            communityUnit.setPrivateFacilityForMrdt(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORMRDT)));
            communityUnit.setNameOfNgoDoingIccm(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGICCM)));
            communityUnit.setNameOfNgoDoingMhealth(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGMHEALTH)));
            communityUnit.setDateAdded(cursor.getInt(cursor.getColumnIndex(DATEADDED)));
            communityUnit.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDEDBY)));
            communityUnit.setNumberOfChvs(cursor.getInt(cursor.getColumnIndex(NUMBEROFCHVS)));
            communityUnit.setHouseholdPerChv(cursor.getInt(cursor.getColumnIndex(HOUSEHOLDPERCHV)));
            communityUnit.setNumberOfVillages(cursor.getInt(cursor.getColumnIndex(NUMBEROFVILLAGES)));
            communityUnit.setDistanceToBranch(cursor.getInt(cursor.getColumnIndex(DISTANCETOBRANCH)));
            communityUnit.setTransportCost(cursor.getInt(cursor.getColumnIndex(TRANSPORTCOST)));
            communityUnit.setDistanceTOMainRoad(cursor.getInt(cursor.getColumnIndex(DISTANCETOMAINROAD)));
            communityUnit.setNoOfHouseholds(cursor.getInt(cursor.getColumnIndex(NOOFHOUSEHOLDS)));
            communityUnit.setMohPoplationDensity(cursor.getInt(cursor.getColumnIndex(MOHPOPLATIONDENSITY)));
            communityUnit.setEstimatedPopulationDensity(cursor.getInt(cursor.getColumnIndex(ESTIMATEDPOPULATIONDENSITY)));
            communityUnit.setDistanceTONearestHealthFacility(cursor.getInt(cursor.getColumnIndex(DISTANCETONEARESTHEALTHFACILITY)));
            communityUnit.setActLevels(cursor.getInt(cursor.getColumnIndex(ACTLEVELS)));
            communityUnit.setActPrice(cursor.getInt(cursor.getColumnIndex(ACTPRICE)));
            communityUnit.setMrdtLevels(cursor.getInt(cursor.getColumnIndex(MRDTLEVELS)));
            communityUnit.setMrdtPrice(cursor.getInt(cursor.getColumnIndex(MRDTPRICE)));
            communityUnit.setNoOfDistibutors(cursor.getInt(cursor.getColumnIndex(NOOFDISTIBUTORS)));
            communityUnit.setChvsTrained((cursor.getInt(cursor.getColumnIndex(CHVSTRAINED)) ==  1));
            communityUnit.setPresenceOfEstates((cursor.getInt(cursor.getColumnIndex(PRESENCEOFESTATES)) == 1));
            communityUnit.setPresenceOfFactories((cursor.getLong(cursor.getColumnIndex(PRESENCEOFFACTORIES))));
            communityUnit.setPresenceOfHostels((cursor.getInt(cursor.getColumnIndex(PRESENCEOFHOSTELS)) ==  1));
            communityUnit.setTraderMarket((cursor.getInt(cursor.getColumnIndex(TRADERMARKET)) ==  1));
            communityUnit.setLargeSupermarket((cursor.getInt(cursor.getColumnIndex(LARGESUPERMARKET)) ==  1));
            communityUnit.setNgosGivingFreeDrugs((cursor.getInt(cursor.getColumnIndex(NGOSGIVINGFREEDRUGS)) ==  1));
            communityUnit.setNgoDoingIccm((cursor.getInt(cursor.getColumnIndex(NGODOINGICCM)) ==  1));
            communityUnit.setNgoDoingMhealth((cursor.getInt(cursor.getColumnIndex(NGODOINGMHEALTH)) ==  1));

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

            communityUnit.setId(cursor.getString(cursor.getColumnIndex(ID)));
            communityUnit.setCommunityUnitName(cursor.getString(cursor.getColumnIndex(NAME)));
            communityUnit.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPINGID)));
            communityUnit.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
            communityUnit.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));
            communityUnit.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            communityUnit.setSubCountyId(cursor.getString(cursor.getColumnIndex(SUBCOUNTYID)));
            communityUnit.setLinkFacilityId(cursor.getString(cursor.getColumnIndex(LINKFACILITYID)));
            communityUnit.setAreaChiefName(cursor.getString(cursor.getColumnIndex(AREACHIEFNAME)));
            communityUnit.setWard(cursor.getString(cursor.getColumnIndex(WARD)));
            communityUnit.setEconomicStatus(cursor.getString(cursor.getColumnIndex(ECONOMICSTATUS)));
            communityUnit.setPrivateFacilityForAct(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORACT)));
            communityUnit.setPrivateFacilityForMrdt(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORMRDT)));
            communityUnit.setNameOfNgoDoingIccm(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGICCM)));
            communityUnit.setNameOfNgoDoingMhealth(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGMHEALTH)));
            communityUnit.setDateAdded(cursor.getInt(cursor.getColumnIndex(DATEADDED)));
            communityUnit.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDEDBY)));
            communityUnit.setNumberOfChvs(cursor.getInt(cursor.getColumnIndex(NUMBEROFCHVS)));
            communityUnit.setHouseholdPerChv(cursor.getInt(cursor.getColumnIndex(HOUSEHOLDPERCHV)));
            communityUnit.setNumberOfVillages(cursor.getInt(cursor.getColumnIndex(NUMBEROFVILLAGES)));
            communityUnit.setDistanceToBranch(cursor.getInt(cursor.getColumnIndex(DISTANCETOBRANCH)));
            communityUnit.setTransportCost(cursor.getInt(cursor.getColumnIndex(TRANSPORTCOST)));
            communityUnit.setDistanceTOMainRoad(cursor.getInt(cursor.getColumnIndex(DISTANCETOMAINROAD)));
            communityUnit.setNoOfHouseholds(cursor.getInt(cursor.getColumnIndex(NOOFHOUSEHOLDS)));
            communityUnit.setMohPoplationDensity(cursor.getInt(cursor.getColumnIndex(MOHPOPLATIONDENSITY)));
            communityUnit.setEstimatedPopulationDensity(cursor.getInt(cursor.getColumnIndex(ESTIMATEDPOPULATIONDENSITY)));
            communityUnit.setDistanceTONearestHealthFacility(cursor.getInt(cursor.getColumnIndex(DISTANCETONEARESTHEALTHFACILITY)));
            communityUnit.setActLevels(cursor.getInt(cursor.getColumnIndex(ACTLEVELS)));
            communityUnit.setActPrice(cursor.getInt(cursor.getColumnIndex(ACTPRICE)));
            communityUnit.setMrdtLevels(cursor.getInt(cursor.getColumnIndex(MRDTLEVELS)));
            communityUnit.setMrdtPrice(cursor.getInt(cursor.getColumnIndex(MRDTPRICE)));
            communityUnit.setNoOfDistibutors(cursor.getInt(cursor.getColumnIndex(NOOFDISTIBUTORS)));
            communityUnit.setChvsTrained((cursor.getInt(cursor.getColumnIndex(CHVSTRAINED)) ==  1));
            communityUnit.setPresenceOfEstates((cursor.getInt(cursor.getColumnIndex(PRESENCEOFESTATES)) == 1));
            communityUnit.setPresenceOfFactories((cursor.getLong(cursor.getColumnIndex(PRESENCEOFFACTORIES))));
            communityUnit.setPresenceOfHostels((cursor.getInt(cursor.getColumnIndex(PRESENCEOFHOSTELS)) ==  1));
            communityUnit.setTraderMarket((cursor.getInt(cursor.getColumnIndex(TRADERMARKET)) ==  1));
            communityUnit.setLargeSupermarket((cursor.getInt(cursor.getColumnIndex(LARGESUPERMARKET)) ==  1));
            communityUnit.setNgosGivingFreeDrugs((cursor.getInt(cursor.getColumnIndex(NGOSGIVINGFREEDRUGS)) ==  1));
            communityUnit.setNgoDoingIccm((cursor.getInt(cursor.getColumnIndex(NGODOINGICCM)) ==  1));
            communityUnit.setNgoDoingMhealth((cursor.getInt(cursor.getColumnIndex(NGODOINGMHEALTH)) ==  1));

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

            communityUnit.setId(cursor.getString(cursor.getColumnIndex(ID)));
            communityUnit.setCommunityUnitName(cursor.getString(cursor.getColumnIndex(NAME)));
            communityUnit.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPINGID)));
            communityUnit.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
            communityUnit.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));
            communityUnit.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            communityUnit.setSubCountyId(cursor.getString(cursor.getColumnIndex(SUBCOUNTYID)));
            communityUnit.setLinkFacilityId(cursor.getString(cursor.getColumnIndex(LINKFACILITYID)));
            communityUnit.setAreaChiefName(cursor.getString(cursor.getColumnIndex(AREACHIEFNAME)));
            communityUnit.setWard(cursor.getString(cursor.getColumnIndex(WARD)));
            communityUnit.setEconomicStatus(cursor.getString(cursor.getColumnIndex(ECONOMICSTATUS)));
            communityUnit.setPrivateFacilityForAct(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORACT)));
            communityUnit.setPrivateFacilityForMrdt(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORMRDT)));
            communityUnit.setNameOfNgoDoingIccm(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGICCM)));
            communityUnit.setNameOfNgoDoingMhealth(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGMHEALTH)));
            communityUnit.setDateAdded(cursor.getInt(cursor.getColumnIndex(DATEADDED)));
            communityUnit.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDEDBY)));
            communityUnit.setNumberOfChvs(cursor.getInt(cursor.getColumnIndex(NUMBEROFCHVS)));
            communityUnit.setHouseholdPerChv(cursor.getInt(cursor.getColumnIndex(HOUSEHOLDPERCHV)));
            communityUnit.setNumberOfVillages(cursor.getInt(cursor.getColumnIndex(NUMBEROFVILLAGES)));
            communityUnit.setDistanceToBranch(cursor.getInt(cursor.getColumnIndex(DISTANCETOBRANCH)));
            communityUnit.setTransportCost(cursor.getInt(cursor.getColumnIndex(TRANSPORTCOST)));
            communityUnit.setDistanceTOMainRoad(cursor.getInt(cursor.getColumnIndex(DISTANCETOMAINROAD)));
            communityUnit.setNoOfHouseholds(cursor.getInt(cursor.getColumnIndex(NOOFHOUSEHOLDS)));
            communityUnit.setMohPoplationDensity(cursor.getInt(cursor.getColumnIndex(MOHPOPLATIONDENSITY)));
            communityUnit.setEstimatedPopulationDensity(cursor.getInt(cursor.getColumnIndex(ESTIMATEDPOPULATIONDENSITY)));
            communityUnit.setDistanceTONearestHealthFacility(cursor.getInt(cursor.getColumnIndex(DISTANCETONEARESTHEALTHFACILITY)));
            communityUnit.setActLevels(cursor.getInt(cursor.getColumnIndex(ACTLEVELS)));
            communityUnit.setActPrice(cursor.getInt(cursor.getColumnIndex(ACTPRICE)));
            communityUnit.setMrdtLevels(cursor.getInt(cursor.getColumnIndex(MRDTLEVELS)));
            communityUnit.setMrdtPrice(cursor.getInt(cursor.getColumnIndex(MRDTPRICE)));
            communityUnit.setNoOfDistibutors(cursor.getInt(cursor.getColumnIndex(NOOFDISTIBUTORS)));
            communityUnit.setChvsTrained((cursor.getInt(cursor.getColumnIndex(CHVSTRAINED)) ==  1));
            communityUnit.setPresenceOfEstates((cursor.getInt(cursor.getColumnIndex(PRESENCEOFESTATES)) == 1));
            communityUnit.setPresenceOfFactories((cursor.getLong(cursor.getColumnIndex(PRESENCEOFFACTORIES))));
            communityUnit.setPresenceOfHostels((cursor.getInt(cursor.getColumnIndex(PRESENCEOFHOSTELS)) ==  1));
            communityUnit.setTraderMarket((cursor.getInt(cursor.getColumnIndex(TRADERMARKET)) ==  1));
            communityUnit.setLargeSupermarket((cursor.getInt(cursor.getColumnIndex(LARGESUPERMARKET)) ==  1));
            communityUnit.setNgosGivingFreeDrugs((cursor.getInt(cursor.getColumnIndex(NGOSGIVINGFREEDRUGS)) ==  1));
            communityUnit.setNgoDoingIccm((cursor.getInt(cursor.getColumnIndex(NGODOINGICCM)) ==  1));
            communityUnit.setNgoDoingMhealth((cursor.getInt(cursor.getColumnIndex(NGODOINGMHEALTH)) ==  1));

            communityUnitList.add(communityUnit);
        }
        db.close();

        return communityUnitList;
    }

    public void deleteCommunityUnit (CommunityUnit communityUnit){
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[] { communityUnit.getId() });
    }


    public Cursor getCommunityUnitDataCursor() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
    }

    //JSON
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
                results.put(CU_JSON_ROOT, resultSet);
            } catch (JSONException e) {

            }
        }
        cursor.close();
        db.close();
        return results;
    }

    public void CuFromJson (JSONObject jsonObject){
        Log.d("Tremap", "+++++++++++++++++++++++++++++++++++++++");
        Log.d("Tremap", "Trying to Save teh CU from JSON");
        CommunityUnit communityUnit = new CommunityUnit();
        try {

            communityUnit.setId(jsonObject.getString(ID));
            communityUnit.setCommunityUnitName(jsonObject.getString(NAME));
            communityUnit.setMappingId(jsonObject.getString(MAPPINGID));
            communityUnit.setLat(jsonObject.getDouble(LAT));
            communityUnit.setLon(jsonObject.getDouble(LON));
            communityUnit.setCountry(jsonObject.getString(COUNTRY));
            communityUnit.setSubCountyId(jsonObject.getString(SUBCOUNTYID));
            communityUnit.setLinkFacilityId(jsonObject.getString(LINKFACILITYID));
            communityUnit.setAreaChiefName(jsonObject.getString(AREACHIEFNAME));
            communityUnit.setWard(jsonObject.getString(WARD));
            communityUnit.setEconomicStatus(jsonObject.getString(ECONOMICSTATUS));
            communityUnit.setPrivateFacilityForAct(jsonObject.getString(PRIVATEFACILITYFORACT));
            communityUnit.setPrivateFacilityForMrdt(jsonObject.getString(PRIVATEFACILITYFORMRDT));
            communityUnit.setNameOfNgoDoingIccm(jsonObject.getString(NAMEOFNGODOINGICCM));
            communityUnit.setNameOfNgoDoingMhealth(jsonObject.getString(NAMEOFNGODOINGMHEALTH));
            communityUnit.setDateAdded(jsonObject.getLong(DATEADDED));
            communityUnit.setAddedBy(jsonObject.getInt(ADDEDBY));
            communityUnit.setNumberOfChvs(jsonObject.getLong(NUMBEROFCHVS));
            communityUnit.setHouseholdPerChv(jsonObject.getLong(HOUSEHOLDPERCHV));
            communityUnit.setNumberOfVillages(jsonObject.getLong(NUMBEROFVILLAGES));
            communityUnit.setDistanceToBranch(jsonObject.getLong(DISTANCETOBRANCH));
            communityUnit.setTransportCost(jsonObject.getLong(TRANSPORTCOST));
            communityUnit.setDistanceTOMainRoad(jsonObject.getLong(DISTANCETOMAINROAD));
            communityUnit.setNoOfHouseholds(jsonObject.getLong(NOOFHOUSEHOLDS));
            communityUnit.setMohPoplationDensity(jsonObject.getLong(MOHPOPLATIONDENSITY));
            communityUnit.setEstimatedPopulationDensity(jsonObject.getLong(ESTIMATEDPOPULATIONDENSITY));
            communityUnit.setDistanceTONearestHealthFacility(jsonObject.getLong(DISTANCETONEARESTHEALTHFACILITY));
            communityUnit.setActLevels(jsonObject.getLong(ACTLEVELS));
            communityUnit.setActPrice(jsonObject.getLong(ACTPRICE));
            communityUnit.setMrdtLevels(jsonObject.getLong(MRDTLEVELS));
            communityUnit.setMrdtPrice(jsonObject.getLong(MRDTPRICE));
            communityUnit.setNoOfDistibutors(jsonObject.getLong(NOOFDISTIBUTORS));
            communityUnit.setChvsTrained(jsonObject.getString(CHVSTRAINED).equalsIgnoreCase("1"));
            communityUnit.setPresenceOfEstates(jsonObject.getString(PRESENCEOFESTATES).equalsIgnoreCase("1"));
            communityUnit.setPresenceOfFactories(jsonObject.getLong(PRESENCEOFFACTORIES));
            communityUnit.setPresenceOfHostels(jsonObject.getString(PRESENCEOFHOSTELS).equalsIgnoreCase("1"));
            communityUnit.setTraderMarket(jsonObject.getString(TRADERMARKET).equalsIgnoreCase("1"));
            communityUnit.setLargeSupermarket(jsonObject.getString(LARGESUPERMARKET).equalsIgnoreCase("1"));
            communityUnit.setNgosGivingFreeDrugs(jsonObject.getString(NGOSGIVINGFREEDRUGS).equalsIgnoreCase("1"));
            communityUnit.setNgoDoingIccm(jsonObject.getString(NGODOINGICCM).equalsIgnoreCase("1"));
            communityUnit.setNgoDoingMhealth(jsonObject.getString(NGODOINGMHEALTH).equalsIgnoreCase("1"));

            addCommunityUnitData(communityUnit);
        }catch (Exception e){
            Log.d("Tremap", "+++++++++++++++++++++++++++++++++++++++");
            Log.d("Tremap", "CU ERROR IN CREATING CU FROM JSON");
            Log.d("Tremap", "CE ERROR "+e.getMessage());
        }
    }

    private void upgradeVersion2(SQLiteDatabase db) {}
}

