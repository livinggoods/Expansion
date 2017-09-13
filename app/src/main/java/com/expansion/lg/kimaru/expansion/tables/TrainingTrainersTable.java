package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.TrainingTrainer;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class TrainingTrainersTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="training_trainers";
    public static final String JSON_ROOT="training_trainers";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    Context context;


    public static final String ID ="id";
    public static final String TRAINING_ID ="training_id";
    public static final String CLASS_ID ="class_id";
    public static final String TRAINER_ID ="trainer_id";
    public static final String COUNTRY ="country";
    public static final String CLIENT_TIME ="client_time";
    public static final String CREATED_BY ="created_by";
    public static final String ARCHIVED ="archived";
    public static final String TRAINING_ROLE_ID ="training_role_id";


    String [] columns=new String[]{ID, TRAINING_ID, CLASS_ID, TRAINER_ID, COUNTRY,
            CLIENT_TIME, CREATED_BY, ARCHIVED, TRAINING_ROLE_ID};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + Constants.integer_field + ", "
            + TRAINING_ID + Constants.varchar_field + ", "
            + CLASS_ID + Constants.integer_field + ", "
            + TRAINER_ID + Constants.integer_field + ", "
            + COUNTRY + Constants.varchar_field + ", "
            + CLIENT_TIME + Constants.real_field + ", "
            + CREATED_BY + Constants.integer_field + ", "
            + ARCHIVED + Constants.integer_field + ", "
            + TRAINING_ROLE_ID + Constants.integer_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public TrainingTrainersTable(Context context) {
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

    public long addData(TrainingTrainer trainingTrainer) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, trainingTrainer.getId());
        cv.put(TRAINING_ID, trainingTrainer.getTrainingId());
        cv.put(CLASS_ID, trainingTrainer.getClassId());
        cv.put(TRAINER_ID, trainingTrainer.getTrainerId());
        cv.put(COUNTRY, trainingTrainer.getCountry());
        cv.put(CLIENT_TIME, trainingTrainer.getClientTime());
        cv.put(CREATED_BY, trainingTrainer.getCreatedby());
        cv.put(ARCHIVED, trainingTrainer.isArchived() ? 1:0);
        cv.put(TRAINING_ROLE_ID, trainingTrainer.getTrainingRoleId());

        long id;
        if (isExist(trainingTrainer)){
            id = db.update(TABLE_NAME, cv, ID+"='"+ trainingTrainer.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;

    }
    public List<TrainingTrainer> getTrainers() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        List<TrainingTrainer> trainingTrainerList =new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

            TrainingTrainer trainingTrainer =new TrainingTrainer();

            trainingTrainer.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            trainingTrainer.setTrainingId(cursor.getString(cursor.getColumnIndex(TRAINING_ID)));
            trainingTrainer.setClassId(cursor.getString(cursor.getColumnIndex(CLASS_ID)));
            trainingTrainer.setTrainerId(cursor.getInt(cursor.getColumnIndex(TRAINER_ID)));
            trainingTrainer.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            trainingTrainer.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            trainingTrainer.setCreatedby(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
            trainingTrainer.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
            trainingTrainer.setTrainingRoleId(cursor.getInt(cursor.getColumnIndex(TRAINING_ROLE_ID)));
            trainingTrainerList.add(trainingTrainer);
        }
        cursor.close();
        return trainingTrainerList;
    }

    public List<TrainingTrainer> getTrainingTrainersByTrainingId(String trainingId){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = TRAINING_ID+" = ?";
        String[] whereArgs = new String[] {
                trainingId,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            List<TrainingTrainer> trainingTrainerList =new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                TrainingTrainer trainingTrainer =new TrainingTrainer();
                trainingTrainer.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                trainingTrainer.setTrainingId(cursor.getString(cursor.getColumnIndex(TRAINING_ID)));
                trainingTrainer.setClassId(cursor.getString(cursor.getColumnIndex(CLASS_ID)));
                trainingTrainer.setTrainerId(cursor.getInt(cursor.getColumnIndex(TRAINER_ID)));
                trainingTrainer.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
                trainingTrainer.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
                trainingTrainer.setCreatedby(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
                trainingTrainer.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
                trainingTrainer.setTrainingRoleId(cursor.getInt(cursor.getColumnIndex(TRAINING_ROLE_ID)));
                trainingTrainerList.add(trainingTrainer);
            }
            cursor.close();
            return trainingTrainerList;
        }

    }

    public void fromJson(JSONObject jsonObject){
        try{
            TrainingTrainer trainingTrainer =new TrainingTrainer();
            trainingTrainer.setId(jsonObject.getInt(ID));
            trainingTrainer.setTrainingId(jsonObject.getString(TRAINER_ID));
            trainingTrainer.setClassId(jsonObject.getString(CLASS_ID));
            trainingTrainer.setTrainerId(jsonObject.getInt(TRAINER_ID));
            trainingTrainer.setCountry(jsonObject.getString(COUNTRY));
            trainingTrainer.setClientTime(jsonObject.getLong(CLIENT_TIME));
            trainingTrainer.setCreatedby(jsonObject.getInt(CREATED_BY));
            trainingTrainer.setArchived(jsonObject.getInt(ARCHIVED)==1);
            trainingTrainer.setTrainingRoleId(jsonObject.getInt(TRAINING_ROLE_ID));;
            this.addData(trainingTrainer);
        }catch (Exception e){}
    }

    public TrainingTrainer getTrainingTrainerById(Integer id){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                id.toString(),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            TrainingTrainer trainingTrainer=new TrainingTrainer();
            trainingTrainer.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            trainingTrainer.setTrainingId(cursor.getString(cursor.getColumnIndex(TRAINING_ID)));
            trainingTrainer.setClassId(cursor.getString(cursor.getColumnIndex(CLASS_ID)));
            trainingTrainer.setTrainerId(cursor.getInt(cursor.getColumnIndex(TRAINER_ID)));
            trainingTrainer.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            trainingTrainer.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            trainingTrainer.setCreatedby(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
            trainingTrainer.setArchived(cursor.getInt(cursor.getColumnIndex(ARCHIVED))==1);
            trainingTrainer.setTrainingRoleId(cursor.getInt(cursor.getColumnIndex(TRAINING_ROLE_ID)));
            return trainingTrainer;
        }

    }

    public boolean isExist(TrainingTrainer trainingTrainer) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + trainingTrainer.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public JSONObject getTrainingTrainerJson() {
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

