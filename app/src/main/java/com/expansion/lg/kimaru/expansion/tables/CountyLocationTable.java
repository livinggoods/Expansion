package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.sync.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class CountyLocationTable extends SQLiteOpenHelper {

    Context context;
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
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL " + ", "
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
            id = db.update(TABLE_NAME, cv, ID+" = '"+countyLocation.getId()+"'", null);
            Log.e("expansion ugcountytable", "Updated ID : " + String.valueOf(countyLocation.getId()));
        }else{
            id = db.insert(TABLE_NAME, null, cv);
            Log.e("expansion ugcountytable", "New record - ID is " + String.valueOf(id));
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
    public CountyLocation getDistrictByName(String name){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = NAME+" = ? AND (" +
                ADMIN_NAME +" = ? OR " +
                ADMIN_NAME +" = ? ) ";
        String[] whereArgs = new String[] {
                name,
                "District",
                "County"
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
    public CountyLocation getCountyOrDistrictByName(String name){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = NAME+" = ? AND (" +
                ADMIN_NAME +" = ? OR " +
                ADMIN_NAME +" = ? ) ";
        String[] whereArgs = new String[] {
                name,
                "County",
                "DISTRICT"
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

    public CountyLocation getCountyByName(String name){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = NAME+" = ? AND " +
                            ADMIN_NAME +" = ? ";
        String[] whereArgs = new String[] {
                name,
                "County"
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
        String orderBy = NAME + " asc";
        String whereClause = PARENT+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(parentCountyLocation.getId())
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);

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
        String orderBy = NAME + " asc";
        String whereClause = ADMIN_NAME+" = ?";
        String[] whereArgs = new String[] {
                "County"
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);

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

    public List<CountyLocation> getCountiesAndDistricts(){
        SQLiteDatabase db=getReadableDatabase();
        String orderBy = NAME + " asc";
        String whereClause = ADMIN_NAME +" = ? OR " +
                ADMIN_NAME +" = ? ";
        String[] whereArgs = new String[] {
                "County",
                "District"
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);

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
    public long nextIncrementValue (){
        SQLiteDatabase db = getReadableDatabase();
        Long id = 0L;
        Cursor c = db.rawQuery("select seq from sqlite_sequence WHERE name = '" +TABLE_NAME+"'", null);
        if (c.moveToFirst()){
            do {
                id = c.getLong(0);
            } while (c.moveToNext());
        }
        return id +1;
    }

    public void insertRawSql(String name,String admin_name, String code, String country, String lat,
                             String lon, String meta, Long parent, String polygon){
        SQLiteDatabase db = getReadableDatabase();
        db.rawQuery("INSERT INTO `location`(`id`,`name`,`admin_name`,`code`,`country`," +
                "`lat`,`lon`,`meta`,`parent`,`polygon`) VALUES (NULL,'"+name+"','"+admin_name+"','"
                +code+"','"+country+"','"+ lat+"','"+lon+"','"+meta+"','"+parent+"','"+polygon+"');", null);
    }
    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return cnt;
    }
    private void upgradeVersion2(SQLiteDatabase db) {
        createLocations();
    }
    public void createLocations(){
        if (getProfilesCount() < 1){
            new syncLocations().execute(new Constants(context).getCloudAddress()+"/api/v1/sync/locations");
        }

    }
    private class syncLocations extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("locations");
                    CountyLocationTable countyLocationTable = new CountyLocationTable(context);
                    for (int x = 0; x < recs.length(); x++){
                        countyLocationTable.fromJson(recs.getJSONObject(x));
                    }
                }catch(JSONException e){
                }

            }
            return stream;
        }
        protected void onPostExecute(String stream){

        } // onPostExecute() end
    }
}

