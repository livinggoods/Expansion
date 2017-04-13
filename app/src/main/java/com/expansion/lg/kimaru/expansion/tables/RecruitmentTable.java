package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class RecruitmentTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="recruitment";
    public static final String DATABASE_NAME="expansion";
    public static final String JSON_ROOT="recruitments";
    public static final int DATABASE_VERSION=1;

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "_id";
    public static final String NAME= "name";
    public static final String LON = "lon";
    public static final String LAT = "lat";
    public static final String DISTRICT = "district";
    public static final String SUB_COUNTY = "subcounty";
    public static final String DIVISION = "division";
    public static final String COUNTRY = "country";
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String DATE_ADDED = "date_added";
    public static final String SYNCED = "synced";

    String [] columns=new String[]{ID, NAME, DISTRICT, SUB_COUNTY, DIVISION, LAT, LON, ADDED_BY,
            COMMENT, DATE_ADDED, SYNCED, COUNTRY};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + NAME + varchar_field + ", "
            + LAT + varchar_field + ", "
            + LON + varchar_field + ", "
            + DISTRICT + varchar_field + ", "
            + SUB_COUNTY + varchar_field + ", "
            + DIVISION + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + ADDED_BY + integer_field + ", "
            + COMMENT + text_field + ", "
            + DATE_ADDED + integer_field + ", "
            + SYNCED + integer_field + ");";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public RecruitmentTable(Context context) {
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

    public long addData(Recruitment recruitment) {

        SQLiteDatabase db=getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(ID, recruitment.getId());
        cv.put(NAME, recruitment.getName());
        cv.put(DISTRICT, recruitment.getDistrict());
        cv.put(LAT, recruitment.getLat());
        cv.put(LON, recruitment.getLon());
        cv.put(SUB_COUNTY, recruitment.getSubcounty());
        cv.put(COUNTRY, recruitment.getCountry());
        cv.put(DIVISION, recruitment.getDivision());
        cv.put(ADDED_BY, recruitment.getAddedBy());
        cv.put(COMMENT, recruitment.getComment());
        cv.put(DATE_ADDED, recruitment.getDateAdded());
        cv.put(SYNCED, recruitment.getSynced());
        long id;
        if (isExist(recruitment)){
            //uupdate
            cv.put(SYNCED, 0);
            id = db.update(TABLE_NAME, cv, ID+"='"+recruitment.getId()+"'", null);
        }else{
            //create new
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;

    }
    public boolean isExist(Recruitment recruitment) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + TABLE_NAME + " WHERE " + ID + " = '" + recruitment.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }


    public long getRecruitmentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return cnt;
    }

    public List<Recruitment> getRecruitmentData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        List<Recruitment> recruitmentList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Recruitment recruitment=new Recruitment();

            recruitment.setId(cursor.getString(0));
            recruitment.setName(cursor.getString(1));
            recruitment.setDistrict(cursor.getString(2));
            recruitment.setSubcounty(cursor.getString(3));
            recruitment.setDivision(cursor.getString(4));
            recruitment.setLat(cursor.getString(5));
            recruitment.setLon(cursor.getString(6));
            recruitment.setAddedBy(cursor.getInt(7));
            recruitment.setComment(cursor.getString(8));
            recruitment.setDateAdded(cursor.getLong(9));
            recruitment.setSynced(cursor.getInt(10));
            recruitment.setCountry(cursor.getString(11));

            recruitmentList.add(recruitment);
        }

        db.close();

        return recruitmentList;
    }

    public List<Recruitment> getRecruitmentDataByCountryCode(String country) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED +" desc";
        String whereClause = COUNTRY+" = ?";
        String[] whereArgs = new String[] {
                country,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);
        List<Recruitment> recruitmentList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Recruitment recruitment=new Recruitment();

            recruitment.setId(cursor.getString(0));
            recruitment.setName(cursor.getString(1));
            recruitment.setDistrict(cursor.getString(2));
            recruitment.setSubcounty(cursor.getString(3));
            recruitment.setDivision(cursor.getString(4));
            recruitment.setLat(cursor.getString(5));
            recruitment.setLon(cursor.getString(6));
            recruitment.setAddedBy(cursor.getInt(7));
            recruitment.setComment(cursor.getString(8));
            recruitment.setDateAdded(cursor.getLong(9));
            recruitment.setSynced(cursor.getInt(10));
            recruitment.setCountry(cursor.getString(11));

            recruitmentList.add(recruitment);
        }

        db.close();

        return recruitmentList;
    }

    public void syncRecruitment(Recruitment recruitment){
        //In order to make it unique, we shall be checking the recruitments as follows
        // a) The recruitment must be created by someone else (addedby != currentUserID)
        // b) Recruitment Name is not the same as the one we have in the DB
        // c) Recruitment added_date should not be the same


    }

    public List<Recruitment> getRecruitmentbyQuery(String whereClause){

        List<Recruitment> recruitments = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String queryString = "SELECT * FROM  " + TABLE_NAME + " WHERE " + whereClause;
        Cursor cursor = db.rawQuery(queryString, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Recruitment recruitment=new Recruitment();

            recruitment.setId(cursor.getString(0));
            recruitment.setName(cursor.getString(1));
            recruitment.setDistrict(cursor.getString(2));
            recruitment.setSubcounty(cursor.getString(3));
            recruitment.setDivision(cursor.getString(4));
            recruitment.setLat(cursor.getString(5));
            recruitment.setLon(cursor.getString(6));
            recruitment.setAddedBy(cursor.getInt(7));
            recruitment.setComment(cursor.getString(8));
            recruitment.setDateAdded(cursor.getLong(9));
            recruitment.setSynced(cursor.getInt(10));
            recruitment.setCountry(cursor.getString(11));

            recruitments.add(recruitment);
        }

        db.close();

        return recruitments;
    }
    public JSONObject getRecruitmentJson() {

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

    public Recruitment getRecruitmentById(String id) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED +" desc";
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                id,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);

        Recruitment recruitment = new Recruitment();

        recruitment.setId(cursor.getString(0));
        recruitment.setName(cursor.getString(1));
        recruitment.setDistrict(cursor.getString(2));
        recruitment.setSubcounty(cursor.getString(3));
        recruitment.setDivision(cursor.getString(4));
        recruitment.setLat(cursor.getString(5));
        recruitment.setLon(cursor.getString(6));
        recruitment.setAddedBy(cursor.getInt(7));
        recruitment.setComment(cursor.getString(8));
        recruitment.setDateAdded(cursor.getLong(9));
        recruitment.setSynced(cursor.getInt(10));
        recruitment.setCountry(cursor.getString(11));

        db.close();

        return recruitment;
    }

    public JSONObject getRecruitmentToSyncAsJson() {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = SYNCED+" = ?";
        String[] whereArgs = new String[] {
                "0",
        };

        Cursor cursor=db.query(TABLE_NAME,columns, whereClause, whereArgs,null,null,null,null);

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


    public Cursor getRecruitmentDataCursor() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, NAME, DISTRICT, SUB_COUNTY, DIVISION, LAT, LON, ADDED_BY, COMMENT, DATE_ADDED, SYNCED};

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return  cursor;
    }
}

