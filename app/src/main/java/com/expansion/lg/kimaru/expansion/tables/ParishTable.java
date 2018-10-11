package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kimaru on 5/23/17.
 */




public class ParishTable extends SQLiteOpenHelper {

    Context context;

    public static final String TABLE_NAME="parish";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String PARISHNAME= "name";
    public static final String PARENT_LOCATION = "parent";
    public static final String MAPPINGID = "mapping";
    public static final String MAPPING_ID = "mapping_id";
    public static final String ADDED_BY = "added_by";
    public static final String CONTACTPERSON = "contact_person";
    public static final String CONTACTPERSONPHONE = "phone";
    public static final String COMMENT = "comment";
    public static final String SYNCED = "synced";
    public static final String COUNTRY = "country";
    public static final String DATE_ADDED = "date_added";

    public static final String JSON_ROOT = "parishes";

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + PARISHNAME + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + PARENT_LOCATION + varchar_field + ", "
            + MAPPINGID + varchar_field + ", "
            + ADDED_BY + integer_field + ", "
            + CONTACTPERSON + varchar_field + ", "
            + CONTACTPERSONPHONE + varchar_field + ", "
            + COMMENT + text_field + ", "
            + SYNCED + integer_field + ", "
            + DATE_ADDED + integer_field + "); ";

    public String [] columns=new String[]{ID, PARISHNAME, COUNTRY, PARENT_LOCATION, MAPPINGID, ADDED_BY,
            CONTACTPERSON, CONTACTPERSONPHONE, COMMENT, SYNCED, DATE_ADDED};

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;
    public static final String DB_UPDATE_V2 = CREATE_DATABASE;

    public ParishTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2){
            upgradeVersion2(db);
        }
    }

    public String addData(Parish parish) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, parish.getId());
        cv.put(PARISHNAME, parish.getName());
        cv.put(COUNTRY, parish.getCountry());
        cv.put(PARENT_LOCATION, parish.getParent());
        cv.put(MAPPINGID, parish.getMapping());
        cv.put(ADDED_BY, parish.getAddedBy());
        cv.put(CONTACTPERSON, parish.getContactPerson());
        cv.put(CONTACTPERSONPHONE, parish.getContactPersonPhone());
        cv.put(COMMENT, parish.getComment());
        cv.put(SYNCED, parish.getSynced());
        cv.put(DATE_ADDED, parish.getDateAdded());
        long id;
        if (isExist(parish)){
            id = db.update(TABLE_NAME, cv, ID+" = '"+parish.getId()+"'", null);
            Log.e("expansion parish table ", "Updated ID : " + String.valueOf(parish.getId()));
        }else{
            id = db.insert(TABLE_NAME, null, cv);
            Log.e("expansion parish ", "New record - ID is " + String.valueOf(id));
        }
        db.close();
        return String.valueOf(id);

    }

    public boolean isExist(Parish parish) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + TABLE_NAME + " WHERE "+ID+" = '" + parish.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }
    public List<Parish> getParishData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Parish> parishList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Parish parish=new Parish();

            parish.setId(cursor.getString(0));
            parish.setName(cursor.getString(1));
            parish.setCountry(cursor.getString(2));
            parish.setParent(cursor.getString(3));
            parish.setMapping(cursor.getString(4));
            parish.setAddedBy(cursor.getInt(5));
            parish.setContactPerson(cursor.getString(6));
            parish.setContactPersonPhone(cursor.getString(7));
            parish.setComment(cursor.getString(8));
            parish.setSynced(cursor.getInt(9));
            parish.setDateAdded(cursor.getLong(10));

            parishList.add(parish);
        }
        db.close();

        return parishList;
    }

    public Parish getParishById(String uuid){
        Parish parish = new Parish();
        String [] selection = new String[]{uuid};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns,ID+"=?", selection, null,null,null,null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            parish.setId(cursor.getString(0));
            parish.setName(cursor.getString(1));
            parish.setCountry(cursor.getString(2));
            parish.setParent(cursor.getString(3));
            parish.setMapping(cursor.getString(4));
            parish.setAddedBy(cursor.getInt(5));
            parish.setContactPerson(cursor.getString(6));
            parish.setContactPersonPhone(cursor.getString(7));
            parish.setComment(cursor.getString(8));
            parish.setSynced(cursor.getInt(9));
            parish.setDateAdded(cursor.getLong(10));
            return parish;
        }

        return null;
    }
    public List<Parish> getParishByCountry(String countryCode) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED + " desc";
        String whereClause = COUNTRY+" = ?";
        String[] whereArgs = new String[] {
                countryCode,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);
        List<Parish> parishList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Parish parish=new Parish();
            parish.setId(cursor.getString(0));
            parish.setName(cursor.getString(1));
            parish.setCountry(cursor.getString(2));
            parish.setParent(cursor.getString(3));
            parish.setMapping(cursor.getString(4));
            parish.setAddedBy(cursor.getInt(5));
            parish.setContactPerson(cursor.getString(6));
            parish.setContactPersonPhone(cursor.getString(7));
            parish.setComment(cursor.getString(8));
            parish.setSynced(cursor.getInt(9));
            parish.setDateAdded(cursor.getLong(10));

            parishList.add(parish);
        }
        db.close();

        return parishList;
    }
    public List<Parish> getParishByParent(String parentUuid) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED + " desc";
        String whereClause = PARENT_LOCATION+" = ?";
        String[] whereArgs = new String[] {
                parentUuid,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);
        List<Parish> parishList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Parish parish=new Parish();
            parish.setId(cursor.getString(0));
            parish.setName(cursor.getString(1));
            parish.setCountry(cursor.getString(2));
            parish.setParent(cursor.getString(3));
            parish.setMapping(cursor.getString(4));
            parish.setAddedBy(cursor.getInt(5));
            parish.setContactPerson(cursor.getString(6));
            parish.setContactPersonPhone(cursor.getString(7));
            parish.setComment(cursor.getString(8));
            parish.setSynced(cursor.getInt(9));
            parish.setDateAdded(cursor.getLong(10));

            parishList.add(parish);
        }
        db.close();

        return parishList;
    }

    public List<Parish> getParishByMapping(String mappingId) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED + " desc";
        String whereClause = MAPPINGID+" = ?";
        String[] whereArgs = new String[] {
                mappingId,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);
        List<Parish> parishList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Parish parish=new Parish();
            parish.setId(cursor.getString(0));
            parish.setName(cursor.getString(1));
            parish.setCountry(cursor.getString(2));
            parish.setParent(cursor.getString(3));
            parish.setMapping(cursor.getString(4));
            parish.setAddedBy(cursor.getInt(5));
            parish.setContactPerson(cursor.getString(6));
            parish.setContactPersonPhone(cursor.getString(7));
            parish.setComment(cursor.getString(8));
            parish.setSynced(cursor.getInt(9));
            parish.setDateAdded(cursor.getLong(10));

            parishList.add(parish);
        }
        db.close();

        return parishList;
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
        Cursor cursor=db.query(TABLE_NAME,columns,SYNCED+"=?",new String[]{Constants.SYNC_STATUS_UNSYNCED + ""},null,null,null,
                String.format("%d,%d", offset, Constants.SYNC_PAGINATION_SIZE));
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
        Parish parish = new Parish();
        try {

            if (!jsonObject.isNull(ID)){
                parish.setId(jsonObject.getString(ID));
            }
            if (!jsonObject.isNull(PARISHNAME)){
                parish.setName(jsonObject.getString(PARISHNAME));
            }
            if (!jsonObject.isNull(CONTACTPERSON)){
                parish.setContactPerson(jsonObject.getString(CONTACTPERSON));
            }
            if (!jsonObject.isNull(CONTACTPERSONPHONE)){
                parish.setContactPersonPhone(jsonObject.getString(CONTACTPERSONPHONE));
            }
            if (!jsonObject.isNull(PARENT_LOCATION)){
                parish.setParent(jsonObject.getString(PARENT_LOCATION));
            }
            if (!jsonObject.isNull(MAPPING_ID)){
                parish.setMapping(jsonObject.getString(MAPPING_ID));
            }
            if (!jsonObject.isNull(DATE_ADDED)){
                //Wed, 31 Jan 2018 16:23:07
                SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
                Date date = df.parse(jsonObject.getString(DATE_ADDED));
                long epoch = date.getTime();
                parish.setDateAdded(epoch);
            }
            //parish.setDateAdded(jsonObject.getLong(DATE_ADDED));
            if (!jsonObject.isNull(ADDED_BY)){
                parish.setAddedBy(jsonObject.getInt(ADDED_BY));
            }
            if (!jsonObject.isNull(SYNCED)){
                parish.setSynced(jsonObject.getInt(SYNCED));
            }
            if (!jsonObject.isNull(COUNTRY)){
                parish.setCountry(jsonObject.getString(COUNTRY));
            }
            if (!jsonObject.isNull(COMMENT)){
                parish.setComment(jsonObject.getString(COMMENT));
            }
            addData(parish);
        }catch (Exception e){
            Log.d("Tremap ERR", "Parish from JSON "+e.getMessage());
        }
    }

    private void upgradeVersion2(SQLiteDatabase db) {
        // add column
        db.execSQL(DB_UPDATE_V2);
    }
}