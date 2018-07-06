package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Partners;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 8/16/17.
 */



public class PartnersTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="partners";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String varchar_field = " varchar(512) ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static String JSON_ROOT = "community_unit";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CONTACTPERSON = "contact_person";
    public static final String CONTACTPERSONPHONE = "contact_person_phone";
    public static final String PARENT = "parent";
    public static final String MAPPINGID = "mapping_id";
    public static final String DATEADDED = "client_time";
    public static final String ADDEDBY = "added_by";
    public static final String SYNCED = "synced";
    public static final String COUNTRY = "country";
    public static final String COUNTY = "county";
    public static final String SUBCOUNTY = "subcounty";
    public static final String COMMUNITYUNIT = "community_unit";
    public static final String VILLAGE = "village";
    public static final String COMMENT = "comment";
    public static final String ARCHIVED = "archived";

    String [] columns=new String[]{ID, NAME, CONTACTPERSON, CONTACTPERSONPHONE, PARENT, MAPPINGID,
            DATEADDED, ADDEDBY, SYNCED, COUNTRY, COMMENT, ARCHIVED, COUNTY,
            SUBCOUNTY, COMMUNITYUNIT, VILLAGE};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ","
            + NAME + varchar_field + ","
            + CONTACTPERSON + varchar_field + ","
            + CONTACTPERSONPHONE + varchar_field + ","
            + PARENT + varchar_field + ","
            + MAPPINGID + varchar_field + ","
            + DATEADDED + integer_field + ","
            + ADDEDBY + integer_field + ","
            + SYNCED + integer_field + ","
            + COUNTRY + varchar_field + ","
            + COUNTY + varchar_field + ","
            + SUBCOUNTY + varchar_field + ","
            + COMMUNITYUNIT + varchar_field + ","
            + VILLAGE + varchar_field + ","
            + COMMENT + varchar_field + ","
            + ARCHIVED + integer_field + ");";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public PartnersTable(Context context) {
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

    public long addData(Partners partner) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, partner.getPartnerID());
        cv.put(NAME, partner.getPartnerName());
        cv.put(CONTACTPERSON, partner.getContactPerson());
        cv.put(CONTACTPERSONPHONE, partner.getContactPersonPhone());
        cv.put(PARENT, partner.getParent());
        cv.put(MAPPINGID, partner.getMappingId());
        cv.put(DATEADDED, partner.getDateAdded());
        cv.put(ADDEDBY, partner.getAddedBy());
        cv.put(SYNCED, partner.isSynced() ? 1:0);
        cv.put(COUNTRY, partner.getCountry());
        cv.put(COMMENT, partner.getComment());
        cv.put(ARCHIVED, partner.isArchived() ? 1:0);


        long id;
        if (isPartnerExisting(partner)){
            id = db.update(TABLE_NAME, cv, ID+"='"+partner.getPartnerID()+"'", null);
        }else{
            id = db.insert(TABLE_NAME,null,cv);
        }
        db.close();
        return id;
    }

    public boolean isPartnerExisting(Partners partner) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + TABLE_NAME + " WHERE " + ID + " = '"
                + partner.getPartnerID() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }

    public List<Partners> getPartnersData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, columns,null,null,null,null,null,null);
        List<Partners> partnersList = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Partners partners = new Partners();
            partners.setPartnerID(c.getString(c.getColumnIndex(ID)));
            partners.setPartnerName(c.getString(c.getColumnIndex(NAME)));
            partners.setContactPerson(c.getString(c.getColumnIndex(CONTACTPERSON)));
            partners.setContactPersonPhone(c.getString(c.getColumnIndex(CONTACTPERSONPHONE)));
            partners.setParent(c.getString(c.getColumnIndex(PARENT)));
            partners.setMappingId(c.getString(c.getColumnIndex(MAPPINGID)));
            partners.setCountry(c.getString(c.getColumnIndex(COUNTRY)));
            partners.setComment(c.getString(c.getColumnIndex(COMMENT)));
            partners.setSynced(c.getInt(c.getColumnIndex(SYNCED))==1);
            partners.setArchived(c.getInt(c.getColumnIndex(ARCHIVED))==1);
            partners.setDateAdded(c.getLong(c.getColumnIndex(DATEADDED)));
            partners.setAddedBy(c.getLong(c.getColumnIndex(ADDEDBY)));

            partnersList.add(partners);
        }
        db.close();
        return partnersList;
    }

    public List<Partners> getPartnersBySubCounty(String subCountyUUID) {

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = SUBCOUNTY+" = ? ";
        String[] whereArgs = new String[] {
                subCountyUUID
        };

        Cursor c = db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        List<Partners> partnersList = new ArrayList<>();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Partners partners = new Partners();
            partners.setPartnerID(c.getString(c.getColumnIndex(ID)));
            partners.setPartnerName(c.getString(c.getColumnIndex(NAME)));
            partners.setContactPerson(c.getString(c.getColumnIndex(CONTACTPERSON)));
            partners.setContactPersonPhone(c.getString(c.getColumnIndex(CONTACTPERSONPHONE)));
            partners.setParent(c.getString(c.getColumnIndex(PARENT)));
            partners.setMappingId(c.getString(c.getColumnIndex(MAPPINGID)));
            partners.setCountry(c.getString(c.getColumnIndex(COUNTRY)));
            partners.setComment(c.getString(c.getColumnIndex(COMMENT)));
            partners.setSynced(c.getInt(c.getColumnIndex(SYNCED))==1);
            partners.setArchived(c.getInt(c.getColumnIndex(ARCHIVED))==1);
            partners.setDateAdded(c.getLong(c.getColumnIndex(DATEADDED)));
            partners.setAddedBy(c.getLong(c.getColumnIndex(ADDEDBY)));

            partnersList.add(partners);
        }
        db.close();
        return partnersList;
    }

    public Partners getPartnerById(String partnerUUID){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                partnerUUID,
        };
        Cursor c=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(c.moveToFirst()) || c.getCount() ==0){
            return null;
        }else{
            Partners partners = new Partners();
            partners.setPartnerID(c.getString(c.getColumnIndex(ID)));
            partners.setPartnerName(c.getString(c.getColumnIndex(NAME)));
            partners.setContactPerson(c.getString(c.getColumnIndex(CONTACTPERSON)));
            partners.setContactPersonPhone(c.getString(c.getColumnIndex(CONTACTPERSONPHONE)));
            partners.setParent(c.getString(c.getColumnIndex(PARENT)));
            partners.setMappingId(c.getString(c.getColumnIndex(MAPPINGID)));
            partners.setCountry(c.getString(c.getColumnIndex(COUNTRY)));
            partners.setComment(c.getString(c.getColumnIndex(COMMENT)));
            partners.setSynced(c.getInt(c.getColumnIndex(SYNCED))==1);
            partners.setArchived(c.getInt(c.getColumnIndex(ARCHIVED))==1);
            partners.setDateAdded(c.getLong(c.getColumnIndex(DATEADDED)));
            partners.setAddedBy(c.getLong(c.getColumnIndex(ADDEDBY)));
            return partners;
        }

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



    public void fromJson (JSONObject jsonObject){
        Partners partners = new Partners();
        try {

            partners.setPartnerID(jsonObject.getString(ID));
            partners.setPartnerName(jsonObject.getString(NAME));
            partners.setContactPerson(jsonObject.getString(CONTACTPERSON));
            partners.setContactPersonPhone(jsonObject.getString(CONTACTPERSONPHONE));
            partners.setParent(jsonObject.getString(PARENT));
            partners.setMappingId(jsonObject.getString(MAPPINGID));
            partners.setCountry(jsonObject.getString(COUNTRY));
            partners.setComment(jsonObject.getString(COMMENT));
            partners.setSynced(jsonObject.getInt(SYNCED)==1);
            partners.setArchived(jsonObject.getInt(ARCHIVED)==1);
            partners.setDateAdded(jsonObject.getLong(DATEADDED));
            partners.setAddedBy(jsonObject.getLong(ADDEDBY));

            addData(partners);
        }catch (Exception e){
            Log.d("Tremap", "+++++++++++++++++++++++++++++++++++++++");
            Log.d("Tremap", "CE ERROR "+e.getMessage());
        }
    }

    private void upgradeVersion2(SQLiteDatabase db) {}
}

