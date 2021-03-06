package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Mapping;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.sync.LocationDataSync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class MappingTable extends SQLiteOpenHelper {

    Context context;

    public static final String TABLE_NAME = "mapping";
    public static final String DATABASE_NAME = Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION = Constants.DATABASE_VERSION;


    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";
    public static String real_field = " REAL ";

    public static final String ID = "id";
    public static final String MAPPINGNAME = "name";
    public static final String COUNTRY = "country";
    public static final String COUNTY = "county";
    public static final String SUBCOUNTY = "subcounty";
    public static final String DISTRICT = "district";
    public static final String REGION = "region";
    public static final String ADDED_BY = "added_by";
    public static final String CONTACTPERSON = "contact_person";
    public static final String CONTACTPERSONPHONE = "phone";
    public static final String COMMENT = "comment";
    public static final String SYNCED = "synced";
    public static final String DATE_ADDED = "date_added";
    public static final String JSON_ROOT = "mappings";

    public static final String CREATE_DATABASE = "CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + MAPPINGNAME + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + COUNTY + varchar_field + ", "
            + DISTRICT + varchar_field + ", "
            + SUBCOUNTY + varchar_field + ", "
            + ADDED_BY + integer_field + ", "
            + CONTACTPERSON + varchar_field + ", "
            + CONTACTPERSONPHONE + varchar_field + ", "
            + REGION + varchar_field + ", "
            + COMMENT + text_field + ", "
            + SYNCED + integer_field + ", "
            + DATE_ADDED + real_field + "); ";

    public String[] columns = new String[]{ID, MAPPINGNAME, COUNTRY, COUNTY, ADDED_BY, CONTACTPERSON,
            CONTACTPERSONPHONE, COMMENT, DATE_ADDED, SYNCED, DISTRICT, SUBCOUNTY, REGION};

    public static final String DATABASE_DROP = "DROP TABLE IF EXISTS" + TABLE_NAME;
    public static final String DB_UPDATE_V2 = "ALTER TABLE " + TABLE_NAME +
            "  ADD " + SUBCOUNTY + varchar_field + ";";

    public static final String ADD_REGION_FIELD_SQL = "ALTER TABLE " + TABLE_NAME +
            "  ADD " + REGION + varchar_field + ";";

    public MappingTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        this.context = context;

        if (!isFieldExist(REGION)) {
            this.addRegionField();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            upgradeVersion2(db);
        }
    }

    public void addRegionField() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(ADD_REGION_FIELD_SQL);
    }

    public String addData(Mapping mapping) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, mapping.getId());
        cv.put(MAPPINGNAME, mapping.getMappingName());
        cv.put(COUNTRY, mapping.getCountry());
        cv.put(COUNTY, mapping.getCounty());
        cv.put(DISTRICT, mapping.getDistrict());
        cv.put(ADDED_BY, mapping.getAddedBy());
        cv.put(CONTACTPERSON, mapping.getContactPerson());
        cv.put(CONTACTPERSONPHONE, mapping.getContactPersonPhone());
        cv.put(COMMENT, mapping.getComment());
        cv.put(SUBCOUNTY, mapping.getSubCounty());
        cv.put(SYNCED, mapping.getSynced());
        cv.put(DATE_ADDED, mapping.getDateAdded());
        cv.put(REGION, mapping.getMappingRegion());

        long id;
        if (isExist(mapping)) {
            id = db.update(TABLE_NAME, cv, ID + "='" + mapping.getId() + "'", null);
            Log.d("Tremap DB Op", "Mapping updated");
        } else {
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Tremap DB Op", "Mapping Created");
        }
        db.close();
        return String.valueOf(id);

    }

    public boolean isExist(Mapping mapping) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE " + ID + " = '" + mapping.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }

    public List<Mapping> getMappingData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null);
        List<Mapping> mappingList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            mappingList.add(cursorToMapping(cursor));
        }
        db.close();
        return mappingList;
    }

    public Mapping getMappingByUuid(String uuid) {
        String whereClause = ID + " = ?";
        Log.d("======", "TREMAP " + uuid);
        String[] whereArgs = new String[]{uuid};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null);
        return cursorToMapping(cursor);
    }

    public Mapping getMappingById(String uuid) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = ID + " = ?";
        String[] whereArgs = new String[]{
                uuid
        };
        Cursor cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            return null;
        } else {
            Mapping mapping = cursorToMapping(cursor);
            db.close();
            return mapping;
        }
    }


    public List<Mapping> getMappingsByCountry(String countryCode) {

        SQLiteDatabase db = getReadableDatabase();
        String orderBy = DATE_ADDED + " desc";
        String whereClause = COUNTRY + " = ?";
        String[] whereArgs = new String[]{
                countryCode,
        };
        Cursor cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, orderBy, null);
        List<Mapping> mappingList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            mappingList.add(cursorToMapping(cursor));
        }
        db.close();

        return mappingList;
    }

    public Mapping getMappingByCounty(String uuid) {
        Mapping mapping = new Mapping();
        String[] selection = new String[]{uuid};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns, ID, selection, null, null, null, null);
        mapping = cursorToMapping(cursor);
        cursor.close();
        return mapping;
    }

    public Mapping getMappingByDistrict(String uuid) {
        Mapping mapping = new Mapping();
        String[] selection = new String[]{uuid};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns, ID, selection, null, null, null, null);
        mapping = cursorToMapping(cursor);
        cursor.close();
        return mapping;
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
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns,
                SYNCED + "=?", new String[]{Constants.SYNC_STATUS_UNSYNCED + ""}, null, null, null,
                String.format("%d,%d", offset, Constants.SYNC_PAGINATION_SIZE ));
        JSONObject results = new JSONObject();
        JSONArray resultSet = new JSONArray();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int totalColumns = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumns; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
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

    //JSON
    public JSONObject getJson() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null);
        JSONObject results = new JSONObject();
        JSONArray resultSet = new JSONArray();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int totalColumns = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumns; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
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

    public void fromJson(JSONObject jsonObject) {
        Mapping mapping = new Mapping();
        try {


            mapping.setId(jsonObject.getString(ID));
            if (!jsonObject.isNull(MAPPINGNAME)) {
                mapping.setMappingName(jsonObject.getString(MAPPINGNAME));
            } else {
                mapping.setMappingName("");
            }

            if (!jsonObject.isNull(COUNTRY)) {
                mapping.setCountry(jsonObject.getString(COUNTRY));
            }
            if (!jsonObject.isNull(COUNTY)) {
                mapping.setCounty(jsonObject.getString(COUNTY));
            }
            if (!jsonObject.isNull(ADDED_BY)) {
                mapping.setAddedBy(jsonObject.getInt(ADDED_BY));
            }
            if (!jsonObject.isNull(CONTACTPERSON)) {
                mapping.setContactPerson(jsonObject.getString(CONTACTPERSON));
            }
            if (!jsonObject.isNull(CONTACTPERSONPHONE)) {
                mapping.setContactPersonPhone(jsonObject.getString(CONTACTPERSONPHONE));
            }
            if (!jsonObject.isNull(COMMENT)) {
                mapping.setComment(jsonObject.getString(COMMENT));
            }
            if (!jsonObject.isNull(DATE_ADDED)) {
                mapping.setDateAdded(jsonObject.getLong(DATE_ADDED));
            }
            if (!jsonObject.isNull(SYNCED)) {
                mapping.setSynced(jsonObject.getInt(SYNCED));
            }
            if (!jsonObject.isNull(DISTRICT)) {
                mapping.setDistrict(jsonObject.getString(DISTRICT));
            }
            if (!jsonObject.isNull(SUBCOUNTY)) {
                mapping.setSubCounty(jsonObject.getString(SUBCOUNTY));
            }
            addData(mapping);
        } catch (Exception e) {
            Log.d("Tremap", "ERROR CREATING MAPPING FROM JSON");
            Log.d("Tremap", "CE ERROR " + e.getMessage());
        }
    }

    private void upgradeVersion2(SQLiteDatabase db) {
        // add column
        db.execSQL(DB_UPDATE_V2);
        LocationDataSync locationDataSync = new LocationDataSync(context);
        locationDataSync.pollLocations();
    }

    private Mapping cursorToMapping(Cursor cursor) {
        Mapping mapping = new Mapping();
        mapping.setId(cursor.getString(cursor.getColumnIndex(ID)));
        mapping.setMappingName(cursor.getString(cursor.getColumnIndex(MAPPINGNAME)));
        mapping.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
        mapping.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
        mapping.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDED_BY)));
        mapping.setContactPerson(cursor.getString(cursor.getColumnIndex(CONTACTPERSON)));
        mapping.setContactPersonPhone(cursor.getString(cursor.getColumnIndex(CONTACTPERSONPHONE)));
        mapping.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
        mapping.setDateAdded(cursor.getLong(cursor.getColumnIndex(DATE_ADDED)));
        mapping.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED)));
        mapping.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
        mapping.setSubCounty(cursor.getString(cursor.getColumnIndex(SUBCOUNTY)));
        mapping.setMappingRegion(cursor.getString(cursor.getColumnIndex(REGION)));
        return mapping;
    }

    public boolean isFieldExist(String fieldName) {
        SQLiteDatabase db = getReadableDatabase();
        boolean isExist = false;
        Cursor res = null;
        try {
            res = db.rawQuery("Select * from " + TABLE_NAME + " limit 1", null);
            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex != -1) {
                isExist = true;
            } else {
                Log.d("Tremap", "The col " + fieldName + " is NOT found");
            }
        } catch (Exception e) {
            Log.d("Tremap", "Error getting  " + fieldName);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception e1) {
            }
        }
        return isExist;
    }

    /**
     * BUGFIX
     * This methods removes all the invalid mapping from sqlite
     */
    public void removeInvalidRecords() {
        SQLiteDatabase db = getWritableDatabase();

        // Remove records where mapping is an empty string
        db.delete(TABLE_NAME, MAPPINGNAME + "=?", new String[]{""});

        // Remove all the mapping with invalid IDs
        db.delete(TABLE_NAME, ID + " LIKE '[%]'", null);

        db.close();
    }
}