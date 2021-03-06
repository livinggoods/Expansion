package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Village;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 2/28/17.
 */

public class VillageTable extends SQLiteOpenHelper {
    public static final String TABLE_NAME="village";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    public static String varchar_field = " varchar(512) ";
    public static String real_field = " REAL ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String VILLAGENAME = "village_name";
    public static final String MAPPINGID = "mapping_id";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String COUNTRY = "country";
    public static final String DISTRICT = "district";
    public static final String COUNTY = "county";
    public static final String SUBCOUNTYID = "sub_county_id";
    public static final String PARISH = "parish";
    public static final String COMMUNITY_UNIT = "community_unit";
    public static final String WARD = "ward";
    public static final String LINKFACILITYID = "link_facility_id";
    public static final String AREACHIEFNAME = "area_chief_name";
    public static final String AREACHIEFPHONE = "area_chief_phone";
    public static final String DISTANCETOBRANCH = "distancetobranch";
    public static final String TRANSPORTCOST = "transportcost";
    public static final String DISTANCETOMAINROAD = "distancetomainroad";
    public static final String NOOFHOUSEHOLDS = "noofhouseholds";
    public static final String MOHPOPLATIONDENSITY = "mohpoplationdensity";
    public static final String ESTIMATEDPOPULATIONDENSITY = "estimatedpopulationdensity";
    public static final String ECONOMICSTATUS = "economic_status";
    public static final String DISTANCETONEARESTHEALTHFACILITY = "distancetonearesthealthfacility";
    public static final String ACTLEVELS = "actlevels";
    public static final String ACTPRICE = "actprice";
    public static final String MRDTLEVELS = "mrdtlevels";
    public static final String MRDTPRICE = "mrdtprice";
    public static final String PRESENCEOFHOSTELS = "presenceofhostels";
    public static final String PRESENCEOFESTATES = "presenceofestates";
    public static final String NUMBEROFFACTORIES = "number_of_factories";
    public static final String PRESENCEOFDISTRIBUTORS = "presenceofdistibutors";
    public static final String DISTRIBUTORSINTHEAREA = "name_of_distibutors";
    public static final String TRADERMARKET = "tradermarket";
    public static final String LARGESUPERMARKET = "largesupermarket";
    public static final String NGOSGIVINGFREEDRUGS = "ngosgivingfreedrugs";
    public static final String NGODOINGICCM = "ngodoingiccm";
    public static final String NGODOINGMHEALTH = "ngodoingmhealth";
    public static final String NAMEOFNGODOINGICCM = "nameofngodoingiccm";
    public static final String NAMEOFNGODOINGMHEALTH = "nameofngodoingmhealth";
    public static final String PRIVATEFACILITYFORACT = "privatefacilityforact";
    public static final String PRIVATEFACILITYFORMRDT = "privatefacilityformrdt";
    public static final String SYNCED = "synced";
    public static final String CHVS_TRAINED = "chvs_trained";
    public static final String DATEADDED = "dateadded";
    public static final String ADDEDBY = "addedby";
    public static final String COMMENT = "comment";
    public static final String BRAC_OPERATING = "brac_operating";
    public static final String MTN = "mtn_signal";
    public static final String SAFARICOM = "safaricom_signal";
    public static final String AIRTEL = "airtel_signal";
    public static final String ORANGE = "orange_signal";
    public static final String ACTSTOCK = "act_stock";
    public static final String JSON_ROOT = "villages";


    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ","
            + VILLAGENAME + varchar_field + ","
            + MAPPINGID + varchar_field + ","
            + LAT + real_field + ","
            + LON + real_field + ","
            + COUNTRY + varchar_field + ","
            + DISTRICT + varchar_field + ","
            + COUNTY + varchar_field + ","
            + SUBCOUNTYID + varchar_field + ","
            + PARISH + varchar_field + ","
            + COMMUNITY_UNIT + varchar_field + ","
            + WARD + varchar_field + ","
            + LINKFACILITYID + varchar_field + ","
            + AREACHIEFNAME + varchar_field + ","
            + AREACHIEFPHONE + varchar_field + ","
            + DISTANCETOBRANCH + varchar_field + ","
            + TRANSPORTCOST + varchar_field + ","
            + DISTANCETOMAINROAD + varchar_field + ","
            + NOOFHOUSEHOLDS + text_field + ","
            + MOHPOPLATIONDENSITY + text_field + ","
            + ESTIMATEDPOPULATIONDENSITY + integer_field + ","
            + ECONOMICSTATUS + varchar_field + ","
            + DISTANCETONEARESTHEALTHFACILITY + integer_field + ","
            + ACTLEVELS + integer_field + ","
            + ACTPRICE + integer_field + ","
            + MRDTLEVELS + integer_field + ","
            + MRDTPRICE + integer_field + ","
            + PRESENCEOFHOSTELS + integer_field + ","
            + PRESENCEOFESTATES + integer_field + ","
            + NUMBEROFFACTORIES + integer_field + ","
            + PRESENCEOFDISTRIBUTORS + integer_field + ","
            + DISTRIBUTORSINTHEAREA + text_field + ","
            + TRADERMARKET + integer_field + ","
            + LARGESUPERMARKET + integer_field + ","
            + NGOSGIVINGFREEDRUGS + integer_field + ","
            + NGODOINGICCM + integer_field + ","
            + NGODOINGMHEALTH + integer_field + ","
            + NAMEOFNGODOINGICCM + integer_field + ","
            + NAMEOFNGODOINGMHEALTH + integer_field + ","
            + PRIVATEFACILITYFORACT + integer_field + ","
            + PRIVATEFACILITYFORMRDT + integer_field + ","
            + DATEADDED + integer_field + ","
            + ADDEDBY + integer_field + ","
            + SYNCED + integer_field + ","
            + CHVS_TRAINED + integer_field + ","
            + BRAC_OPERATING + integer_field + ","
            + MTN + integer_field + ","
            + SAFARICOM + integer_field + ","
            + ORANGE + integer_field + ","
            + AIRTEL + integer_field + ","
            + ACTSTOCK + integer_field + ","
            + COMMENT + text_field + ");";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;
    public static final String[] columns = { ID, VILLAGENAME, MAPPINGID, LAT, LON, COUNTRY, DISTRICT,
            COUNTY, SUBCOUNTYID, PARISH, COMMUNITY_UNIT, WARD, LINKFACILITYID, AREACHIEFNAME,
            AREACHIEFPHONE, DISTANCETOBRANCH, TRANSPORTCOST, DISTANCETOMAINROAD, NOOFHOUSEHOLDS,
            MOHPOPLATIONDENSITY, ESTIMATEDPOPULATIONDENSITY, ECONOMICSTATUS,
            DISTANCETONEARESTHEALTHFACILITY, ACTLEVELS, ACTPRICE, MRDTLEVELS, MRDTPRICE,
            PRESENCEOFHOSTELS, PRESENCEOFESTATES, NUMBEROFFACTORIES, PRESENCEOFDISTRIBUTORS,
            DISTRIBUTORSINTHEAREA, TRADERMARKET, LARGESUPERMARKET, NGOSGIVINGFREEDRUGS, NGODOINGICCM,
            NGODOINGMHEALTH, NAMEOFNGODOINGICCM, NAMEOFNGODOINGMHEALTH, PRIVATEFACILITYFORACT,
            PRIVATEFACILITYFORMRDT, DATEADDED, ADDEDBY, COMMENT, SYNCED, CHVS_TRAINED, BRAC_OPERATING,
            SAFARICOM, MTN, AIRTEL, ORANGE, ACTSTOCK};

    public static final String DB_UPDATE_V2 = "ALTER TABLE " + TABLE_NAME +
            "  ADD "+ ACTSTOCK + integer_field +";";

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
        if (oldVersion < 2){
            upgradeVersion2(db);

        }
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
        cv.put(DISTRICT, village.getDistrict());
        cv.put(COUNTY, village.getCounty());
        cv.put(SUBCOUNTYID, village.getSubCountyId());
        cv.put(PARISH, village.getParish());
        cv.put(COMMUNITY_UNIT, village.getCommunityUnit());
        cv.put(WARD, village.getWard());
        cv.put(LINKFACILITYID, village.getLinkFacilityId());
        cv.put(AREACHIEFNAME, village.getAreaChiefName());
        cv.put(AREACHIEFPHONE, village.getAreaChiefPhone());
        cv.put(DISTANCETOBRANCH, village.getDistanceToBranch());
        cv.put(TRANSPORTCOST, village.getTransportCost());
        cv.put(DISTANCETOMAINROAD, village.getDistanceToMainRoad());
        cv.put(NOOFHOUSEHOLDS, village.getNoOfHouseholds());
        cv.put(MOHPOPLATIONDENSITY, village.getMohPoplationDensity());
        cv.put(ESTIMATEDPOPULATIONDENSITY, village.getEstimatedPopulationDensity());
        cv.put(ECONOMICSTATUS, village.getEconomicStatus());
        cv.put(DISTANCETONEARESTHEALTHFACILITY, village.getDistanceToNearestHealthFacility());
        cv.put(ACTLEVELS, village.getActLevels());
        cv.put(ACTPRICE, village.getActPrice());
        cv.put(MRDTLEVELS, village.getMrdtLevels());
        cv.put(MRDTPRICE, village.getMrdtPrice());
        cv.put(PRESENCEOFHOSTELS, village.getPresenceOfHostels());
        cv.put(PRESENCEOFESTATES, village.getPresenceOfEstates());
        cv.put(NUMBEROFFACTORIES, village.getNumberOfFactories());
        cv.put(PRESENCEOFDISTRIBUTORS, village.getPresenceOfDistributors());
        cv.put(DISTRIBUTORSINTHEAREA, village.getDistributorsInTheArea());
        cv.put(TRADERMARKET, village.getTraderMarket());
        cv.put(LARGESUPERMARKET, village.getLargeSupermarket());
        cv.put(NGOSGIVINGFREEDRUGS, village.getNgosGivingFreeDrugs());
        cv.put(NGODOINGICCM, village.getNgoDoingIccm());
        cv.put(NGODOINGMHEALTH, village.getNgoDoingMhealth());
        cv.put(NAMEOFNGODOINGICCM, village.getNameOfNgoDoingIccm());
        cv.put(NAMEOFNGODOINGMHEALTH, village.getNameOfNgoDoingMhealth());
        cv.put(PRIVATEFACILITYFORACT, village.getPrivateFacilityForAct());
        cv.put(PRIVATEFACILITYFORMRDT, village.getPrivateFacilityForMrdt());
        cv.put(DATEADDED, village.getDateAdded());
        cv.put(ADDEDBY, village.getAddedBy());
        cv.put(COMMENT, village.getComment());
        cv.put(SYNCED, village.getSynced());
        cv.put(CHVS_TRAINED, village.getChvsTrained());
        cv.put(BRAC_OPERATING, village.isBracOperating() ? 1 : 0);
        cv.put(SAFARICOM, village.getSafaricomSignalStrength());
        cv.put(MTN, village.getMtnSignalStrength());
        cv.put(AIRTEL, village.getAirtelSignalStrength());
        cv.put(ORANGE, village.getOrangeSignalStrength());
        cv.put(ACTSTOCK, village.getActStock() ? 1 : 0);

        long id;
        if (isExist(village)){
            id = db.update(TABLE_NAME, cv, ID+"='"+village.getId()+"'", null);
            Log.d("Tremap DB Op", "Village updated");
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Tremap DB Op", "Village created with id "+ village.getId());
        }
        db.close();
        return id;

    }
    public boolean isExist(Village village) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + village.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public Village getVillageById(String id) {
        SQLiteDatabase db=getReadableDatabase();

        String whereClause = ID+" = ? ";

        String[] whereArgs = new String[] {
                id
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else {
            Village village = cursorToVillage(cursor);
            db.close();
            return village;
        }
    }

    public List<Village> getVillageData() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        List<Village> villages = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            villages.add(cursorToVillage(cursor));
        }
        db.close();
        return villages;
    }


    public List<Village> getVillagesByLinkFacility(String linkFacilityId) {
        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATEADDED +" desc";
        String whereClause = LINKFACILITYID+" = ?";
        String[] whereArgs = new String[] {
                linkFacilityId,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);
        List<Village> villages = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            villages.add(cursorToVillage(cursor));
        }
        db.close();
        return villages;
    }


    public List<Village> getVillageDataByParishId(String parishUUID) {
        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATEADDED +" desc";
        String whereClause = PARISH+" = ?";
        String[] whereArgs = new String[] {
                parishUUID,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);

        List<Village> villages = new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            villages.add(cursorToVillage(cursor));
        }
        db.close();

        return villages;
    }

    public Cursor getVillageDataCursor() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
    }
    private void upgradeVersion2(SQLiteDatabase db) {
        if (!isFieldExist(db, ACTSTOCK)){
            db.execSQL(DB_UPDATE_V2);
        }
    }

    public boolean isFieldExist(SQLiteDatabase db, String fieldName)
    {
        boolean isExist = false;
        Cursor res = null;
        try {
            res = db.rawQuery("Select * from "+ TABLE_NAME +" limit 1", null);
            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex!=-1){
                isExist = true;
            }

        } catch (Exception e) {
        } finally {
            try { if (res !=null){ res.close(); } } catch (Exception e1) {}
        }
        return isExist;
    }

    public long getAllRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                null,
                null);
        db.close();
        return cnt;
    }

    public long getPendingRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                SYNCED + "=?",
                new String[] {String.valueOf(Constants.SYNC_STATUS_UNSYNCED)});
        db.close();
        return cnt;
    }

    public JSONObject getPayload(int offset) {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,
                SYNCED + "=?",new String[]{Constants.SYNC_STATUS_UNSYNCED + ""},null,null,null,
                String.format("%d,%d", offset, Constants.SYNC_PAGINATION_SIZE ));
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
                        e.printStackTrace();
                    }
                }
            }
            resultSet.put(rowObject);
            try {
                results.put(JSON_ROOT, resultSet);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return results;
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
                results.put(JSON_ROOT, resultSet);
            } catch (JSONException e) {

            }
        }
        cursor.close();
        db.close();
        return results;
    }

    public void fromJson(JSONObject jsonObject){
        Village village = new Village();
        try {
            village.setId(jsonObject.getString(ID));
            if (!jsonObject.isNull(VILLAGENAME)){
                village.setVillageName(jsonObject.getString(VILLAGENAME));
            }
            if (!jsonObject.isNull(MAPPINGID)){
                village.setMappingId(jsonObject.getString(MAPPINGID));
            }
            if (!jsonObject.isNull(LAT)){
                village.setLat(jsonObject.getDouble(LAT));
            }
            if (!jsonObject.isNull(LON)){
                village.setLon(jsonObject.getDouble(LON));
            }
            if(!jsonObject.isNull(COUNTRY)){
                village.setCountry(jsonObject.getString(COUNTRY));
            }
            if(!jsonObject.isNull(DISTRICT)){
                village.setDistrict(jsonObject.getString(DISTRICT));
            }
            if(!jsonObject.isNull(COUNTY)){
                village.setCounty(jsonObject.getString(COUNTY));
            }
            if(!jsonObject.isNull(SUBCOUNTYID)){
                village.setSubCountyId(jsonObject.getString(SUBCOUNTYID));
            }
            if(!jsonObject.isNull(PARISH)){
                village.setParish(jsonObject.getString(PARISH));
            }
            if(!jsonObject.isNull(COMMUNITY_UNIT)){
                village.setCommunityUnit(jsonObject.getString(COMMUNITY_UNIT));
            }
            if(!jsonObject.isNull(WARD)){
                village.setWard(jsonObject.getString(WARD));
            }
            if(!jsonObject.isNull(LINKFACILITYID)){
                village.setLinkFacilityId(jsonObject.getString(LINKFACILITYID));
            }
            if(!jsonObject.isNull(AREACHIEFNAME)){
                village.setAreaChiefName(jsonObject.getString(AREACHIEFNAME));
            }
            if(!jsonObject.isNull(AREACHIEFPHONE)){
                village.setAreaChiefPhone(jsonObject.getString(AREACHIEFPHONE));
            }
            if(!jsonObject.isNull(DISTANCETOBRANCH)){
                village.setDistanceToBranch(jsonObject.getLong(DISTANCETOBRANCH));
            }
            if(!jsonObject.isNull(TRANSPORTCOST)){
                village.setTransportCost(jsonObject.getLong(TRANSPORTCOST));
            }
            if(!jsonObject.isNull(DISTANCETOMAINROAD)){
                village.setDistanceToMainRoad(jsonObject.getLong(DISTANCETOMAINROAD));
            }
            if(!jsonObject.isNull(NOOFHOUSEHOLDS)){
                village.setNoOfHouseholds(jsonObject.getLong(NOOFHOUSEHOLDS));
            }
            if(!jsonObject.isNull(MOHPOPLATIONDENSITY)){
                village.setMohPoplationDensity(jsonObject.getLong(MOHPOPLATIONDENSITY));
            }
            if(!jsonObject.isNull(ESTIMATEDPOPULATIONDENSITY)){
                village.setEstimatedPopulationDensity(jsonObject.getLong(ESTIMATEDPOPULATIONDENSITY));
            }
            if(!jsonObject.isNull(ECONOMICSTATUS)){
                village.setEconomicStatus(jsonObject.getString(ECONOMICSTATUS));
            }
            if(!jsonObject.isNull(DISTANCETONEARESTHEALTHFACILITY)){
                village.setDistanceToNearestHealthFacility(jsonObject.getLong(DISTANCETONEARESTHEALTHFACILITY));
            }
            if(!jsonObject.isNull(ACTLEVELS)){
                village.setActLevels(jsonObject.getLong(ACTLEVELS));
            }
            if(!jsonObject.isNull(ACTPRICE)){
                village.setActPrice(jsonObject.getLong(ACTPRICE));
            }
            if(!jsonObject.isNull(MRDTLEVELS)){
                village.setMrdtLevels(jsonObject.getLong(MRDTLEVELS));
            }
            if(!jsonObject.isNull(MRDTPRICE)){
                village.setMrdtPrice(jsonObject.getLong(MRDTPRICE));
            }
            if(!jsonObject.isNull(PRESENCEOFHOSTELS)){
                village.setPresenceOfHostels(jsonObject.getInt(PRESENCEOFHOSTELS)==1);
            }
            if(!jsonObject.isNull(PRESENCEOFESTATES)){
                village.setPresenceOfEstates(jsonObject.getInt(PRESENCEOFESTATES)==1);
            }
            if(!jsonObject.isNull(NUMBEROFFACTORIES)){
                village.setNumberOfFactories(jsonObject.getInt(NUMBEROFFACTORIES));
            }
            if(!jsonObject.isNull(PRESENCEOFDISTRIBUTORS)){
                village.setPresenceOfDistributors(jsonObject.getInt(PRESENCEOFDISTRIBUTORS)==1);
            }
            if(!jsonObject.isNull(DISTRIBUTORSINTHEAREA)){
                village.setDistributorsInTheArea(jsonObject.getString(DISTRIBUTORSINTHEAREA));
            }
            if(!jsonObject.isNull(TRADERMARKET)){
                village.setTraderMarket(jsonObject.getInt(TRADERMARKET)==1);
            }
            if(!jsonObject.isNull(LARGESUPERMARKET)){
                village.setLargeSupermarket(jsonObject.getInt(LARGESUPERMARKET)==1);
            }
            if(!jsonObject.isNull(NGOSGIVINGFREEDRUGS)){
                village.setNgosGivingFreeDrugs(jsonObject.getInt(NGOSGIVINGFREEDRUGS)==1);
            }
            if(!jsonObject.isNull(NGODOINGICCM)){
                village.setNgoDoingIccm(jsonObject.getInt(NGODOINGICCM)==1);
            }
            if(!jsonObject.isNull(NGODOINGMHEALTH)){
                village.setNgoDoingMhealth(jsonObject.getInt(NGODOINGMHEALTH)==1);
            }
            if(!jsonObject.isNull(NAMEOFNGODOINGICCM)){
                village.setNameOfNgoDoingIccm(jsonObject.getString(NAMEOFNGODOINGICCM));
            }
            if(!jsonObject.isNull(NAMEOFNGODOINGMHEALTH)){
                village.setNameOfNgoDoingMhealth(jsonObject.getString(NAMEOFNGODOINGMHEALTH));
            }
            if(!jsonObject.isNull(PRIVATEFACILITYFORACT)){
                village.setPrivateFacilityForAct(jsonObject.getString(PRIVATEFACILITYFORACT));
            }
            if(!jsonObject.isNull(PRIVATEFACILITYFORMRDT)){
                village.setPrivateFacilityForMrdt(jsonObject.getString(PRIVATEFACILITYFORMRDT));
            }
            if(!jsonObject.isNull(DATEADDED)){
                village.setDateAdded(jsonObject.getLong(DATEADDED));
            }
            if(!jsonObject.isNull(ADDEDBY)){
                village.setAddedBy(jsonObject.getInt(ADDEDBY));
            }
            if(!jsonObject.isNull(COMMENT)){
                village.setComment(jsonObject.getString(COMMENT));
            }
            if(!jsonObject.isNull(SYNCED)){
                village.setSynced(jsonObject.getInt(SYNCED));
            }
            if(!jsonObject.isNull(CHVS_TRAINED)){
                village.setChvsTrained(jsonObject.getInt(CHVS_TRAINED)==1);
            }
            if(!jsonObject.isNull(BRAC_OPERATING)){
                village.setBracOperating(jsonObject.getInt(BRAC_OPERATING)==1);
            }
            if(!jsonObject.isNull(SAFARICOM)){
                village.setSafaricomSignalStrength(jsonObject.getInt(SAFARICOM));
            }
            if(!jsonObject.isNull(MTN)){
                village.setMtnSignalStrength(jsonObject.getInt(MTN));
            }
            if(!jsonObject.isNull(AIRTEL)){
                village.setAirtelSignalStrength(jsonObject.getInt(AIRTEL));
            }
            if(!jsonObject.isNull(ORANGE)){
                village.setOrangeSignalStrength(jsonObject.getInt(ORANGE));
            }
            if(!jsonObject.isNull(ACTSTOCK)){
                village.setActStock(jsonObject.getInt(ACTSTOCK)==1);
            }
            this.addData(village);
        }catch (Exception e){
            Log.d("Tremap", e.getMessage());
        }
    }

    private Village cursorToVillage(Cursor cursor){
        Village village = new Village();
        village.setId(cursor.getString(cursor.getColumnIndex(ID)));
        village.setVillageName(cursor.getString(cursor.getColumnIndex(VILLAGENAME)));
        village.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPINGID)));
        village.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
        village.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));
        village.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
        village.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
        village.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
        village.setSubCountyId(cursor.getString(cursor.getColumnIndex(SUBCOUNTYID)));
        village.setParish(cursor.getString(cursor.getColumnIndex(PARISH)));
        village.setCommunityUnit(cursor.getString(cursor.getColumnIndex(COMMUNITY_UNIT)));
        village.setWard(cursor.getString(cursor.getColumnIndex(WARD)));
        village.setLinkFacilityId(cursor.getString(cursor.getColumnIndex(LINKFACILITYID)));
        village.setAreaChiefName(cursor.getString(cursor.getColumnIndex(AREACHIEFNAME)));
        village.setAreaChiefPhone(cursor.getString(cursor.getColumnIndex(AREACHIEFPHONE)));
        village.setDistanceToBranch(cursor.getLong(cursor.getColumnIndex(DISTANCETOBRANCH)));
        village.setTransportCost(cursor.getLong(cursor.getColumnIndex(TRANSPORTCOST)));
        village.setDistanceToMainRoad(cursor.getLong(cursor.getColumnIndex(DISTANCETOMAINROAD)));
        village.setNoOfHouseholds(cursor.getLong(cursor.getColumnIndex(NOOFHOUSEHOLDS)));
        village.setMohPoplationDensity(cursor.getLong(cursor.getColumnIndex(MOHPOPLATIONDENSITY)));
        village.setEstimatedPopulationDensity(cursor.getLong(cursor.getColumnIndex(ESTIMATEDPOPULATIONDENSITY)));
        village.setEconomicStatus(cursor.getString(cursor.getColumnIndex(ECONOMICSTATUS)));
        village.setDistanceToNearestHealthFacility(cursor.getLong(cursor.getColumnIndex(DISTANCETONEARESTHEALTHFACILITY)));
        village.setActLevels(cursor.getLong(cursor.getColumnIndex(ACTLEVELS)));
        village.setActPrice(cursor.getLong(cursor.getColumnIndex(ACTPRICE)));
        village.setMrdtLevels(cursor.getLong(cursor.getColumnIndex(MRDTLEVELS)));
        village.setMrdtPrice(cursor.getLong(cursor.getColumnIndex(MRDTPRICE)));
        village.setPresenceOfHostels(cursor.getInt(cursor.getColumnIndex(PRESENCEOFHOSTELS))==1);
        village.setPresenceOfEstates(cursor.getInt(cursor.getColumnIndex(PRESENCEOFESTATES))==1);
        village.setNumberOfFactories(cursor.getInt(cursor.getColumnIndex(NUMBEROFFACTORIES)));
        village.setPresenceOfDistributors(cursor.getInt(cursor.getColumnIndex(PRESENCEOFDISTRIBUTORS)) ==1);
        village.setDistributorsInTheArea(cursor.getString(cursor.getColumnIndex(DISTRIBUTORSINTHEAREA)));
        village.setTraderMarket(cursor.getInt(cursor.getColumnIndex(TRADERMARKET))==1);
        village.setLargeSupermarket(cursor.getInt(cursor.getColumnIndex(LARGESUPERMARKET))==1);
        village.setNgosGivingFreeDrugs(cursor.getInt(cursor.getColumnIndex(NGOSGIVINGFREEDRUGS))==1);
        village.setNgoDoingIccm(cursor.getInt(cursor.getColumnIndex(NGODOINGICCM))==1);
        village.setNgoDoingMhealth(cursor.getInt(cursor.getColumnIndex(NGODOINGMHEALTH))==1);
        village.setNameOfNgoDoingIccm(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGICCM)));
        village.setNameOfNgoDoingMhealth(cursor.getString(cursor.getColumnIndex(NAMEOFNGODOINGMHEALTH)));
        village.setPrivateFacilityForAct(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORACT)));
        village.setPrivateFacilityForMrdt(cursor.getString(cursor.getColumnIndex(PRIVATEFACILITYFORMRDT)));
        village.setDateAdded(cursor.getLong(cursor.getColumnIndex(DATEADDED)));
        village.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDEDBY)));
        village.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
        village.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED)));
        village.setChvsTrained(cursor.getInt(cursor.getColumnIndex(CHVS_TRAINED))==1);
        village.setBracOperating(cursor.getInt(cursor.getColumnIndex(BRAC_OPERATING))==1);
        village.setSafaricomSignalStrength(cursor.getInt(cursor.getColumnIndex(SAFARICOM)));
        village.setMtnSignalStrength(cursor.getInt(cursor.getColumnIndex(MTN)));
        village.setAirtelSignalStrength(cursor.getInt(cursor.getColumnIndex(AIRTEL)));
        village.setOrangeSignalStrength(cursor.getInt(cursor.getColumnIndex(ORANGE)));
        village.setActStock(cursor.getInt(cursor.getColumnIndex(ACTSTOCK))==1);
        return village;
    }
}

