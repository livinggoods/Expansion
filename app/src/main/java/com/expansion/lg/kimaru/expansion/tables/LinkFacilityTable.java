package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class LinkFacilityTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="link_facility";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String NAME = "facility_name";
    public static final String COUNTY = "county";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String SUBCOUNTY = "subcounty";
    public static final String ADDED = "date_added";
    public static final String ADDEDBY = "added_by";
    public static final String MRDTLEVELS = "mrdt_levels";
    public static final String ACTLEVELS = "act_levels";
    public static final String COUNTRY = "country";

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field +", "
            + NAME + varchar_field + ", "
            + COUNTY + varchar_field + ", "
            + LAT + varchar_field + ", "
            + LON + varchar_field + ", "
            + SUBCOUNTY + varchar_field + ", "
            + ADDED + integer_field + ", "
            + ADDEDBY + integer_field + ", "
            + MRDTLEVELS + integer_field + ", "
            + ACTLEVELS + integer_field + ", "
            + COUNTRY + varchar_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public LinkFacilityTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("Link Facility", "upgrading database from" + oldVersion + "to" + newVersion);
        db.execSQL(DATABASE_DROP);
    }

    public long addData(LinkFacility linkFacility) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, linkFacility.getId());
        cv.put(NAME, linkFacility.getFacilityName());
        cv.put(COUNTY, linkFacility.getMappingId());
        cv.put(LAT, linkFacility.getLat());
        cv.put(LON, linkFacility.getLon());
        cv.put(SUBCOUNTY, linkFacility.getSubCountyId());
        cv.put(ADDED, linkFacility.getDateAdded());
        cv.put(ADDEDBY, linkFacility.getAddedBy());
        cv.put(MRDTLEVELS, linkFacility.getMrdtLevels());
        cv.put(ACTLEVELS, linkFacility.getActLevels());
        cv.put(COUNTRY, linkFacility.getCountry());


        long id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
        return id;

    }
    public List<LinkFacility> getLinkFacilityData() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, NAME, COUNTY, LAT, LON, SUBCOUNTY, ADDED, ADDEDBY,
                MRDTLEVELS, ACTLEVELS, COUNTRY};

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<LinkFacility> facilityList =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            LinkFacility linkFacility =new LinkFacility();

            linkFacility.setId(cursor.getString(0));
            linkFacility.setFacilityName(cursor.getString(1));
            linkFacility.setMappingId(cursor.getString(2));
            linkFacility.setLat(cursor.getString(3));
            linkFacility.setLon(cursor.getString(4));
            linkFacility.setSubCountyId(cursor.getString(5));
            linkFacility.setDateAdded(cursor.getInt(6));
            linkFacility.setAddedBy(cursor.getInt(7));
            linkFacility.setMrdtLevels(cursor.getInt(8));
            linkFacility.setActLevels(cursor.getInt(9));
            linkFacility.setCountry(cursor.getString(10));

            facilityList.add(linkFacility);
        }
        db.close();
        return facilityList;
    }

    public Cursor getUserCursor() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, NAME, COUNTY, LAT, LON, SUBCOUNTY, ADDED, ADDEDBY,
                MRDTLEVELS, ACTLEVELS, COUNTRY};

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
    }

    public JSONObject getJson() {

        SQLiteDatabase db=getReadableDatabase();

        String [] columns=new String[]{ID, NAME, COUNTY, LAT, LON, SUBCOUNTY, ADDED, ADDEDBY,
                MRDTLEVELS, ACTLEVELS, COUNTRY};

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
                results.put("users", resultSet);
            } catch (JSONException e) {

            }
        }
        cursor.close();
        db.close();
        return results;
    }

}

