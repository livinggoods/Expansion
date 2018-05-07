package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Education;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class EducationTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="education";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    //String id, level_name, level_type, hierachy, country;

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "_id";
    public static final String LEVEL_NAME= "name";
    public static final String TYPE = "level_type";
    public static final String HIERACHY = "hierachy";
    public static final String COUNTRY = "country";

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LEVEL_NAME + varchar_field + ", "
            + TYPE + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + HIERACHY + integer_field + "); ";

    String [] columns=new String[]{ID, LEVEL_NAME, TYPE, HIERACHY, COUNTRY};

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS " + TABLE_NAME;

    public EducationTable(Context context) {
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

    public void createEducationFromJson(JSONObject jsonObject){
        Education education = new Education();
        try {
            education.setId(jsonObject.getInt(ID));
            if (!jsonObject.isNull(LEVEL_NAME)){
                education.setLevelName(jsonObject.getString(LEVEL_NAME));
            }
            if (!jsonObject.isNull(COUNTRY)){
                education.setCountry(jsonObject.getString(COUNTRY));
            }
            if (!jsonObject.isNull(TYPE)){
                education.setLevelType(jsonObject.getString(TYPE));
            }
            if (!jsonObject.isNull(HIERACHY)){
                education.setHierachy(jsonObject.getInt(HIERACHY));
            }
            this.addEducation(education);
        }catch (Exception e){}
    }

    public long addEducation(Education education) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, education.getId());
        cv.put(LEVEL_NAME, education.getLevelName());
        cv.put(TYPE, education.getLevelType());
        cv.put(HIERACHY, education.getHierachy());
        cv.put(COUNTRY, education.getCountry());

        long id;
        if (isExist(education)){
            id = db.update(TABLE_NAME, cv, ID+"='"+education.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;
    }
    public List<Education> getEducationData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Education> educationList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Education education=cursorToEducation(cursor);

            educationList.add(education);
        }
        db.close();
        return educationList;
    }
    public Education getEducationById(int id) {

        SQLiteDatabase db = getReadableDatabase();
        String whereClause = ID +" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            Education education=cursorToEducation(cursor);
            return education;
        }
    }

    private Education cursorToEducation(Cursor cursor){
        Education education=new Education();

        education.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        education.setLevelName(cursor.getString(cursor.getColumnIndex(LEVEL_NAME)));
        education.setLevelType(cursor.getString(cursor.getColumnIndex(TYPE)));
        education.setHierachy(cursor.getInt(cursor.getColumnIndex(HIERACHY)));
        education.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
        return education;
    }


    public JSONObject getEducationJson() {

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
                results.put("education", resultSet);
            } catch (JSONException e) {

            }
        }
        cursor.close();
        db.close();
        return results;
    }

    public Cursor getEducationDataCursor(String country) {

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = COUNTRY+" = ?";
        String[] whereArgs = new String[] {
                country,
        };
        String [] columns=new String[]{ID, LEVEL_NAME, TYPE, HIERACHY, COUNTRY};
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        return cursor;
    }

    public void createEducationLevels(){
        //P1 to 8
        // id, hierachy;
        String levelName, levelType, country;
        // set Ugandan Education System
        for (int y=1; y <= 8; y++){
            Education education = new Education();
            education.setId(y);
            education.setCountry("UG");
            education.setHierachy(y);
            if (y == 1  ){
                education.setLevelName("Less than P7");
                education.setLevelType("primary");
            }else if(y >= 2 && y <= 7) {
                education.setLevelName("S" + (y - 1));
                education.setLevelType("secondary");
            }else{
                education.setLevelName("Tertiary");
                education.setLevelType("tertiary");
            }

            this.addEducation(education);
        }
        for (int x = 9; x <= 15; x++){
            Education education = new Education();
            education.setId(x);
            education.setHierachy(x-8);
            education.setCountry("KE");
            if (x > 14) {
                education.setLevelName("Tertiary");
                education.setLevelType("tertiary");
            }else if (x > 10){
                education.setLevelName("Form " + (x - 10));
                education.setLevelType("secondary");
            } else if (x == 10){
                education.setLevelName("P" + (x-2));
                education.setLevelType("primary");
            }else {
                education.setLevelName("Less than P" + (x-2));
                education.setLevelType("primary");
            }
            this.addEducation(education);
        }
    }

    public boolean isExist(Education education) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + TABLE_NAME + " WHERE "+ID+" = '" + education.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public void fromJson(JSONObject jsonObject){
        try{
            Education education = new Education();
            education.setId(jsonObject.getInt(ID));
            education.setLevelName(jsonObject.getString(LEVEL_NAME));
            education.setLevelType(jsonObject.getString(TYPE));
            education.setHierachy(jsonObject.getInt(HIERACHY));
            education.setCountry(jsonObject.getString(COUNTRY));
            this.addEducation(education);
        }catch (Exception e){}
    }

    private void upgradeVersion2(SQLiteDatabase db) {}


}

