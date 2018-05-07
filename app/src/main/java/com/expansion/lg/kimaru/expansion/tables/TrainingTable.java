package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Training;
import com.expansion.lg.kimaru.expansion.other.Constants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 9/4/17.
 */


public class TrainingTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="training";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;


    public static String JSON_ROOT = "trainings";

    public static final String ID = "id";
    public static final String TRAINNIG_NAME= "training_name";
    public static final String COUNTRY = "country";
    public static final String COUNTY = "county_id";
    public static final String SUBCOUNTY = "subcounty_id";
    public static final String WARD = "ward_id";
    public static final String RECRUITMENT = "recruitment_id";
    public static final String DISTRICT = "district";
    public static final String PARISH = "parish_id";
    public static final String LOCATION = "location_id";
    public static final String CREATED_BY = "created_by";
    public static final String STATUS = "status";
    public static final String CLIENT_TIME = "client_time";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String COMMENT = "comment";

    public String [] columns=new String[]{ID, TRAINNIG_NAME, COUNTRY, COUNTY, SUBCOUNTY, WARD, DISTRICT,
            PARISH, LOCATION, CREATED_BY, STATUS, CLIENT_TIME, LAT, LON, COMMENT, RECRUITMENT};
    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + Constants.varchar_field + ","
            + TRAINNIG_NAME + Constants.varchar_field + ", "
            + COUNTRY + Constants.varchar_field + ", "
            + COUNTY + Constants.varchar_field + ", "
            + SUBCOUNTY + Constants.varchar_field + ", "
            + WARD + Constants.varchar_field + ", "
            + DISTRICT + Constants.varchar_field + ", "
            + PARISH + Constants.varchar_field + ", "
            + LOCATION + Constants.varchar_field + ", "
            + CREATED_BY + Constants.integer_field + ", "
            + STATUS + Constants.integer_field + ", "
            + CLIENT_TIME + Constants.real_field + ", "
            + COMMENT + Constants.text_field + ", "
            + RECRUITMENT + Constants.varchar_field + ", "
            + LAT + Constants.real_field + ", "
            + LON + Constants.real_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public TrainingTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //prior to this, there are registrations that have been created using the old Scoring
        // We need to select all of them, and for each, we shall create the referrals and return the
        //IDs. Then we update the registration
        Log.w("referral", "upgrading database from" + oldVersion + "to" + newVersion);

        if (oldVersion < 2){
            upgradeVersion2(db);
        }
    }

    public long addTraining(Training training) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, training.getId());
        cv.put(TRAINNIG_NAME, training.getTrainingName());
        cv.put(COUNTRY, training.getCountry());
        cv.put(COUNTY, training.getCounty());
        cv.put(SUBCOUNTY, training.getSubCounty());
        cv.put(WARD, training.getWard());
        cv.put(DISTRICT, training.getDistrict());
        cv.put(PARISH, training.getParish());
        cv.put(LOCATION, training.getLocation());
        cv.put(RECRUITMENT, training.getRecruitment());
        cv.put(COMMENT, training.getComment());
        cv.put(CREATED_BY, training.getCreatedBy());
        cv.put(STATUS, training.getStatus());
        cv.put(CLIENT_TIME, training.getClientTime());
        cv.put(LAT, training.getLat());
        cv.put(LON, training.getLon());

        long id;
        if (isExist(training)){
            id = db.update(TABLE_NAME, cv, ID+"='"+training.getId()+"'", null);
            Log.d("Tremap DB Op", "Training updated");
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Tremap DB Op", "Training created");
        }
        db.close();
        return id;

    }
    public boolean isExist(Training training) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + training.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }


    public List<Training> getTrainingData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Training> trainingList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Training training = new Training();
            training.setId(cursor.getString(cursor.getColumnIndex(ID)));
            training.setTrainingName(cursor.getString(cursor.getColumnIndex(TRAINNIG_NAME)));
            training.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            training.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
            training.setSubCounty(cursor.getString(cursor.getColumnIndex(SUBCOUNTY)));
            training.setWard(cursor.getString(cursor.getColumnIndex(WARD)));
            training.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
            training.setParish(cursor.getString(cursor.getColumnIndex(PARISH)));
            training.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
            training.setRecruitment(cursor.getString(cursor.getColumnIndex(RECRUITMENT)));
            training.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            training.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
            training.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
            training.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            training.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
            training.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));

            trainingList.add(training);
        }
        db.close();
        return trainingList;
    }

    public List<Training> getTrainingByCountry (String countryCode){

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = COUNTRY+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(countryCode),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            List<Training> trainingList = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                Training training = new Training();
                training.setId(cursor.getString(cursor.getColumnIndex(ID)));
                training.setTrainingName(cursor.getString(cursor.getColumnIndex(TRAINNIG_NAME)));
                training.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
                training.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
                training.setSubCounty(cursor.getString(cursor.getColumnIndex(SUBCOUNTY)));
                training.setWard(cursor.getString(cursor.getColumnIndex(WARD)));
                training.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
                training.setParish(cursor.getString(cursor.getColumnIndex(PARISH)));
                training.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
                training.setRecruitment(cursor.getString(cursor.getColumnIndex(RECRUITMENT)));
                training.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
                training.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
                training.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                training.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
                training.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
                training.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));

                trainingList.add(training);
            }
            db.close();
            return trainingList;
        }

    }

    public Training getTrainingById (String id){

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            Training training = new Training();
            training.setId(cursor.getString(cursor.getColumnIndex(ID)));
            training.setTrainingName(cursor.getString(cursor.getColumnIndex(TRAINNIG_NAME)));
            training.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            training.setCounty(cursor.getString(cursor.getColumnIndex(COUNTY)));
            training.setSubCounty(cursor.getString(cursor.getColumnIndex(SUBCOUNTY)));
            training.setWard(cursor.getString(cursor.getColumnIndex(WARD)));
            training.setDistrict(cursor.getString(cursor.getColumnIndex(DISTRICT)));
            training.setParish(cursor.getString(cursor.getColumnIndex(PARISH)));
            training.setLocation(cursor.getString(cursor.getColumnIndex(LOCATION)));
            training.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
            training.setRecruitment(cursor.getString(cursor.getColumnIndex(RECRUITMENT)));
            training.setCreatedBy(cursor.getInt(cursor.getColumnIndex(CREATED_BY)));
            training.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
            training.setClientTime(cursor.getLong(cursor.getColumnIndex(CLIENT_TIME)));
            training.setLat(cursor.getDouble(cursor.getColumnIndex(LAT)));
            training.setLon(cursor.getDouble(cursor.getColumnIndex(LON)));

            db.close();

            /**
             * @TODO: Add all registrations referred by this user.
             * When one retrieves a referral, it would be good to get all the registrations that (s)he
             * has referred
             */
            return training;
        }

    }


    public Cursor getCursor() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
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
            Training training = new Training();
            training.setId(jsonObject.getString(ID));
            if (!jsonObject.isNull(TRAINNIG_NAME)){
                training.setTrainingName(jsonObject.getString(TRAINNIG_NAME));
            }
            if (!jsonObject.isNull(COUNTRY)){
                training.setCountry(jsonObject.getString(COUNTRY));
            }
            if (!jsonObject.isNull(COUNTY)){
                training.setCounty(jsonObject.getString(COUNTY));
            }
            if (!jsonObject.isNull(SUBCOUNTY)){
                training.setSubCounty(jsonObject.getString(SUBCOUNTY));
            }
            if (!jsonObject.isNull(WARD)){
                training.setWard(jsonObject.getString(WARD));
            }
            if (!jsonObject.isNull(DISTRICT)){
                training.setDistrict(jsonObject.getString(DISTRICT));
            }
            if (!jsonObject.isNull(PARISH)){
                training.setParish(jsonObject.getString(PARISH));
            }
            if (!jsonObject.isNull(LOCATION)){
                training.setLocation(jsonObject.getString(LOCATION));
            }
            if (!jsonObject.isNull(CREATED_BY)){
                training.setCreatedBy(jsonObject.getInt(CREATED_BY));
            }
            if (!jsonObject.isNull(STATUS)){
                training.setStatus(jsonObject.getInt(STATUS));
            }
            if (!jsonObject.isNull(RECRUITMENT)){
                training.setRecruitment(jsonObject.getString(RECRUITMENT));
            }
            if (!jsonObject.isNull(COMMENT)){
                training.setComment(jsonObject.getString(COMMENT));
            }
            if (!jsonObject.isNull(CLIENT_TIME)){
                training.setClientTime(jsonObject.getLong(CLIENT_TIME));
            }
            if (!jsonObject.isNull(LAT)){
                training.setLat(jsonObject.getDouble(LAT));
            }
            if (!jsonObject.isNull(LON)){
                training.setLon(jsonObject.getDouble(LON));
            }
            addTraining(training);
        }catch (Exception e){
            Log.d("Tremap Training ERR", "From Json : "+e.getMessage());
        }
    }

    private void upgradeVersion2(SQLiteDatabase db) {}
}


