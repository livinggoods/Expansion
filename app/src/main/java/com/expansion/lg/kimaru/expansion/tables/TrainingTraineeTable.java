package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainee;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 9/4/17.
 */


public class TrainingTraineeTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="training_trainees";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String JSON_ROOT = "training_trainees";


    public static final String ID = "id";
    public static final String REGISTRATION_ID = "registration_id";
    public static final String CLASS_ID = "class_id";
    public static final String TRAINING_ID = "training_id";
    public static final String COUNTRY = "country";
    public static final String ADDED_BY = "added_by";
    public static final String CLIENT_TIME = "client_time";
    public static final String BRANCH = "branch";
    public static final String COHORT = "cohort";
    public static final String CHP_CODE = "chp_code";

    public String [] columns=new String[]{ID, REGISTRATION_ID, CLASS_ID, TRAINING_ID, COUNTRY,
            ADDED_BY, CLIENT_TIME, BRANCH, COHORT, CHP_CODE};
    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + Constants.varchar_field + ","
            + REGISTRATION_ID + Constants.varchar_field + ", "
            + CLASS_ID + Constants.integer_field + ", "
            + TRAINING_ID + Constants.varchar_field + ", "
            + COUNTRY + Constants.varchar_field + ", "
            + ADDED_BY + Constants.integer_field + ", "
            + CLIENT_TIME + Constants.real_field + ", "
            + BRANCH + Constants.varchar_field + ", "
            + COHORT + Constants.integer_field + ", "
            + CHP_CODE + Constants.varchar_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public TrainingTraineeTable(Context context) {
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

    public long addTrainingTraineer(TrainingTrainee trainingTrainee) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, trainingTrainee.getId());
        cv.put(REGISTRATION_ID, trainingTrainee.getRegistrationId());
        cv.put(CLASS_ID, trainingTrainee.getClassId());
        cv.put(TRAINING_ID, trainingTrainee.getTrainingId());
        cv.put(COUNTRY, trainingTrainee.getCountry());
        cv.put(ADDED_BY, trainingTrainee.getAddedBy());
        cv.put(CLIENT_TIME, trainingTrainee.getClientTime());
        cv.put(BRANCH, trainingTrainee.getBranch());
        cv.put(COHORT, trainingTrainee.getCohort());
        cv.put(CHP_CODE, trainingTrainee.getChpCode());
        long id;
        if (isExist(trainingTrainee)){
            id = db.update(TABLE_NAME, cv, ID+"='"+trainingTrainee.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;

    }
    public boolean isExist(TrainingTrainee trainingTrainee) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + trainingTrainee.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }


    public List<TrainingTrainee> getTrainingTraineeData() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        List<TrainingTrainee> trainingTrainees = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            TrainingTrainee trainingTrainee = new TrainingTrainee();
            trainingTrainee.setId(cursor.getString(cursor.getColumnIndex(ID)));
            trainingTrainee.setRegistrationId(cursor.getString(cursor.getColumnIndex(REGISTRATION_ID)));
            trainingTrainee.setClassId(cursor.getInt(cursor.getColumnIndex(CLASS_ID)));
            trainingTrainee.setTrainingId(cursor.getString(cursor.getColumnIndex(TRAINING_ID)));
            trainingTrainee.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            trainingTrainee.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDED_BY)));
            trainingTrainee.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            trainingTrainee.setBranch(cursor.getInt(cursor.getColumnIndex(BRANCH)));
            trainingTrainee.setChpCode(cursor.getString(cursor.getColumnIndex(CHP_CODE)));
            trainingTrainees.add(trainingTrainee);
        }
        db.close();
        return trainingTrainees;
    }

    public List<TrainingTrainee> getTrainingByTraining (String trainingId){

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = TRAINING_ID+" = ?";
        String[] whereArgs = new String[] {
                trainingId,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            List<TrainingTrainee> trainingList = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                TrainingTrainee trainingTrainee = new TrainingTrainee();
                trainingTrainee.setId(cursor.getString(cursor.getColumnIndex(ID)));
                trainingTrainee.setRegistrationId(cursor.getString(cursor.getColumnIndex(REGISTRATION_ID)));
                trainingTrainee.setClassId(cursor.getInt(cursor.getColumnIndex(CLASS_ID)));
                trainingTrainee.setTrainingId(cursor.getString(cursor.getColumnIndex(TRAINING_ID)));
                trainingTrainee.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
                trainingTrainee.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDED_BY)));
                trainingTrainee.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
                trainingTrainee.setBranch(cursor.getInt(cursor.getColumnIndex(BRANCH)));
                trainingTrainee.setChpCode(cursor.getString(cursor.getColumnIndex(CHP_CODE)));

                trainingList.add(trainingTrainee);
            }
            db.close();
            return trainingList;
        }

    }

    public TrainingTrainee getTrainingTraineeById (String id){

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            TrainingTrainee trainingTrainee = new TrainingTrainee();
            trainingTrainee.setId(cursor.getString(cursor.getColumnIndex(ID)));
            trainingTrainee.setRegistrationId(cursor.getString(cursor.getColumnIndex(REGISTRATION_ID)));
            trainingTrainee.setClassId(cursor.getInt(cursor.getColumnIndex(CLASS_ID)));
            trainingTrainee.setTrainingId(cursor.getString(cursor.getColumnIndex(TRAINING_ID)));
            trainingTrainee.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            trainingTrainee.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDED_BY)));
            trainingTrainee.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            trainingTrainee.setBranch(cursor.getInt(cursor.getColumnIndex(BRANCH)));
            trainingTrainee.setChpCode(cursor.getString(cursor.getColumnIndex(CHP_CODE)));

            db.close();

            /**
             * @TODO: Add all registrations referred by this user.
             * When one retrieves a referral, it would be good to get all the registrations that (s)he
             * has referred
             */
            return trainingTrainee;
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
            TrainingTrainee trainingTrainee = new TrainingTrainee();
            trainingTrainee.setId(jsonObject.getString(ID));
            if (!jsonObject.isNull(REGISTRATION_ID)){
                trainingTrainee.setRegistrationId(jsonObject.getString(REGISTRATION_ID));
            }
            if (!jsonObject.isNull(CLASS_ID)){
                trainingTrainee.setClassId(jsonObject.getInt(CLASS_ID));
            }
            if (!jsonObject.isNull(TRAINING_ID)){
                trainingTrainee.setTrainingId(jsonObject.getString(TRAINING_ID));
            }
            if (!jsonObject.isNull(COUNTRY)){
                trainingTrainee.setCountry(jsonObject.getString(COUNTRY));
            }
            if (!jsonObject.isNull(ADDED_BY)){
                trainingTrainee.setAddedBy(jsonObject.getInt(ADDED_BY));
            }
            if (!jsonObject.isNull(CLIENT_TIME)){
                trainingTrainee.setClientTime(jsonObject.getLong(CLIENT_TIME));
            }
            if (!jsonObject.isNull(BRANCH)){
                trainingTrainee.setBranch(jsonObject.getInt(BRANCH));
            }
            if (!jsonObject.isNull(COHORT)){
                trainingTrainee.setCohort(jsonObject.getInt(COHORT));
            }
            if (!jsonObject.isNull(CHP_CODE)){
                trainingTrainee.setChpCode(jsonObject.getString(CHP_CODE));
            }
            addTrainingTraineer(trainingTrainee);
        }catch (Exception e){
            Log.d("Tremap Trainee ERR", "From Json : "+e.getMessage());
        }
    }

    private void upgradeVersion2(SQLiteDatabase db) {}
}


