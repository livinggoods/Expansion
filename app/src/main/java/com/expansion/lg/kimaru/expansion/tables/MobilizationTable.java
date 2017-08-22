package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Mobilization;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class MobilizationTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="mobilization";
    public static final String JSON_ROOT="mobilization";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    Context context;


    public static String varchar_field = " varchar(512) ";
    public static String real_field = " REAL ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID= "id";
    public static final String NAME= "name";
    public static final String MAPPING = "mapping";
    public static final String COUNTRY = "country";
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String DATE_ADDED = "client_time";
    public static final String COUNTY = "county";
    public static final String DISTRICT = "district";
    public static final String SUB_COUNTY = "sub_county";
    public static final String PARISH = "parish";
    public static final String SYNCED = "synced";

    public String [] columns=new String[]{ID, NAME, MAPPING, COUNTRY, ADDED_BY, COMMENT,
            DATE_ADDED, SYNCED, DISTRICT, COUNTY, SUB_COUNTY, PARISH};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + NAME + varchar_field + ", "
            + MAPPING + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + ADDED_BY + integer_field + ", "
            + COMMENT + text_field + ", "
            + COUNTY + text_field + ", "
            + DISTRICT + text_field + ", "
            + SUB_COUNTY + text_field + ", "
            + PARISH + text_field + ", "
            + DATE_ADDED + real_field + ", "
            + SYNCED + integer_field + ");";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public MobilizationTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        this.context = context;
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

    public long addData(Mobilization mobilization) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(ID, mobilization.getId());
        cv.put(NAME, mobilization.getName());
        cv.put(MAPPING, mobilization.getMappingId());
        cv.put(COUNTRY, mobilization.getCountry());
        cv.put(ADDED_BY, mobilization.getAddedBy());
        cv.put(COMMENT, mobilization.getComment());
        cv.put(DATE_ADDED, mobilization.getDateAdded());
        cv.put(COUNTY, mobilization.getCounty());
        cv.put(DISTRICT, mobilization.getDistrict());
        cv.put(SUB_COUNTY, mobilization.getSubCounty());
        cv.put(PARISH, mobilization.getParish());
        cv.put(SYNCED, mobilization.getSynced());
        long id;
        if (isExist(mobilization)){
            cv.put(SYNCED, 0);
            id = db.update(TABLE_NAME, cv, ID+"='"+mobilization.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();

        return id;

    }

    public List<Mobilization> getMobilizationData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Mobilization> mobilizationList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Mobilization mobilization = new Mobilization();

            mobilization.setId(cursor.getString(cursor.getColumnIndex(ID)));
            mobilization.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            mobilization.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPING)));
            mobilization.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            mobilization.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDED_BY)));
            mobilization.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            mobilization.setDateAdded(cursor.getLong(cursor.getColumnIndex(DATE_ADDED)));
            mobilization.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED))== 1);
            mobilization.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
            mobilization.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
            mobilization.setSubCounty(cursor.getString(cursor.getColumnIndex(SUB_COUNTY)));
            mobilization.setParish(cursor.getString(cursor.getColumnIndex(PARISH)));

            mobilizationList.add(mobilization);
        }
        db.close();

        return mobilizationList;
    }

    public List<Mobilization> getMobilizationByMappingId(String mappingId){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = MAPPING+" = ?";
        String[] whereArgs = new String[] {
                mappingId,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<Mobilization> mobilizationList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Mobilization mobilization = new Mobilization();

            mobilization.setId(cursor.getString(cursor.getColumnIndex(ID)));
            mobilization.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            mobilization.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPING)));
            mobilization.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            mobilization.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDED_BY)));
            mobilization.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            mobilization.setDateAdded(cursor.getLong(cursor.getColumnIndex(DATE_ADDED)));
            mobilization.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED))== 1);
            mobilization.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
            mobilization.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
            mobilization.setSubCounty(cursor.getString(cursor.getColumnIndex(SUB_COUNTY)));
            mobilization.setParish(cursor.getString(cursor.getColumnIndex(PARISH)));

            mobilizationList.add(mobilization);
        }
        db.close();

        return mobilizationList;
    }


    public void fromJson(JSONObject jsonObject){
        try{
            Mobilization mobilization = new Mobilization();
            mobilization.setId(jsonObject.getString(ID));
            mobilization.setName(jsonObject.getString(NAME));
            mobilization.setMappingId(jsonObject.getString(MAPPING));
            mobilization.setCountry(jsonObject.getString(COUNTRY));
            mobilization.setAddedBy(jsonObject.getInt(ADDED_BY));
            mobilization.setComment(jsonObject.getString(COMMENT));
            mobilization.setDateAdded(jsonObject.getLong(DATE_ADDED));
            mobilization.setSynced(jsonObject.getBoolean(SYNCED));
            mobilization.setCounty(jsonObject.getString(COUNTY));
            mobilization.setDistrict(jsonObject.getString(DISTRICT));
            mobilization.setSubCounty(jsonObject.getString(SUB_COUNTY));
            mobilization.setParish(jsonObject.getString(PARISH));
            this.addData(mobilization);
        }catch (Exception e){}
    }

    public Mobilization getMobilizationById(String id){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                id,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            Mobilization mobilization = new Mobilization();
            mobilization.setId(cursor.getString(cursor.getColumnIndex(ID)));
            mobilization.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            mobilization.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPING)));
            mobilization.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            mobilization.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDED_BY)));
            mobilization.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            mobilization.setDateAdded(cursor.getLong(cursor.getColumnIndex(DATE_ADDED)));
            mobilization.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED))== 1);
            mobilization.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
            mobilization.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
            mobilization.setSubCounty(cursor.getString(cursor.getColumnIndex(SUB_COUNTY)));
            mobilization.setParish(cursor.getString(cursor.getColumnIndex(PARISH)));
            db.close();
            return mobilization;
        }

    }

    public List<Mobilization> getMobilizationByCountyId(String countyId){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = COUNTY+" = ?";
        String[] whereArgs = new String[] {
                countyId,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<Mobilization> mobilizationList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Mobilization mobilization = new Mobilization();

            mobilization.setId(cursor.getString(cursor.getColumnIndex(ID)));
            mobilization.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            mobilization.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPING)));
            mobilization.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            mobilization.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDED_BY)));
            mobilization.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            mobilization.setDateAdded(cursor.getLong(cursor.getColumnIndex(DATE_ADDED)));
            mobilization.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED))== 1);
            mobilization.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
            mobilization.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
            mobilization.setSubCounty(cursor.getString(cursor.getColumnIndex(SUB_COUNTY)));
            mobilization.setParish(cursor.getString(cursor.getColumnIndex(PARISH)));

            mobilizationList.add(mobilization);
        }
        db.close();

        return mobilizationList;
    }

    public boolean isExist(Mobilization mobilization) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + mobilization.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public JSONObject getMobilizationJson() {

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

    public JSONObject getMobilizationToSyncAsJson() {

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = SYNCED+" = ?";
        String[] whereArgs = new String[] {
                "0",
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

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
    private void upgradeVersion2(SQLiteDatabase db) {}
}

