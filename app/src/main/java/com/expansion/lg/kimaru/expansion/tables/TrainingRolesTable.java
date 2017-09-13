package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.TrainingRole;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class TrainingRolesTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="training_roles";
    public static final String JSON_ROOT="training_roles";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    Context context;


    public static final String ID = "id";
    public static final String ROLE_NAME = "role_name";
    public static final String ARCHIVED = "archived";
    public static final String READONLY = "readonly";
    public static final String COUNTRY = "country";
    public static final String CLIENT_TIME = "client_time";
    public static final String CREATED_BY = "created_by";


    String [] columns=new String[]{ID, ROLE_NAME, ARCHIVED, READONLY, COUNTRY,
            CLIENT_TIME, CREATED_BY};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + Constants.integer_field + ", "
            + ROLE_NAME + Constants.varchar_field + ", "
            + ARCHIVED + Constants.varchar_field + ", "
            + READONLY + Constants.varchar_field + ", "
            + COUNTRY + Constants.real_field + ", "
            + CLIENT_TIME + Constants.real_field + ", "
            + CREATED_BY + Constants.integer_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public TrainingRolesTable(Context context) {
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

    public long addData(TrainingRole trainingRole) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, trainingRole.getId());
        cv.put(ROLE_NAME, trainingRole.getRoleName());
        cv.put(ARCHIVED, trainingRole.isArchived() ? 1:0);
        cv.put(READONLY, trainingRole.isReadOnly()? 1:0);
        cv.put(COUNTRY, trainingRole.getCountry());
        cv.put(CLIENT_TIME, trainingRole.getClientTime());
        cv.put(CREATED_BY, trainingRole.getCreatedBy());

        long id;
        if (isExist(trainingRole)){
            id = db.update(TABLE_NAME, cv, ID+"='"+ trainingRole.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;

    }
    public List<TrainingRole> getTrainingRoles() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        List<TrainingRole> trainingRoles =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            TrainingRole trainingRole =new TrainingRole();
            trainingRole.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            trainingRole.setRoleName(cursor.getString(cursor.getColumnIndex(ROLE_NAME)));
            trainingRole.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
            trainingRole.setReadOnly(cursor.getInt(cursor.getColumnIndex(READONLY))==1);
            trainingRole.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            trainingRole.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            trainingRole.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
            trainingRoles.add(trainingRole);
        }
        cursor.close();
        return trainingRoles;
    }

    public void fromJson(JSONObject jsonObject){
        try{
            TrainingRole trainingRole =new TrainingRole();
            trainingRole.setId(jsonObject.getInt(ID));
            trainingRole.setRoleName(jsonObject.getString(ROLE_NAME));
            trainingRole.setArchived(jsonObject.getInt(ARCHIVED)==1);
            trainingRole.setReadOnly(jsonObject.getInt(READONLY)==1);
            trainingRole.setCountry(jsonObject.getString(COUNTRY));
            trainingRole.setClientTime(jsonObject.getLong(CLIENT_TIME));
            trainingRole.setCreatedBy(jsonObject.getInt(CREATED_BY));
            this.addData(trainingRole);
        }catch (Exception e){}
    }

    public TrainingRole getTrainingRoleById(Integer id){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                id.toString(),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            TrainingRole trainingRole =new TrainingRole();
            trainingRole.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            trainingRole.setRoleName(cursor.getString(cursor.getColumnIndex(ROLE_NAME)));
            trainingRole.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
            trainingRole.setReadOnly(cursor.getInt(cursor.getColumnIndex(READONLY))==1);
            trainingRole.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            trainingRole.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            trainingRole.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
            return trainingRole;
        }

    }

    public boolean isExist(TrainingRole trainingRole) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + trainingRole.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public JSONObject getTrainingRoleJson() {
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
        return results;
    }

    private void upgradeVersion2(SQLiteDatabase db) {}
}

