package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.LinkFacility;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.other.UtilFunctions;

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

    public static String JSON_ROOT = "link_facilities";

    public static final String ID = "id";
    public static final String NAME = "facility_name";
    public static final String COUNTY = "county";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String SUBCOUNTY = "subcounty";
    public static final String MFLCODE = "mfl_code";
    public static final String MAPPING = "mapping";
    public static final String ADDED = "date_added";
    public static final String ADDEDBY = "added_by";
    public static final String MRDTLEVELS = "mrdt_levels";
    public static final String ACTLEVELS = "act_levels";
    public static final String COUNTRY = "country";
    public static final String PARISH = "parish";
    public static final String OTHER = "other";
    public static final String SYNCED = "synced";
    String [] columns=new String[]{ID, NAME, COUNTY, LAT, LON, SUBCOUNTY, ADDED, ADDEDBY,
            MRDTLEVELS, ACTLEVELS, COUNTRY, MFLCODE, MAPPING, PARISH, OTHER};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field +", "
            + NAME + varchar_field + ", "
            + COUNTY + varchar_field + ", "
            + LAT + varchar_field + ", "
            + LON + varchar_field + ", "
            + MFLCODE + varchar_field + ", "
            + MAPPING + varchar_field + ", "
            + SUBCOUNTY + varchar_field + ", "
            + ADDED + integer_field + ", "
            + ADDEDBY + integer_field + ", "
            + MRDTLEVELS + integer_field + ", "
            + ACTLEVELS + integer_field + ", "
            + PARISH + varchar_field + ", "
            + OTHER + text_field + ", "
            + SYNCED + integer_field + ", "
            + COUNTRY + varchar_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public LinkFacilityTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        if (!isFieldExist(PARISH)){
            this.addParishFields();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE);

    }
    public void addParishFields(){
        String ADD_PARISH = "ALTER TABLE " + TABLE_NAME +
                "  ADD "+ PARISH + varchar_field +";";
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(ADD_PARISH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("Link Facility", "upgrading database from" + oldVersion + "to" + newVersion);
        if (oldVersion < 2){
            upgradeVersion2(db);
        }

        if (oldVersion < 3) {
            upgradeVersion3(db);
        }
    }

    public long addData(LinkFacility linkFacility) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, linkFacility.getId());
        cv.put(NAME, linkFacility.getFacilityName());
        cv.put(COUNTY, linkFacility.getCounty());
        cv.put(MAPPING, linkFacility.getMappingId());
        cv.put(LAT, linkFacility.getLat());
        cv.put(LON, linkFacility.getLon());
        cv.put(MFLCODE, linkFacility.getMflCode());
        cv.put(SUBCOUNTY, linkFacility.getSubCountyId());
        cv.put(ADDED, linkFacility.getDateAdded());
        cv.put(ADDEDBY, linkFacility.getAddedBy());
        cv.put(MRDTLEVELS, linkFacility.getMrdtLevels());
        cv.put(ACTLEVELS, linkFacility.getActLevels());
        cv.put(COUNTRY, linkFacility.getCountry());
        cv.put(PARISH, linkFacility.getParish());
        cv.put(OTHER, linkFacility.getOther());
        cv.put(SYNCED, linkFacility.getSynced());

        long id;
        if (isExist(linkFacility)){
            id = db.update(TABLE_NAME, cv, ID+"='"+linkFacility.getId()+"'", null);
            Log.d("Tremap DB Op", "Link Facility updated");
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Tremap DB Op", "Link Facility Created");
        }

        db.close();
        return id;

    }
    public boolean isExist(LinkFacility linkFacility) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + linkFacility.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }

    public List<LinkFacility> getLinkFacilityData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<LinkFacility> facilityList =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            LinkFacility linkFacility= cursorToLinkFacility(cursor);
            facilityList.add(linkFacility);
        }
        db.close();
        return facilityList;
    }

    public List<LinkFacility> getLinkFacilityByParish(String parishId) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = PARISH+" = ? ";

        String[] whereArgs = new String[] {
                parishId
        };

        List<LinkFacility> facilityList =new ArrayList<>();
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            LinkFacility linkFacility = cursorToLinkFacility(cursor);
            facilityList.add(linkFacility);
        }
        db.close();
        return facilityList;
    }

    public List<LinkFacility> getLinkFacilityBySubCounty(String subCountyId) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = SUBCOUNTY+" = ? ";

        String[] whereArgs = new String[] {
                subCountyId
        };

        List<LinkFacility> facilityList =new ArrayList<>();
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            LinkFacility linkFacility = cursorToLinkFacility(cursor);
            facilityList.add(linkFacility);
        }
        db.close();
        return facilityList;
    }

    public LinkFacility getLinkFacilityByName(String facilityName) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = NAME+" = ? ";

        String[] whereArgs = new String[] {
                facilityName
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else {
            LinkFacility linkFacility =  cursorToLinkFacility(cursor);
            db.close();
            return linkFacility;
        }
    }

    public LinkFacility getLinkFacilityById(String uuid) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = ID+" = ? ";

        String[] whereArgs = new String[] {
                uuid
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else {
            LinkFacility linkFacility =cursorToLinkFacility(cursor);
            db.close();
            return linkFacility;
        }
    }

    public Cursor getLinkFacilityCursor() {

        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
    }

    public long getAllRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                null,
                null);
        db.close();
        return cnt;
    }

    public long getPendingRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                SYNCED + "=?",
                new String[] {String.valueOf(Constants.SYNC_STATUS_UNSYNCED)});
        db.close();
        return cnt;
    }

    public JSONObject getPayload(int offset) {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,
                SYNCED+ "=?",new String[]{Constants.SYNC_STATUS_UNSYNCED+""},null,null,null,
                String.format("%d,%d", offset, Constants.SYNC_PAGINATION_SIZE ));

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
                        e.printStackTrace();
                    }
                }
            }
            resultSet.put(rowObject);
            try {
                results.put(JSON_ROOT, resultSet);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return results;
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
        LinkFacility linkFacility = new LinkFacility();
        try {

            if (!jsonObject.isNull(ID)){
                linkFacility.setId(jsonObject.getString(ID));
            }
            if (!jsonObject.isNull(NAME)){
                linkFacility.setFacilityName(jsonObject.getString(NAME));
            }

            if (!jsonObject.isNull(LAT)){
                linkFacility.setLat(jsonObject.getDouble(LAT));
            }

            if (!jsonObject.isNull(LON)){
                linkFacility.setLon(jsonObject.getDouble(LON));
            }

            if (!jsonObject.isNull(MAPPING)){
                linkFacility.setMappingId(jsonObject.getString(MAPPING));
            }

            if (!jsonObject.isNull(MAPPING)){
                linkFacility.setMappingId(jsonObject.getString(MAPPING));
            }

            if (!jsonObject.isNull(MFLCODE)){
                linkFacility.setMflCode(jsonObject.getString(MFLCODE));
            }

            if (!jsonObject.isNull(SUBCOUNTY)){
                linkFacility.setSubCountyId(jsonObject.getString(SUBCOUNTY));
            }

            if (!jsonObject.isNull(ADDED)){
                linkFacility.setDateAdded(jsonObject.getLong(ADDED));
            }else{
                linkFacility.setDateAdded(1L);
            }

            if (!jsonObject.isNull(COUNTY)){
                linkFacility.setCounty(jsonObject.getString(COUNTY));
            }

            if (!jsonObject.isNull(ADDEDBY)){
                linkFacility.setAddedBy(jsonObject.getInt(ADDEDBY));
            }

            if (!jsonObject.isNull(MRDTLEVELS)){
                linkFacility.setMrdtLevels(jsonObject.getInt(MRDTLEVELS));
            }

            if (!jsonObject.isNull(ACTLEVELS)){
                linkFacility.setActLevels(jsonObject.getInt(ACTLEVELS));
            }
            if (!jsonObject.isNull(COUNTRY)){
                linkFacility.setCountry(jsonObject.getString(COUNTRY));
            }
            if (!jsonObject.isNull(PARISH)){
                linkFacility.setParish(jsonObject.getString(PARISH));
            }

            linkFacility.setOther(jsonObject.getString("other"));
            ///////
            addData(linkFacility);
        }catch (Exception e){
            Log.d("Tremap ERR", "Link Facility from JSON "+e.getMessage());
        }
    }
    private LinkFacility cursorToLinkFacility(Cursor cursor){
        LinkFacility linkFacility =new LinkFacility();
        linkFacility.setId(cursor.getString(cursor.getColumnIndex(ID)));
        linkFacility.setFacilityName(cursor.getString(cursor.getColumnIndex(NAME)));
        linkFacility.setMappingId(cursor.getString(cursor.getColumnIndex(MAPPING)));
        linkFacility.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
        linkFacility.setMflCode(cursor.getString(cursor.getColumnIndex(MFLCODE)));
        linkFacility.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
        linkFacility.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));
        linkFacility.setSubCountyId(cursor.getString(cursor.getColumnIndex(SUBCOUNTY)));
        linkFacility.setDateAdded(cursor.getLong(cursor.getColumnIndex(ADDED)));
        linkFacility.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDEDBY)));
        linkFacility.setMrdtLevels(cursor.getInt(cursor.getColumnIndex(MRDTLEVELS)));
        linkFacility.setActLevels(cursor.getInt(cursor.getColumnIndex(ACTLEVELS)));
        linkFacility.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
        linkFacility.setParish(cursor.getString(cursor.getColumnIndex(PARISH)));
        linkFacility.setOther(cursor.getString(cursor.getColumnIndex(OTHER)));

        return linkFacility;
    }
    private void upgradeVersion2(SQLiteDatabase db) {}

    private void upgradeVersion3(SQLiteDatabase db) {
        if (!UtilFunctions.isColumnExists(db, TABLE_NAME, SYNCED))
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + SYNCED + integer_field + ";");

        if (!UtilFunctions.isColumnExists(db, TABLE_NAME, OTHER))
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + OTHER + text_field + " default '{}';");
    }

    public boolean isFieldExist(String fieldName)
    {
        SQLiteDatabase db = getReadableDatabase();
        boolean isExist = false;
        Cursor res = null;
        try {
            res = db.rawQuery("Select * from "+ TABLE_NAME +" limit 1", null);
            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex!=-1){
                isExist = true;
            }else{
                Log.d("Tremap", "The col "+fieldName+" is NOT found");
            }

        } catch (Exception e) {
            Log.d("Tremap", "Error getting  "+fieldName);
        } finally {
            try {
                if (res !=null){ res.close();}
            } catch (Exception e1) {
                Log.d("Tremap", "Error closing the  DB  "+fieldName);
            }
        }
        return isExist;
    }

}

