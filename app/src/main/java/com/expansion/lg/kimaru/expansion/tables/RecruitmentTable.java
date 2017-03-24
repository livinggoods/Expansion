package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String DATE_ADDED = "date_added";
    public static final String SYNCED = "synced";

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + primary_field + ", "
            + NAME + varchar_field + ", "
            + LAT + varchar_field + ", "
            + LON + varchar_field + ", "
            + DISTRICT + varchar_field + ", "
            + SUB_COUNTY + varchar_field + ", "
            + DIVISION + varchar_field + ", "
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
        cv.put(NAME, recruitment.getName());
        cv.put(DISTRICT, recruitment.getDistrict());
        cv.put(LAT, recruitment.getLat());
        cv.put(LON, recruitment.getLon());
        cv.put(SUB_COUNTY, recruitment.getSubcounty());
        cv.put(DIVISION, recruitment.getDivision());
        cv.put(ADDED_BY, recruitment.getAddedBy());
        cv.put(COMMENT, recruitment.getComment());
        cv.put(DATE_ADDED, recruitment.getDateAdded());
        cv.put(SYNCED, recruitment.getSynced());

        long id=db.insert(TABLE_NAME,null,cv);

        db.close();
        return id;

    }

    public List<Recruitment> getRecruitmentData() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, NAME, DISTRICT, SUB_COUNTY, DIVISION, LAT, LON, ADDED_BY, COMMENT, DATE_ADDED, SYNCED};

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Recruitment> recruitmentList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Recruitment recruitment=new Recruitment();

            recruitment.setId(cursor.getInt(0));
            recruitment.setName(cursor.getString(1));
            recruitment.setDistrict(cursor.getString(2));
            recruitment.setSubcounty(cursor.getString(3));
            recruitment.setDivision(cursor.getString(4));
            recruitment.setLat(cursor.getString(5));
            recruitment.setLon(cursor.getString(6));
            recruitment.setAddedBy(cursor.getInt(7));
            recruitment.setComment(cursor.getString(8));
            recruitment.setDateAdded(cursor.getInt(9));
            recruitment.setSynced(cursor.getInt(10));

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

            recruitment.setId(cursor.getInt(0));
            recruitment.setName(cursor.getString(1));
            recruitment.setDistrict(cursor.getString(2));
            recruitment.setSubcounty(cursor.getString(3));
            recruitment.setDivision(cursor.getString(4));
            recruitment.setLat(cursor.getString(5));
            recruitment.setLon(cursor.getString(6));
            recruitment.setAddedBy(cursor.getInt(7));
            recruitment.setComment(cursor.getString(8));
            recruitment.setDateAdded(cursor.getInt(9));
            recruitment.setSynced(cursor.getInt(10));

            recruitments.add(recruitment);
        }

        db.close();

        return recruitments;
    }
    public JSONObject getRecruitmentJson() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, NAME, DISTRICT, SUB_COUNTY, DIVISION, LAT, LON, ADDED_BY, COMMENT, DATE_ADDED, SYNCED};

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
                results.put("recruitments", resultSet);
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

