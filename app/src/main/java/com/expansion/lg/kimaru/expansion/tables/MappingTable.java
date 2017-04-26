package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Mapping;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class MappingTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="mapping";
    public static final String DATABASE_NAME="expansion";
    public static final int DATABASE_VERSION=1;


    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String MAPPINGNAME= "name";
    public static final String COUNTRY = "country";
    public static final String COUNTY = "county";
    public static final String DISTRICT = "district";
    public static final String ADDED_BY = "added_by";
    public static final String CONTACTPERSON = "contact_person";
    public static final String CONTACTPERSONPHONE = "phone";
    public static final String COMMENT = "comment";
    public static final String SYNCED = "synced";
    public static final String DATE_ADDED = "date_added";

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + MAPPINGNAME + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + COUNTY + varchar_field + ", "
            + DISTRICT + varchar_field + ", "
            + ADDED_BY + integer_field + ", "
            + CONTACTPERSON + varchar_field + ", "
            + CONTACTPERSONPHONE + varchar_field + ", "
            + COMMENT + text_field + ", "
            + SYNCED + integer_field + ", "
            + DATE_ADDED + integer_field + "); ";

    String [] columns=new String[]{ID, MAPPINGNAME, COUNTRY, COUNTY, ADDED_BY, CONTACTPERSON,
            CONTACTPERSONPHONE, COMMENT, DATE_ADDED, SYNCED, DISTRICT};

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public MappingTable(Context context) {
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

    public String addData(Mapping mapping) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, mapping.getId());
        cv.put(MAPPINGNAME, mapping.getMappingName());
        cv.put(COUNTRY, mapping.getCountry());
        cv.put(COUNTY, mapping.getCounty());
        cv.put(DISTRICT, mapping.getDistrict());
        cv.put(ADDED_BY, mapping.getAddedBy());
        cv.put(CONTACTPERSON, mapping.getContactPerson());
        cv.put(CONTACTPERSONPHONE, mapping.getContactPersonPhone());
        cv.put(COMMENT, mapping.getComment());
        cv.put(SYNCED, mapping.isSynced() ? 1 : 0);
        cv.put(DATE_ADDED, mapping.getDateAdded());

        long Is = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        return String.valueOf(Is);

    }
    public List<Mapping> getMappingData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Mapping> mappingList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Mapping mapping=new Mapping();

            mapping.setId(cursor.getString(0));
            mapping.setMappingName(cursor.getString(1));
            mapping.setCountry(cursor.getString(2));
            mapping.setCounty(cursor.getString(3));
            mapping.setAddedBy(cursor.getInt(4));
            mapping.setContactPerson(cursor.getString(5));
            mapping.setContactPersonPhone(cursor.getString(6));
            mapping.setComment(cursor.getString(7));
            mapping.setDateAdded(cursor.getInt(8));
            mapping.setSynced(cursor.getInt(9) == 1);
            mapping.setDistrict(cursor.getString(10));

            mappingList.add(mapping);
        }
        db.close();

        return mappingList;
    }

    public Mapping getMappingById(String uuid){
        Mapping mapping = new Mapping();
        String [] selection = new String[]{uuid};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns,ID, selection, null,null,null,null);
        mapping.setId(cursor.getString(0));
        mapping.setMappingName(cursor.getString(1));
        mapping.setCountry(cursor.getString(2));
        mapping.setCounty(cursor.getString(3));
        mapping.setAddedBy(cursor.getInt(4));
        mapping.setContactPerson(cursor.getString(5));
        mapping.setContactPersonPhone(cursor.getString(6));
        mapping.setComment(cursor.getString(7));
        mapping.setDateAdded(cursor.getInt(8));
        mapping.setSynced(cursor.getInt(9) == 1);
        mapping.setDistrict(cursor.getString(10));
        return mapping;
    }
    public List<Mapping> getMappingsByCountry(String countryCode) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED + " desc";
        String whereClause = COUNTRY+" = ?";
        String[] whereArgs = new String[] {
                countryCode,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);
        List<Mapping> mappingList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Mapping mapping=new Mapping();
            mapping.setId(cursor.getString(0));
            mapping.setMappingName(cursor.getString(1));
            mapping.setCountry(cursor.getString(2));
            mapping.setCounty(cursor.getString(3));
            mapping.setAddedBy(cursor.getInt(4));
            mapping.setContactPerson(cursor.getString(5));
            mapping.setContactPersonPhone(cursor.getString(6));
            mapping.setComment(cursor.getString(7));
            mapping.setDateAdded(cursor.getInt(8));
            mapping.setSynced(cursor.getInt(9) == 1);
            mapping.setDistrict(cursor.getString(10));

            mappingList.add(mapping);
        }
        db.close();

        return mappingList;
    }
    public Mapping getMappingByCounty(String uuid){
        Mapping mapping = new Mapping();
        String [] selection = new String[]{uuid};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns,ID, selection, null,null,null,null);
        mapping.setId(cursor.getString(0));
        mapping.setMappingName(cursor.getString(1));
        mapping.setCountry(cursor.getString(2));
        mapping.setCounty(cursor.getString(3));
        mapping.setAddedBy(cursor.getInt(4));
        mapping.setContactPerson(cursor.getString(5));
        mapping.setContactPersonPhone(cursor.getString(6));
        mapping.setComment(cursor.getString(7));
        mapping.setDateAdded(cursor.getInt(8));
        mapping.setSynced(cursor.getInt(9) == 1);
        mapping.setDistrict(cursor.getString(10));
        return mapping;
    }
    public Mapping getMappingByDistrict(String uuid){
        Mapping mapping = new Mapping();
        String [] selection = new String[]{uuid};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns,ID, selection, null,null,null,null);
        mapping.setId(cursor.getString(0));
        mapping.setMappingName(cursor.getString(1));
        mapping.setCountry(cursor.getString(2));
        mapping.setCounty(cursor.getString(3));
        mapping.setAddedBy(cursor.getInt(4));
        mapping.setContactPerson(cursor.getString(5));
        mapping.setContactPersonPhone(cursor.getString(6));
        mapping.setComment(cursor.getString(7));
        mapping.setDateAdded(cursor.getInt(8));
        mapping.setSynced(cursor.getInt(9) == 1);
        mapping.setDistrict(cursor.getString(10));
        return mapping;
    }
}