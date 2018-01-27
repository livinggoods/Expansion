package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Parish;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        long Is = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        return String.valueOf(Is);

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
        Cursor cursor = db.query(TABLE_NAME, columns,ID, selection, null,null,null,null);
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

            parish.setId(jsonObject.getString(ID));
            parish.setName(jsonObject.getString(PARISHNAME));
            parish.setContactPerson(jsonObject.getString(CONTACTPERSON));
            parish.setContactPersonPhone(jsonObject.getString(CONTACTPERSONPHONE));
            parish.setParent(jsonObject.getString(PARENT_LOCATION));
            parish.setMapping(jsonObject.getString(MAPPINGID));
            parish.setDateAdded(jsonObject.getLong(DATE_ADDED));
            parish.setAddedBy(jsonObject.getInt(ADDED_BY));
            parish.setSynced(jsonObject.getInt(SYNCED));
            parish.setCountry(jsonObject.getString(COUNTRY));
            parish.setComment(jsonObject.getString(COMMENT));
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