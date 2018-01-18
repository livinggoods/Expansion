package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.TrainingClass;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 9/4/17.
 */


public class TrainingClassTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="training_class";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String JSON_ROOT = "training_classes";


    public static final String ID = "id";
    public static final String TRAINING_ID = "training_id";
    public static final String CLASS_NAME = "class_name";
    public static final String CREATED_BY = "created_by";
    public static final String CLIENT_TIME = "client_time";
    public static final String ARCHIVED = "archived";
    public static final String COUNTRY = "country";

    public String [] columns=new String[]{ID, TRAINING_ID, CLASS_NAME, CREATED_BY, CLIENT_TIME,
            ARCHIVED, COUNTRY};
    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + Constants.integer_field + ","
            + TRAINING_ID + Constants.varchar_field + ", "
            + CLASS_NAME + Constants.varchar_field + ", "
            + CREATED_BY + Constants.integer_field + ", "
            + CLIENT_TIME + Constants.real_field + ", "
            + ARCHIVED + Constants.integer_field + ", "
            + COUNTRY + Constants.varchar_field + ");";


    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public TrainingClassTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("referral", "upgrading database from" + oldVersion + "to" + newVersion);

        if (oldVersion < 2){
            upgradeVersion2(db);
        }
    }

    public long addTrainingClass(TrainingClass trainingClass) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, trainingClass.getId());
        cv.put(CLASS_NAME, trainingClass.getClassName());
        cv.put(TRAINING_ID, trainingClass.getTrainingId());
        cv.put(CREATED_BY, trainingClass.getCreatedBy());
        cv.put(CLIENT_TIME, trainingClass.getClientTime());
        cv.put(COUNTRY, trainingClass.getCountry());
        cv.put(ARCHIVED, trainingClass.isArchived() ? 1:0);

        long id;
        if (isExist(trainingClass)){
            id = db.update(TABLE_NAME, cv, ID+"='"+trainingClass.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;

    }
    public boolean isExist(TrainingClass trainingClass) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + trainingClass.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }


    public List<TrainingClass> getTrainingClasses() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        List<TrainingClass> trainingClasses = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            TrainingClass trainingClass = new TrainingClass();
            trainingClass.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            trainingClass.setClassName(cursor.getString(cursor.getColumnIndex(CLASS_NAME)));
            trainingClass.setTrainingId(cursor.getString(cursor.getColumnIndex(TRAINING_ID)));
            trainingClass.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
            trainingClass.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            trainingClass.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            trainingClass.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
            trainingClasses.add(trainingClass);
        }
        db.close();
        return trainingClasses;
    }

    public List<TrainingClass> getTrainingClassesByTraining (String trainingId){

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = TRAINING_ID+" = ?";
        String[] whereArgs = new String[] {
                trainingId,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            List<TrainingClass> trainingClasses = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                TrainingClass trainingClass = new TrainingClass();
                trainingClass.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                trainingClass.setClassName(cursor.getString(cursor.getColumnIndex(CLASS_NAME)));
                trainingClass.setTrainingId(cursor.getString(cursor.getColumnIndex(TRAINING_ID)));
                trainingClass.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
                trainingClass.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
                trainingClass.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
                trainingClass.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
                trainingClasses.add(trainingClass);
            }
            db.close();
            return trainingClasses;
        }

    }

    public TrainingClass getTrainingTraineeById (String id){

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            TrainingClass trainingClass = new TrainingClass();
            trainingClass.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            trainingClass.setClassName(cursor.getString(cursor.getColumnIndex(CLASS_NAME)));
            trainingClass.setTrainingId(cursor.getString(cursor.getColumnIndex(TRAINING_ID)));
            trainingClass.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
            trainingClass.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            trainingClass.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            trainingClass.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
            db.close();

            return trainingClass;
        }

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
        try {
            TrainingClass trainingClass = new TrainingClass();
            trainingClass.setId(jsonObject.getInt(ID));
            if (!jsonObject.isNull(CLASS_NAME)){
                trainingClass.setClassName(jsonObject.getString(CLASS_NAME));
            }
            if (!jsonObject.isNull(TRAINING_ID)){
                trainingClass.setTrainingId(jsonObject.getString(TRAINING_ID));
            }
            if (!jsonObject.isNull(CREATED_BY)){
                trainingClass.setCreatedBy(jsonObject.getInt(CREATED_BY));
            }
            if (!jsonObject.isNull(CLIENT_TIME)){
                trainingClass.setClientTime(jsonObject.getLong(CLIENT_TIME));
            }
            if (!jsonObject.isNull(COUNTRY)){
                trainingClass.setCountry(jsonObject.getString(COUNTRY));
            }
            if (!jsonObject.isNull(ARCHIVED)){
                trainingClass.setArchived(jsonObject.getInt(ARCHIVED)==1);
            }

            addTrainingClass(trainingClass);
        }catch (Exception e){
            Log.d("Tremap ERR", "Training class From Json : "+e.getMessage());
        }
    }

    private void upgradeVersion2(SQLiteDatabase db) {}
}


