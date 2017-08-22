package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.ChewReferral;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 5/12/17.
 */



public class ChewReferralTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="chew_referral";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    //String email, username, password, name;

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";


    public static String JSON_ROOT = "chew_referrals";

    public static final String ID = "id";
    public static final String PHONE= "phone";
    public static final String TITLE = "title";
    public static final String RECRUITMENT = "recruitment";
    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    public static final String COUNTY = "county";
    public static final String DISTRICT = "district";
    public static final String SUBCOUNTY = "subcounty";
    public static final String COMMUNITY_UNIT = "community_unit";
    public static final String VILLAGE = "village";
    public static final String MAPPING = "mapping";
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String MOBILIZATION = "mobilization";
    public static final String SYNCED = "synced";
    public String [] columns=new String[]{ID, NAME, PHONE, TITLE, COUNTRY, RECRUITMENT, SYNCED,
            COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON};
    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ","
            + NAME + varchar_field + ", "
            + PHONE + varchar_field + ", "
            + TITLE + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + SYNCED + integer_field + ", "
            + COUNTY + varchar_field + ", "
            + DISTRICT + varchar_field + ", "
            + SUBCOUNTY + varchar_field + ", "
            + COMMUNITY_UNIT + varchar_field + ", "
            + VILLAGE + varchar_field + ", "
            + MAPPING + varchar_field + ", "
            + MOBILIZATION + varchar_field + ", "
            + LAT + varchar_field + ", "
            + LON + varchar_field + ", "
            + RECRUITMENT + varchar_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public ChewReferralTable(Context context) {
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

    public long addChewReferral(ChewReferral chewReferral) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, chewReferral.getId());
        cv.put(NAME, chewReferral.getName());
        cv.put(PHONE, chewReferral.getPhone());
        cv.put(TITLE, chewReferral.getTitle());
        cv.put(COUNTRY, chewReferral.getCountry());
        cv.put(RECRUITMENT, chewReferral.getRecruitmentId());
        cv.put(SYNCED, chewReferral.getSynced());
        cv.put(COUNTY, chewReferral.getCounty());
        cv.put(DISTRICT, chewReferral.getDistrict());
        cv.put(SUBCOUNTY, chewReferral.getSubCounty());
        cv.put(COMMUNITY_UNIT, chewReferral.getCommunityUnit());
        cv.put(VILLAGE, chewReferral.getVillage());
        cv.put(MAPPING, chewReferral.getMapping());
        cv.put(MOBILIZATION, chewReferral.getMobilization());
        cv.put(LAT, chewReferral.getLat());
        cv.put(LON, chewReferral.getLon());
        //COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON
        long id;
        if (isExist(chewReferral)){
            cv.put(SYNCED, 0);
            id = db.update(TABLE_NAME, cv, ID+"='"+chewReferral.getId()+"'", null);
            Log.d("Tremap DB Op", "CHEW Referral updated");
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("Tremap DB Op", "CHEW Referral created");
        }
        db.close();
        return id;

    }
    public boolean isExist(ChewReferral chewReferral) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + chewReferral.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public List<ChewReferral> getChewReferralByRecruitmentId(String recruitmentId) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = RECRUITMENT+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(recruitmentId),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<ChewReferral> chewReferralList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            ChewReferral chewReferral = new ChewReferral();

            chewReferral.setId(cursor.getString(0));
            chewReferral.setName(cursor.getString(1));
            chewReferral.setPhone(cursor.getString(2));
            chewReferral.setTitle(cursor.getString(3));
            chewReferral.setCountry(cursor.getString(4));
            chewReferral.setRecruitmentId(cursor.getString(5));
            chewReferral.setSynced(cursor.getInt(6));
            //COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON
            chewReferral.setCounty(cursor.getString(7));
            chewReferral.setDistrict(cursor.getString(8));
            chewReferral.setSubCounty(cursor.getString(9));
            chewReferral.setCommunityUnit(cursor.getString(10));
            chewReferral.setVillage(cursor.getString(11));
            chewReferral.setMapping(cursor.getString(12));
            chewReferral.setMobilization(cursor.getString(13));
            chewReferral.setLat(cursor.getString(14));
            chewReferral.setLon(cursor.getString(15));

            chewReferralList.add(chewReferral);
        }
        db.close();
        return chewReferralList;
    }

    public List<ChewReferral> getChewReferralByMappingId(String mappingId) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = MAPPING+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(mappingId),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<ChewReferral> chewReferralList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            ChewReferral chewReferral = new ChewReferral();

            chewReferral.setId(cursor.getString(0));
            chewReferral.setName(cursor.getString(1));
            chewReferral.setPhone(cursor.getString(2));
            chewReferral.setTitle(cursor.getString(3));
            chewReferral.setCountry(cursor.getString(4));
            chewReferral.setRecruitmentId(cursor.getString(5));
            chewReferral.setSynced(cursor.getInt(6));
            //COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON
            chewReferral.setCounty(cursor.getString(7));
            chewReferral.setDistrict(cursor.getString(8));
            chewReferral.setSubCounty(cursor.getString(9));
            chewReferral.setCommunityUnit(cursor.getString(10));
            chewReferral.setVillage(cursor.getString(11));
            chewReferral.setMapping(cursor.getString(12));
            chewReferral.setMobilization(cursor.getString(13));
            chewReferral.setLat(cursor.getString(14));
            chewReferral.setLon(cursor.getString(15));

            chewReferralList.add(chewReferral);
        }
        db.close();
        return chewReferralList;
    }

    public List<ChewReferral> getChewReferralBySubCountyId(String subCounty) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = SUBCOUNTY+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(subCounty),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<ChewReferral> chewReferralList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            ChewReferral chewReferral = new ChewReferral();

            chewReferral.setId(cursor.getString(0));
            chewReferral.setName(cursor.getString(1));
            chewReferral.setPhone(cursor.getString(2));
            chewReferral.setTitle(cursor.getString(3));
            chewReferral.setCountry(cursor.getString(4));
            chewReferral.setRecruitmentId(cursor.getString(5));
            chewReferral.setSynced(cursor.getInt(6));
            //COUNTY, DISTRICT, SUBCOUNTY, COMMUNITY_UNIT, VILLAGE, MAPPING, MOBILIZATION, LAT, LON
            chewReferral.setCounty(cursor.getString(7));
            chewReferral.setDistrict(cursor.getString(8));
            chewReferral.setSubCounty(cursor.getString(9));
            chewReferral.setCommunityUnit(cursor.getString(10));
            chewReferral.setVillage(cursor.getString(11));
            chewReferral.setMapping(cursor.getString(12));
            chewReferral.setMobilization(cursor.getString(13));
            chewReferral.setLat(cursor.getString(14));
            chewReferral.setLon(cursor.getString(15));

            chewReferralList.add(chewReferral);
        }
        db.close();
        return chewReferralList;
    }

    public List<ChewReferral> getChewReferralByPhone(String phone) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = PHONE+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(phone),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        List<ChewReferral> chewReferralList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            ChewReferral chewReferral = new ChewReferral();

            chewReferral.setId(cursor.getString(0));
            chewReferral.setName(cursor.getString(1));
            chewReferral.setPhone(cursor.getString(2));
            chewReferral.setTitle(cursor.getString(3));
            chewReferral.setCountry(cursor.getString(4));
            chewReferral.setRecruitmentId(cursor.getString(5));
            chewReferral.setSynced(cursor.getInt(6));
            chewReferral.setCounty(cursor.getString(7));
            chewReferral.setDistrict(cursor.getString(8));
            chewReferral.setSubCounty(cursor.getString(9));
            chewReferral.setCommunityUnit(cursor.getString(10));
            chewReferral.setVillage(cursor.getString(11));
            chewReferral.setMapping(cursor.getString(12));
            chewReferral.setMobilization(cursor.getString(13));
            chewReferral.setLat(cursor.getString(14));
            chewReferral.setLon(cursor.getString(15));

            chewReferralList.add(chewReferral);
        }
        db.close();
        return chewReferralList;
    }

    public List<ChewReferral> getChewReferralData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<ChewReferral> chewReferralList = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            ChewReferral chewReferral = new ChewReferral();

            chewReferral.setId(cursor.getString(0));
            chewReferral.setName(cursor.getString(1));
            chewReferral.setPhone(cursor.getString(2));
            chewReferral.setTitle(cursor.getString(3));
            chewReferral.setCountry(cursor.getString(4));
            chewReferral.setRecruitmentId(cursor.getString(5));
            chewReferral.setSynced(cursor.getInt(6));
            chewReferral.setCounty(cursor.getString(7));
            chewReferral.setDistrict(cursor.getString(8));
            chewReferral.setSubCounty(cursor.getString(9));
            chewReferral.setCommunityUnit(cursor.getString(10));
            chewReferral.setVillage(cursor.getString(11));
            chewReferral.setMapping(cursor.getString(12));
            chewReferral.setMobilization(cursor.getString(13));
            chewReferral.setLat(cursor.getString(14));
            chewReferral.setLon(cursor.getString(15));

            chewReferralList.add(chewReferral);
        }
        db.close();
        return chewReferralList;
    }

    public ChewReferral getChewReferralById (String id){

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            ChewReferral chewReferral = new ChewReferral();
            chewReferral.setId(cursor.getString(0));
            chewReferral.setName(cursor.getString(1));
            chewReferral.setPhone(cursor.getString(2));
            chewReferral.setTitle(cursor.getString(3));
            chewReferral.setCountry(cursor.getString(4));
            chewReferral.setRecruitmentId(cursor.getString(5));
            chewReferral.setSynced(cursor.getInt(6));
            chewReferral.setCounty(cursor.getString(7));
            chewReferral.setDistrict(cursor.getString(8));
            chewReferral.setSubCounty(cursor.getString(9));
            chewReferral.setCommunityUnit(cursor.getString(10));
            chewReferral.setVillage(cursor.getString(11));
            chewReferral.setMapping(cursor.getString(12));
            chewReferral.setMobilization(cursor.getString(13));
            chewReferral.setLat(cursor.getString(14));
            chewReferral.setLon(cursor.getString(15));

            db.close();

            /**
             * @TODO: Add all registrations referred by this user.
             * When one retrieves a referral, it would be good to get all the registrations that (s)he
             * has referred
             */
            return chewReferral;
        }

    }


    public Cursor getChewReferralCursor() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return cursor;
    }

    public JSONObject getChewReferralJson() {

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
        ChewReferral chewReferral = new ChewReferral();
        try {

            chewReferral.setId(jsonObject.getString(ID));
            chewReferral.setName(jsonObject.getString(NAME));
            chewReferral.setPhone(jsonObject.getString(PHONE));
            chewReferral.setTitle(jsonObject.getString(TITLE));
            chewReferral.setCountry(jsonObject.getString(COUNTRY));
            chewReferral.setRecruitmentId(jsonObject.getString(RECRUITMENT));
            chewReferral.setSynced(jsonObject.getInt(SYNCED));
            chewReferral.setCounty(jsonObject.getString(COUNTY));
            chewReferral.setDistrict(jsonObject.getString(DISTRICT));
            chewReferral.setSubCounty(jsonObject.getString(SUBCOUNTY));
            chewReferral.setCommunityUnit(jsonObject.getString(COMMUNITY_UNIT));
            chewReferral.setVillage(jsonObject.getString(VILLAGE));
            chewReferral.setMapping(jsonObject.getString(MAPPING));
            chewReferral.setMobilization(jsonObject.getString(MOBILIZATION));
            chewReferral.setLat(jsonObject.getString(LAT));
            chewReferral.setLon(jsonObject.getString(LON));

            addChewReferral(chewReferral);
        }catch (Exception e){
            Log.d("Tremap ChewReferral ERR", "From Json : "+e.getMessage());
        }
    }

    public void deleteChewReferral (ChewReferral chewReferral){
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[] { chewReferral.getId() });
    }
    private void upgradeVersion2(SQLiteDatabase db) {}
}

