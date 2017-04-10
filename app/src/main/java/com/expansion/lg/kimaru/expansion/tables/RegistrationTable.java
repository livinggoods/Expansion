package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 3/11/17.
 */


public class RegistrationTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="registration";
    public static final String DATABASE_NAME="expansion";
    public static final int DATABASE_VERSION=1;
    public static final String JSON_ROOT="registrations";

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String NAME= "name";
    public static final String PHONE = "phone";
    public static final String GENDER = "gender";
    public static final String RECRUITMENT = "recruitment";
    public static final String COUNTRY = "country";
    public static final String DOB = "dob";
    public static final String DISTRICT = "district";
    public static final String SUB_COUNTY = "subcounty";
    public static final String DIVISION = "division";
    public static final String VILLAGE = "village";
    public static final String MARK = "feature";
    public static final String READ_ENGLISH = "english";
    public static final String DATE_MOVED = "date_moved";
    public static final String LANGS = "languages";
    public static final String BRAC = "brac";
    public static final String BRAC_CHP = "brac_chp";
    public static final String EDUCATION = "education";
    public static final String OCCUPATION = "occupation";
    public static final String COMMUNITY = "community_membership";
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String PROCEED = "proceed";
    public static final String DATE_ADDED = "date_added";
    public static final String SYNCED = "synced";

    String [] columns=new String[]{ID, NAME, PHONE, GENDER, DOB, DISTRICT, SUB_COUNTY, DIVISION,
            VILLAGE, MARK, READ_ENGLISH, DATE_MOVED, LANGS, BRAC, BRAC_CHP, EDUCATION, OCCUPATION,
            COMMUNITY, ADDED_BY, COMMENT, PROCEED, DATE_ADDED, SYNCED, RECRUITMENT, COUNTRY};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + NAME + varchar_field + ", "
            + PHONE + varchar_field + ", "
            + GENDER + varchar_field + ", "
            + DOB + integer_field + ", "
            + DISTRICT + varchar_field + ", "
            + SUB_COUNTY + varchar_field + ", "
            + RECRUITMENT + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + DIVISION + varchar_field + ", "
            + VILLAGE + varchar_field + ", "
            + MARK + text_field + ", "
            + READ_ENGLISH + integer_field + ", "
            + DATE_MOVED + integer_field + ", "
            + LANGS + varchar_field + ", "
            + BRAC + integer_field + ", "
            + BRAC_CHP + integer_field + ", "
            + EDUCATION + text_field + ", "
            + OCCUPATION + text_field + ", "
            + COMMUNITY + integer_field + ", "
            + ADDED_BY + integer_field + ", "
            + COMMENT + text_field + ", "
            + PROCEED + integer_field + ", "
            + DATE_ADDED + integer_field + ", "
            + SYNCED + integer_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public RegistrationTable(Context context) {
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

    public long addData(Registration registration) {

        SQLiteDatabase db=getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(ID, registration.getId());
        cv.put(NAME, registration.getName());
        cv.put(PHONE, registration.getPhone());
        cv.put(GENDER, registration.getGender());
        cv.put(DOB, registration.getDob());
        cv.put(COUNTRY, registration.getCountry());
        cv.put(RECRUITMENT, registration.getRecruitment());
        cv.put(DISTRICT, registration.getDistrict());
        cv.put(SUB_COUNTY, registration.getSubcounty());
        cv.put(DIVISION, registration.getDivision());
        cv.put(VILLAGE, registration.getVillage());
        cv.put(MARK, registration.getMark());
        cv.put(READ_ENGLISH, registration.getReadEnglish());
        cv.put(DATE_MOVED, registration.getDateMoved());
        cv.put(LANGS, registration.getLangs());
        cv.put(BRAC, registration.getBrac());
        cv.put(BRAC_CHP, registration.getBracChp());
        cv.put(EDUCATION, registration.getEducation());
        cv.put(OCCUPATION, registration.getOccupation());
        cv.put(COMMUNITY, registration.getCommunity());
        cv.put(ADDED_BY, registration.getAddedBy());
        cv.put(COMMENT, registration.getComment());
        cv.put(PROCEED, registration.getProceed());
        cv.put(DATE_ADDED, registration.getDateAdded());
        cv.put(SYNCED, registration.getSynced());
        long id;
        if (isExist(registration)){
            id = db.update(TABLE_NAME, cv, ID+"='"+registration.getId()+"'", null);
        }else{
            id = db.insert(TABLE_NAME,null,cv);
        }

        db.close();
        return id;

    }
    public Cursor getRegistrationDataCursor() {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = "id desc";

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,orderBy,null);
        db.close();
        return cursor;
    }

    public List<Registration> getRegistrationData() {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = "id desc";

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,orderBy,null);

        List<Registration> registrationList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Registration registration=new Registration();

            registration.setId(cursor.getString(0));
            registration.setName(cursor.getString(1));
            registration.setPhone(cursor.getString(2));
            registration.setGender(cursor.getString(3));
            registration.setDob(cursor.getLong(4));
            registration.setDistrict(cursor.getString(5));
            registration.setSubcounty(cursor.getString(6));
            registration.setDivision(cursor.getString(7));
            registration.setVillage(cursor.getString(8));
            registration.setMark(cursor.getString(9));
            registration.setReadEnglish(cursor.getInt(10));
            registration.setDateMoved(cursor.getLong(11));
            registration.setLangs(cursor.getString(12));
            registration.setBrac(cursor.getInt(13));
            registration.setBracChp(cursor.getInt(14));
            registration.setEducation(cursor.getString(15));
            registration.setOccupation(cursor.getString(16));
            registration.setCommunity(cursor.getInt(17));
            registration.setAddedBy(cursor.getInt(18));
            registration.setComment(cursor.getString(19));
            registration.setProceed(cursor.getInt(20));
            registration.setDateAdded(cursor.getLong(21));
            registration.setSynced(cursor.getInt(22));
            registration.setRecruitment(cursor.getString(23));
            registration.setCountry(cursor.getString(24));
            registration.setPicture("");




            registrationList.add(registration);
        }

        db.close();

        return registrationList;
    }

    public Registration getRegistrationById(String registrationUuid){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                registrationUuid,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            Registration registration=new Registration();

            registration.setId(cursor.getString(0));
            registration.setName(cursor.getString(1));
            registration.setPhone(cursor.getString(2));
            registration.setGender(cursor.getString(3));
            registration.setDob(cursor.getLong(4));
            registration.setDistrict(cursor.getString(5));
            registration.setSubcounty(cursor.getString(6));
            registration.setDivision(cursor.getString(7));
            registration.setVillage(cursor.getString(8));
            registration.setMark(cursor.getString(9));
            registration.setReadEnglish(cursor.getInt(10));
            registration.setDateMoved(cursor.getLong(11));
            registration.setLangs(cursor.getString(12));
            registration.setBrac(cursor.getInt(13));
            registration.setBracChp(cursor.getInt(14));
            registration.setEducation(cursor.getString(15));
            registration.setOccupation(cursor.getString(16));
            registration.setCommunity(cursor.getInt(17));
            registration.setAddedBy(cursor.getInt(18));
            registration.setComment(cursor.getString(19));
            registration.setProceed(cursor.getInt(20));
            registration.setDateAdded(cursor.getLong(21));
            registration.setSynced(cursor.getInt(22));
            registration.setRecruitment(cursor.getString(23));
            registration.setCountry(cursor.getString(24));
            registration.setPicture("");

            return registration;
        }

    }

    public boolean isExist(Registration registration) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + TABLE_NAME + " WHERE " + ID + " = '" + registration.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }


    public long getRegistrationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return cnt;
    }

    public Registration getRegistrationsByRecruitment(Recruitment recruitment){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = RECRUITMENT+" = ?";
        String[] whereArgs = new String[] {
                recruitment.getId(),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            Registration registration=new Registration();

            registration.setId(cursor.getString(0));
            registration.setName(cursor.getString(1));
            registration.setPhone(cursor.getString(2));
            registration.setGender(cursor.getString(3));
            registration.setDob(cursor.getLong(4));
            registration.setDistrict(cursor.getString(5));
            registration.setSubcounty(cursor.getString(6));
            registration.setDivision(cursor.getString(7));
            registration.setVillage(cursor.getString(8));
            registration.setMark(cursor.getString(9));
            registration.setReadEnglish(cursor.getInt(10));
            registration.setDateMoved(cursor.getLong(11));
            registration.setLangs(cursor.getString(12));
            registration.setBrac(cursor.getInt(13));
            registration.setBracChp(cursor.getInt(14));
            registration.setEducation(cursor.getString(15));
            registration.setOccupation(cursor.getString(16));
            registration.setCommunity(cursor.getInt(17));
            registration.setAddedBy(cursor.getInt(18));
            registration.setComment(cursor.getString(19));
            registration.setProceed(cursor.getInt(20));
            registration.setDateAdded(cursor.getLong(21));
            registration.setSynced(cursor.getInt(22));
            registration.setRecruitment(cursor.getString(23));
            registration.setCountry(cursor.getString(24));
            registration.setPicture("");

            return registration;
        }

    }

    public JSONObject getRegistrationJson() {

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
}
