package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.PartnerActivity;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 8/16/17.
 */



public class PartnerActivityTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="partner_activity";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String varchar_field = " varchar(512) ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static String CU_JSON_ROOT = "partner_activity";

    public static final String ID = "id";
    public static final String PARTNERID = "partner_id";
    public static final String COUNTRY = "country";
    public static final String COUNTY = "county_id";
    public static final String SUBCOUNTY = "sub_county_id";
    public static final String PARISH = "parish_id";
    public static final String VILLAGE = "village_id";
    public static final String COMMUNITYUNIT = "community_unit_id";
    public static final String MAPPINGID = "mapping_id";
    public static final String COMMENT = "comment";
    public static final String DOINGMHEALTH = "doing_mhealth";
    public static final String DOINGICCM = "doing_iccm";
    public static final String GIVINGFREEDRUGS = "giving_free_drugs";
    public static final String GIVINGSTIPEND = "giving_stipend";
    public static final String DATEADDED = "date_added";
    public static final String ADDEDBY = "added_by";
    public static final String ACTIVITIES = "activities";
    public static final String SYNCED = "synced";

    String [] columns=new String[]{ID, PARTNERID, COUNTRY, COUNTY, SUBCOUNTY, PARISH,
            VILLAGE, COMMUNITYUNIT, MAPPINGID, COMMENT, DOINGMHEALTH, DOINGICCM, GIVINGFREEDRUGS,
            GIVINGSTIPEND, DATEADDED, ADDEDBY, ACTIVITIES, SYNCED};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ","
            + PARTNERID + varchar_field + ","
            + COUNTRY + varchar_field + ","
            + COUNTY + varchar_field + ","
            + SUBCOUNTY + varchar_field + ","
            + PARISH + varchar_field + ","
            + VILLAGE + varchar_field + ","
            + COMMUNITYUNIT + integer_field + ","
            + MAPPINGID + integer_field + ","
            + COMMENT + text_field + ","
            + DOINGMHEALTH + integer_field + ","
            + DOINGICCM + integer_field + ","
            + GIVINGFREEDRUGS + integer_field + ","
            + GIVINGSTIPEND + integer_field + ","
            + DATEADDED + integer_field + ","
            + ADDEDBY + integer_field + ","
            + ACTIVITIES + text_field + ","
            + SYNCED + integer_field + ");";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public PartnerActivityTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("RegistrationTable", "upgrading database from" + oldVersion + "to" + newVersion);
        if (oldVersion < 2){
            upgradeVersion2(db);
        }
    }

    public long addData(PartnerActivity partnerActivity) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, partnerActivity.getId());
        cv.put(PARTNERID, partnerActivity.getPartnerId());
        cv.put(COUNTRY, partnerActivity.getCountry());
        cv.put(COUNTY, partnerActivity.getCounty());
        cv.put(SUBCOUNTY, partnerActivity.getSubcounty());
        cv.put(PARISH, partnerActivity.getParish());
        cv.put(VILLAGE, partnerActivity.getVillage());
        cv.put(COMMUNITYUNIT, partnerActivity.getCommunityUnit());
        cv.put(MAPPINGID, partnerActivity.getMappingId());
        cv.put(COMMENT, partnerActivity.getComment());
        cv.put(DOINGMHEALTH, partnerActivity.isDoingMhealth() ? 1:0);
        cv.put(DOINGICCM, partnerActivity.isDoingIccm() ? 1:0);
        cv.put(GIVINGFREEDRUGS, partnerActivity.isGivingFreeDrugs() ? 1:0);
        cv.put(GIVINGSTIPEND, partnerActivity.isGivingStipend() ? 1:0);
        cv.put(DATEADDED, partnerActivity.getDateAdded());
        cv.put(ADDEDBY, partnerActivity.getAddedBy());
        cv.put(ACTIVITIES, partnerActivity.getActivities());
        cv.put(SYNCED, partnerActivity.isSynced() ? 1:0);


        long id;
        if (isPartnerExisting(partnerActivity)){
            Log.d("Tremap", "++++++++++++++++++++++++++++++++++++++++++++");
            Log.d("Tremap", "Updating Partner Activity");
            Log.d("Tremap", "++++++++++++++++++++++++++++++++++++++++++++");
            id = db.update(TABLE_NAME, cv, ID+"='"+partnerActivity.getId()+"'", null);
        }else{
            Log.d("Tremap", "++++++++++++++++++++++++++++++++++++++++++++");
            Log.d("Tremap", "Creating Partner Activity");
            Log.d("Tremap", "++++++++++++++++++++++++++++++++++++++++++++");
            id = db.insert(TABLE_NAME,null,cv);
        }
        db.close();
        return id;
    }

    public boolean isPartnerExisting(PartnerActivity partnerActivity) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + TABLE_NAME + " WHERE " + ID + " = '"
                + partnerActivity.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }

    public List<PartnerActivity> getPartnerActivityData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, columns,null,null,null,null,null,null);
        List<PartnerActivity> partnersActivityList = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            partnersActivityList.add(cursorToPartner(c));
        }
        db.close();
        return partnersActivityList;
    }

    public List<PartnerActivity> getPartnerActivityByField(String fieldName, String fieldValue) {

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = fieldName+" = ? ";
        String[] whereArgs = new String[] {
                fieldValue
        };

        Cursor c = db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        List<PartnerActivity> partnerActivityList = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            partnerActivityList.add(cursorToPartner(c));
        }
        db.close();
        return partnerActivityList;
    }

    public PartnerActivity getPartnerActivityById(String uuid) {

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = ID+" = ? ";
        String[] whereArgs = new String[] {
                uuid
        };
        Cursor c = db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        PartnerActivity partnerActivity  = cursorToPartner(c);
        db.close();
        return partnerActivity;
    }


    private PartnerActivity cursorToPartner(Cursor c){
        PartnerActivity partners = new PartnerActivity();

        partners.setId(c.getString(c.getColumnIndex(ID)));
        partners.setPartnerId(c.getString(c.getColumnIndex(PARTNERID)));
        partners.setCountry(c.getString(c.getColumnIndex(COUNTRY)));
        partners.setCounty(c.getString(c.getColumnIndex(COUNTY)));
        partners.setSubcounty(c.getString(c.getColumnIndex(SUBCOUNTY)));
        partners.setParish(c.getString(c.getColumnIndex(PARISH)));
        partners.setVillage(c.getString(c.getColumnIndex(VILLAGE)));
        partners.setCommunityUnit(c.getString(c.getColumnIndex(COMMUNITYUNIT)));
        partners.setMappingId(c.getString(c.getColumnIndex(MAPPINGID)));
        partners.setComment(c.getString(c.getColumnIndex(COMMENT)));
        partners.setDoingMhealth(c.getInt(c.getColumnIndex(DOINGMHEALTH))==1);
        partners.setDoingIccm(c.getInt(c.getColumnIndex(DOINGICCM))==1);
        partners.setGivingFreeDrugs(c.getInt(c.getColumnIndex(GIVINGFREEDRUGS))==1);
        partners.setGivingStipend(c.getInt(c.getColumnIndex(GIVINGSTIPEND))==1);
        partners.setDateAdded(c.getLong(c.getColumnIndex(ADDEDBY)));
        partners.setAddedBy(c.getLong(c.getColumnIndex(ADDEDBY)));
        partners.setActivities(c.getString(c.getColumnIndex(ACTIVITIES)));
        partners.setSynced(c.getInt(c.getColumnIndex(SYNCED))==1);
        return partners;
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



    public void partnerActivityFromJson (JSONObject jsonObject){
        PartnerActivity partners = new PartnerActivity();
        try {
            partners.setId(jsonObject.getString(ID));
            partners.setPartnerId(jsonObject.getString(PARTNERID));
            partners.setCountry(jsonObject.getString(COUNTRY));
            partners.setCounty(jsonObject.getString(COUNTY));
            partners.setSubcounty(jsonObject.getString(SUBCOUNTY));
            partners.setParish(jsonObject.getString(PARISH));
            partners.setVillage(jsonObject.getString(VILLAGE));
            partners.setCommunityUnit(jsonObject.getString(COMMUNITYUNIT));
            partners.setMappingId(jsonObject.getString(MAPPINGID));
            partners.setComment(jsonObject.getString(COMMENT));
            partners.setDoingMhealth(jsonObject.getInt(DOINGMHEALTH)==1);
            partners.setDoingIccm(jsonObject.getInt(DOINGICCM)==1);
            partners.setGivingFreeDrugs(jsonObject.getInt(GIVINGFREEDRUGS)==1);
            partners.setGivingStipend(jsonObject.getInt(GIVINGSTIPEND)==1);
            partners.setDateAdded(jsonObject.getLong(ADDEDBY));
            partners.setAddedBy(jsonObject.getLong(ADDEDBY));
            partners.setActivities(jsonObject.getString(ACTIVITIES));
            partners.setSynced(jsonObject.getInt(SYNCED)==1);

            addData(partners);
        }catch (Exception e){
            Log.d("Tremap", "+++++++++++++++++++++++++++++++++++++++");
            Log.d("Tremap", "CE ERROR "+e.getMessage());
        }
    }

    private void upgradeVersion2(SQLiteDatabase db) {}
}

