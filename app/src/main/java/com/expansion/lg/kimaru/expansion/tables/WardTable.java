package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.mzigos.Ward;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 7/21/17.
 */

public class WardTable extends SQLiteOpenHelper {
    public static final String TABLE_NAME="wards";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static String JSON_ROOT = "wards";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COUNTY = "county";
    public static final String SUBCOUNTY = "sub_county";
    public static final String ARCHIVED = "archived";

    String [] columns=new String[]{ID, NAME, COUNTY, SUBCOUNTY, ARCHIVED};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field +", "
            + NAME + varchar_field + ", "
            + COUNTY + integer_field + ", "
            + SUBCOUNTY + varchar_field + ", "
            + ARCHIVED + integer_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public WardTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("Link Facility", "upgrading database from" + oldVersion + "to" + newVersion);
        if (oldVersion < 2){
            upgradeVersion2(db);
        }
        if (oldVersion < 3){
            upgradeVersion3(db);
        }
    }


    public long addData(Ward ward) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, ward.getId());
        cv.put(NAME, ward.getName());
        cv.put(COUNTY, ward.getCounty());
        cv.put(SUBCOUNTY, ward.getSubCounty());
        cv.put(ARCHIVED, ward.getArchived());

        long id;
        if (isExist(ward)){
            id = db.update(TABLE_NAME, cv, ID+"='"+ward.getId()+"'", null);
            Log.d("Tremap DB Op", "Ward updated");
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Tremap DB Op", "Ward Created");
        }
        db.close();
        return id;

    }
    public boolean isExist(Ward ward) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + ward.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }

    public List<Ward> getAllWardsData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Ward> wardList =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Ward ward =new Ward();

            ward.setId(cursor.getString(cursor.getColumnIndex(ID)));
            ward.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            ward.setCounty(cursor.getInt(cursor.getColumnIndex(COUNTY)));
            ward.setSubCounty(cursor.getString(cursor.getColumnIndex(SUBCOUNTY)));
            ward.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED)));
            wardList.add(ward);
        }
        db.close();
        return wardList;
    }

    public List<Ward> getWardsbySubCounty(SubCounty subCounty) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = SUBCOUNTY+" = ? ";

        String[] whereArgs = new String[] {
                subCounty.getId()
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<Ward> wardList =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Ward ward =new Ward();
            ward.setId(cursor.getString(cursor.getColumnIndex(ID)));
            ward.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            ward.setCounty(cursor.getInt(cursor.getColumnIndex(COUNTY)));
            ward.setSubCounty(cursor.getString(cursor.getColumnIndex(SUBCOUNTY)));
            ward.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED)));
            wardList.add(ward);
        }
        db.close();
        return wardList;
    }

    public List<Ward> getWardsbyCounty(Integer countyId) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = COUNTY+" = ? ";

        String[] whereArgs = new String[] {
                countyId.toString()
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<Ward> wardList =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Ward ward =new Ward();
            ward.setId(cursor.getString(cursor.getColumnIndex(ID)));
            ward.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            ward.setCounty(cursor.getInt(cursor.getColumnIndex(COUNTY)));
            ward.setSubCounty(cursor.getString(cursor.getColumnIndex(SUBCOUNTY)));
            ward.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED)));
            wardList.add(ward);
        }
        db.close();
        return wardList;
    }

    public Ward getWardById(String uuid) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = ID+" = ? ";

        String[] whereArgs = new String[] {
                uuid
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else {
            Ward ward =new Ward();
            ward.setId(cursor.getString(cursor.getColumnIndex(ID)));
            ward.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            ward.setCounty(cursor.getInt(cursor.getColumnIndex(COUNTY)));
            ward.setSubCounty(cursor.getString(cursor.getColumnIndex(SUBCOUNTY)));
            ward.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED)));
            db.close();
            return ward;
        }
    }
    public void fromJson(JSONObject jsonObject){
        Ward ward = new Ward();
        try {

            ward.setId(jsonObject.getString(ID));
            String name= jsonObject.getString(NAME);
            if (!jsonObject.getString(NAME).equalsIgnoreCase("")){
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
            ward.setName(name);
            ward.setCounty(jsonObject.getInt(COUNTY));
            ward.setSubCounty(jsonObject.getString(SUBCOUNTY));
            ward.setArchived(jsonObject.getInt(ARCHIVED));
            addData(ward);
        }catch (Exception e){
            Log.d("Tremap ERR", "Ward from JSON "+e.getMessage());
        }
    }
    public JSONObject toJson(Cursor cursor) {
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


    public void upgradeVersion2(SQLiteDatabase db){}
    public void upgradeVersion3(SQLiteDatabase db){}
}
