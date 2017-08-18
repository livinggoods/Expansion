package com.expansion.lg.kimaru.expansion.tables;

/**
 * Created by kimaru on 8/18/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.expansion.lg.kimaru.expansion.mzigos.IccmComponent;
import com.expansion.lg.kimaru.expansion.other.Constants;


import java.util.ArrayList;
import java.util.List;



public class IccmComponentTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="iccm_components";
    public static final String JSON_ROOT="components";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";
    public static String real_field = " REAL ";


    //applicant, recruitment, motivation, community, mentality, selling, health, investment,
    // interpersonal, commitment, total, selected, addedBy,
    // dateAdded, synced
    public static final String ID = "id";
    public static final String COMPONENTNAME= "component_name";
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String CLIENTTIME = "client_time";
    public static final String DATEADDED = "date_added";
    public static final String ARCHIVED = "archived";
    public static final String STATUS = "status";

    String [] columns=new String[]{ID, COMPONENTNAME, ADDED_BY, COMMENT, CLIENTTIME, DATEADDED,
            ARCHIVED, STATUS};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + integer_field + ", "
            + COMPONENTNAME + varchar_field + ", "
            + ADDED_BY + integer_field + ", "
            + COMMENT + text_field + ", "
            + CLIENTTIME + real_field + ", "
            + DATEADDED + varchar_field + ", "
            + ARCHIVED + integer_field + ", "
            + STATUS + varchar_field + ") ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public IccmComponentTable(Context context) {
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

    public long addData(IccmComponent component) {

        SQLiteDatabase db=getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(ID, component.getId());
        cv.put(COMPONENTNAME, component.getComponentName());
        cv.put(ADDED_BY, component.getAddedBy());
        cv.put(COMMENT, component.getComment());
        cv.put(CLIENTTIME, component.getClientTime());
        cv.put(DATEADDED, component.getDateAdded());
        cv.put(ARCHIVED, component.isArchived() ? 1: 0);
        cv.put(STATUS, component.getStatus());

        long id;
        if (isExist(component)){
            id = db.update(TABLE_NAME, cv, ID+"='"+component.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }

        db.close();
        return id;

    }
    public boolean isExist(IccmComponent component) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + component.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public List<IccmComponent> getIccmComponentData() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        List<IccmComponent> componentsList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            IccmComponent component = new IccmComponent();

            component.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            component.setComponentName(cursor.getString(cursor.getColumnIndex(COMPONENTNAME)));
            component.setAddedBy(cursor.getLong(cursor.getColumnIndex(ADDED_BY)));
            component.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            component.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENTTIME)));
            component.setDateAdded(cursor.getString(cursor.getColumnIndex(DATEADDED)));
            component.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
            component.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            componentsList.add(component);
        }
        db.close();
        return componentsList;
    }


    public IccmComponent getIccmComponentById (Integer id){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            IccmComponent component = new IccmComponent();
            component.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            component.setComponentName(cursor.getString(cursor.getColumnIndex(COMPONENTNAME)));
            component.setAddedBy(cursor.getLong(cursor.getColumnIndex(ADDED_BY)));
            component.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            component.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENTTIME)));
            component.setDateAdded(cursor.getString(cursor.getColumnIndex(DATEADDED)));
            component.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
            component.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            return component;
        }
    }

    //getInterviewById

    public void fromJson(JSONObject jsonObject){
        IccmComponent component = new IccmComponent();
        try {
            component.setId(jsonObject.getInt(ID));
            component.setComponentName(jsonObject.getString(COMPONENTNAME));
            component.setAddedBy(jsonObject.getLong(ADDED_BY));
            component.setComment(jsonObject.getString(COMMENT));
            component.setClientTime(jsonObject.getLong(CLIENTTIME));
            component.setDateAdded(jsonObject.getString(DATEADDED));
            component.setArchived(jsonObject.getInt(ARCHIVED)==1);
            component.setStatus(jsonObject.getString(STATUS));
            this.addData(component);
        }catch (Exception e){}
    }

    public JSONObject getIccmJson() {
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
    private void upgradeVersion2(SQLiteDatabase db) {}
}

