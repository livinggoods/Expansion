package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class CountyLocationTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="location";
    public static final String JSON_ROOT="locations";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String varchar_field = " varchar(512) ";
    public static String real_field = " REAL ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID= "id";
    public static final String ADMIN_NAME= "admin_name";
    public static final String ARCHIVED = "archived";
    public static final String CODE = "code";
    public static final String COUNTRY = "country";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String META = "meta";
    public static final String NAME = "name";
    public static final String PARENT = "parent";
    public static final String POLYGON = "polygon";
    String [] columns=new String[]{ID, NAME, ADMIN_NAME, CODE, COUNTRY, LAT, LON, META,
            PARENT, POLYGON, ARCHIVED};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + ", "
            + NAME + varchar_field + ", "
            + ADMIN_NAME + varchar_field + ", "
            + CODE + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + LAT + varchar_field + ", "
            + LON + varchar_field + ", "
            + META + text_field + ", "
            + PARENT + real_field + ", "
            + POLYGON + text_field + ", "
            + ARCHIVED + integer_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public CountyLocationTable(Context context) {
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

    public long addData(CountyLocation countyLocation) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, countyLocation.getId());
        cv.put(NAME, countyLocation.getName());
        cv.put(ADMIN_NAME, countyLocation.getAdminName());
        cv.put(CODE, countyLocation.getCountry());
        cv.put(COUNTRY, countyLocation.getCode());
        cv.put(LAT, countyLocation.getLat());
        cv.put(LON, countyLocation.getLon());
        cv.put(META, countyLocation.getMeta());
        cv.put(PARENT, countyLocation.getParent());
        cv.put(POLYGON, countyLocation.getPolygon());
        cv.put(ARCHIVED, countyLocation.getArchived());

        long id;
        if (isExist(countyLocation)){
            id = db.update(TABLE_NAME, cv, ID+"='"+ countyLocation.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;

    }
    public List<CountyLocation> getLocationData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<CountyLocation> countyLocationList =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

            CountyLocation countyLocation = new CountyLocation();

            countyLocation.setId(cursor.getInt(0));
            countyLocation.setName(cursor.getString(1));
            countyLocation.setAdminName(cursor.getString(2));
            countyLocation.setCode(cursor.getString(3));
            countyLocation.setCountry(cursor.getString(4));
            countyLocation.setLat(cursor.getString(5));
            countyLocation.setLon(cursor.getString(6));
            countyLocation.setMeta(cursor.getString(7));
            countyLocation.setParent(cursor.getInt(8));
            countyLocation.setPolygon(cursor.getString(9));
            countyLocation.setArchived(cursor.getInt(10));

            countyLocationList.add(countyLocation);
        }
        db.close();

        return countyLocationList;
    }

    public void fromJson(JSONObject jsonObject){
        try{
            CountyLocation countyLocation = new CountyLocation();
            countyLocation.setId(jsonObject.getInt(ID));
            countyLocation.setName(jsonObject.getString(NAME));
            countyLocation.setAdminName(jsonObject.getString(ADMIN_NAME));
            countyLocation.setCode(jsonObject.getString(CODE));
            countyLocation.setCountry(jsonObject.getString(COUNTRY));
            countyLocation.setLon(jsonObject.getString(LON));
            countyLocation.setLat(jsonObject.getString(LAT));
            countyLocation.setMeta(jsonObject.getString(META));
            countyLocation.setParent(jsonObject.getInt(PARENT));
            countyLocation.setPolygon(jsonObject.getString(POLYGON));
            countyLocation.setArchived(jsonObject.getInt(ARCHIVED));
            this.addData(countyLocation);
        }catch (Exception e){}
    }

    public CountyLocation getLocationById(String id){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                id,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            String [] columns=new String[]{ID, NAME, ADMIN_NAME, CODE, COUNTRY, LAT, LON, META,
                    PARENT, POLYGON, ARCHIVED};
            CountyLocation countyLocation = new CountyLocation();
            countyLocation.setId(cursor.getInt(0));
            countyLocation.setName(cursor.getString(1));
            countyLocation.setAdminName(cursor.getString(2));
            countyLocation.setCode(cursor.getString(3));
            countyLocation.setCountry(cursor.getString(4));
            countyLocation.setLat(cursor.getString(5));
            countyLocation.setLon(cursor.getString(6));
            countyLocation.setMeta(cursor.getString(7));
            countyLocation.setParent(cursor.getInt(8));
            countyLocation.setPolygon(cursor.getString(9));
            countyLocation.setArchived(cursor.getInt(10));
            cursor.close();
            return countyLocation;
        }

    }
    public List<CountyLocation> getChildrenLocations(CountyLocation parentCountyLocation){
        SQLiteDatabase db=getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(parentCountyLocation.getId())
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<CountyLocation> countyLocationList =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            CountyLocation countyLocation = new CountyLocation();

            countyLocation.setId(cursor.getInt(0));
            countyLocation.setName(cursor.getString(1));
            countyLocation.setAdminName(cursor.getString(2));
            countyLocation.setCode(cursor.getString(3));
            countyLocation.setCountry(cursor.getString(4));
            countyLocation.setLat(cursor.getString(5));
            countyLocation.setLon(cursor.getString(6));
            countyLocation.setMeta(cursor.getString(7));
            countyLocation.setParent(cursor.getInt(8));
            countyLocation.setPolygon(cursor.getString(9));
            countyLocation.setArchived(cursor.getInt(10));

            countyLocationList.add(countyLocation);
        }
        db.close();

        return countyLocationList;
    }

    public List<CountyLocation> getCounties(){
        SQLiteDatabase db=getReadableDatabase();
        String whereClause = ADMIN_NAME+" = ?";
        String[] whereArgs = new String[] {
                "County"
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<CountyLocation> countyLocationList =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            CountyLocation countyLocation = new CountyLocation();

            countyLocation.setId(cursor.getInt(0));
            countyLocation.setName(cursor.getString(1));
            countyLocation.setAdminName(cursor.getString(2));
            countyLocation.setCode(cursor.getString(3));
            countyLocation.setCountry(cursor.getString(4));
            countyLocation.setLat(cursor.getString(5));
            countyLocation.setLon(cursor.getString(6));
            countyLocation.setMeta(cursor.getString(7));
            countyLocation.setParent(cursor.getInt(8));
            countyLocation.setPolygon(cursor.getString(9));
            countyLocation.setArchived(cursor.getInt(10));

            countyLocationList.add(countyLocation);
        }
        db.close();

        return countyLocationList;
    }


    public boolean isExist(CountyLocation countyLocation) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + countyLocation.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }
}

