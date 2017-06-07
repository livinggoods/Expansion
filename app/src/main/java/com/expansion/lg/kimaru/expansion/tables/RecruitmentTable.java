package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.mzigos.CountyLocation;
import com.expansion.lg.kimaru.expansion.mzigos.KeCounty;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.SubCounty;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by kimaru on 2/28/17.
 */


public class RecruitmentTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="recruitment";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final String JSON_ROOT="recruitments";
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String NAME= "name";
    public static final String LON = "lon";
    public static final String LAT = "lat";
    public static final String DISTRICT = "district";
    public static final String SUB_COUNTY = "subcounty";
    public static final String COUNTY = "county";
    public static final String DIVISION = "division";
    public static final String COUNTRY = "country";
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String DATE_ADDED = "client_time";
    public static final String SYNCED = "synced";

    Context context;

    String [] columns=new String[]{ID, NAME, DISTRICT, SUB_COUNTY, DIVISION, LAT, LON, ADDED_BY,
            COMMENT, DATE_ADDED, SYNCED, COUNTRY, COUNTY};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + NAME + varchar_field + ", "
            + LAT + varchar_field + ", "
            + LON + varchar_field + ", "
            + DISTRICT + varchar_field + ", "
            + SUB_COUNTY + varchar_field + ", "
            + COUNTY + varchar_field + ", "
            + DIVISION + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + ADDED_BY + integer_field + ", "
            + COMMENT + text_field + ", "
            + DATE_ADDED + integer_field + ", "
            + SYNCED + integer_field + ");";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public RecruitmentTable(Context context) {
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

    public long addData(Recruitment recruitment) {

        SQLiteDatabase db=getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(ID, recruitment.getId());
        cv.put(NAME, recruitment.getName());
        cv.put(DISTRICT, recruitment.getDistrict());
        cv.put(LAT, recruitment.getLat());
        cv.put(LON, recruitment.getLon());
        cv.put(SUB_COUNTY, recruitment.getSubcounty());
        cv.put(COUNTY, recruitment.getCounty());
        cv.put(COUNTRY, recruitment.getCountry());
        cv.put(DIVISION, recruitment.getDivision());
        cv.put(ADDED_BY, recruitment.getAddedBy());
        cv.put(COMMENT, recruitment.getComment());
        cv.put(DATE_ADDED, recruitment.getDateAdded());
        cv.put(SYNCED, recruitment.getSynced());
        long id;
        if (isExist(recruitment)){
            //uupdate
            cv.put(SYNCED, 0);
            id = db.update(TABLE_NAME, cv, ID+"='"+recruitment.getId()+"'", null);
        }else{
            //create new
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;

    }
    public boolean isExist(Recruitment recruitment) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + TABLE_NAME + " WHERE " + ID + " = '" + recruitment.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }


    public long getRecruitmentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return cnt;
    }

    public List<Recruitment> getRecruitmentData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        List<Recruitment> recruitmentList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Recruitment recruitment=new Recruitment();

            recruitment.setId(cursor.getString(0));
            recruitment.setName(cursor.getString(1));
            recruitment.setDistrict(cursor.getString(2));
            recruitment.setSubcounty(cursor.getString(3));
            recruitment.setDivision(cursor.getString(4));
            recruitment.setLat(cursor.getString(5));
            recruitment.setLon(cursor.getString(6));
            recruitment.setAddedBy(cursor.getInt(7));
            recruitment.setComment(cursor.getString(8));
            recruitment.setDateAdded(cursor.getLong(9));
            recruitment.setSynced(cursor.getInt(10));
            recruitment.setCountry(cursor.getString(11));
            recruitment.setCounty(cursor.getString(12));

            recruitmentList.add(recruitment);
        }

        db.close();

        return recruitmentList;
    }

    public List<Recruitment> getRecruitmentDataByCountryCode(String country) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED +" desc";
        String whereClause = COUNTRY+" = ?";
        String[] whereArgs = new String[] {
                country,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);
        List<Recruitment> recruitmentList=new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Recruitment recruitment=new Recruitment();

            recruitment.setId(cursor.getString(0));
            recruitment.setName(cursor.getString(1));
            recruitment.setDistrict(cursor.getString(2));
            recruitment.setSubcounty(cursor.getString(3));
            recruitment.setDivision(cursor.getString(4));
            recruitment.setLat(cursor.getString(5));
            recruitment.setLon(cursor.getString(6));
            recruitment.setAddedBy(cursor.getInt(7));
            recruitment.setComment(cursor.getString(8));
            recruitment.setDateAdded(cursor.getLong(9));
            recruitment.setSynced(cursor.getInt(10));
            recruitment.setCountry(cursor.getString(11));
            recruitment.setCounty(cursor.getString(12));

            recruitmentList.add(recruitment);
        }

        db.close();

        return recruitmentList;
    }

    public void fromJson(JSONObject jsonObject){
        try{
            Recruitment recruitment = new Recruitment();
            recruitment.setId(jsonObject.getString(RecruitmentTable.ID));
            recruitment.setName(jsonObject.getString(RecruitmentTable.NAME));
            recruitment.setDistrict(jsonObject.getString(RecruitmentTable.DISTRICT));
            recruitment.setSubcounty(jsonObject.getString(RecruitmentTable.SUB_COUNTY));
            recruitment.setDivision(jsonObject.getString(RecruitmentTable.DIVISION));
            recruitment.setCountry(jsonObject.getString(RecruitmentTable.COUNTRY));
            recruitment.setLat(jsonObject.getString(RecruitmentTable.LAT));
            recruitment.setLon(jsonObject.getString(RecruitmentTable.LON));
            recruitment.setAddedBy(jsonObject.getInt(RecruitmentTable.ADDED_BY));
            recruitment.setComment(jsonObject.getString(RecruitmentTable.COMMENT));
            recruitment.setDateAdded(jsonObject.getLong(RecruitmentTable.DATE_ADDED));
            recruitment.setSynced(jsonObject.getInt(RecruitmentTable.SYNCED));
            recruitment.setCounty(jsonObject.getString(RecruitmentTable.COUNTY));
            this.addData(recruitment);
        } catch (Exception e){}
    }

    public List<Recruitment> getRecruitmentbyQuery(String whereClause){

        List<Recruitment> recruitments = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String queryString = "SELECT * FROM  " + TABLE_NAME + " WHERE " + whereClause;
        Cursor cursor = db.rawQuery(queryString, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Recruitment recruitment=new Recruitment();

            recruitment.setId(cursor.getString(0));
            recruitment.setName(cursor.getString(1));
            recruitment.setDistrict(cursor.getString(2));
            recruitment.setSubcounty(cursor.getString(3));
            recruitment.setDivision(cursor.getString(4));
            recruitment.setLat(cursor.getString(5));
            recruitment.setLon(cursor.getString(6));
            recruitment.setAddedBy(cursor.getInt(7));
            recruitment.setComment(cursor.getString(8));
            recruitment.setDateAdded(cursor.getLong(9));
            recruitment.setSynced(cursor.getInt(10));
            recruitment.setCountry(cursor.getString(11));
            recruitment.setCounty(cursor.getString(12));

            recruitments.add(recruitment);
        }

        db.close();

        return recruitments;
    }
    public JSONObject getRecruitmentJson() {

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

    public Recruitment getRecruitmentById(String id) {

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                id
        };
        Log.e("expansion", "requested recruitment ID "+ id);
        Cursor cursor = db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else {

            Recruitment recruitment = new Recruitment();

            recruitment.setId(cursor.getString(0));
            recruitment.setName(cursor.getString(1));
            recruitment.setDistrict(cursor.getString(2));
            recruitment.setSubcounty(cursor.getString(3));
            recruitment.setDivision(cursor.getString(4));
            recruitment.setLat(cursor.getString(5));
            recruitment.setLon(cursor.getString(6));
            recruitment.setAddedBy(cursor.getInt(7));
            recruitment.setComment(cursor.getString(8));
            recruitment.setDateAdded(cursor.getLong(9));
            recruitment.setSynced(cursor.getInt(10));
            recruitment.setCountry(cursor.getString(11));
            recruitment.setCounty(cursor.getString(12));
            SubCountyTable subCountyTable = new SubCountyTable(context);
            KeCountyTable keCountyTable = new KeCountyTable(context);

            if (cursor.getString(11).equalsIgnoreCase("UG")) {
                recruitment.setSubCountyObj(subCountyTable.getSubCountyById(cursor.getString(3)));
            } else {

                recruitment.setKeCounty(keCountyTable.getCountyById(cursor.getInt(12)));
                recruitment.setName(keCountyTable.getCountyById(cursor.getInt(12)).getCountyName());
                recruitment.setSubCountyObj(subCountyTable.getSubCountyById(cursor.getString(3)));
            }
            db.close();

            return recruitment;
        }
    }

    public JSONObject getRecruitmentToSyncAsJson() {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = SYNCED+" = ?";
        String[] whereArgs = new String[] {
                "0",
        };

        Cursor cursor=db.query(TABLE_NAME,columns, whereClause, whereArgs,null,null,null,null);

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


    public Cursor getRecruitmentDataCursor() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        return  cursor;
    }
    private void upgradeVersion2(SQLiteDatabase db) {
        // For each recruitment, update the County Name with the County ID
        // For each recruitment, update the Sub County Name with the County ID


        String [] columns=new String[]{ID, NAME, DISTRICT, SUB_COUNTY, DIVISION, LAT, LON, ADDED_BY,
                COMMENT, DATE_ADDED, SYNCED, COUNTRY, COUNTY};

            Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null, null);

            for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
                // with this selection, let us extract the details
                //
                //
                String country = cursor.getString(cursor.getColumnIndex(COUNTRY));
                if (country.equalsIgnoreCase("UG")){
                    // get the county
                    CountyLocationTable countyLocationTable = new CountyLocationTable(context);
                    countyLocationTable.createLocations();
                    //Thread.sleep(timeInMills);
                    try {
                        Thread.sleep(1500L);
                    }catch (Exception e){}

                    // also update the district
                    String districtName = cursor.getString(cursor.getColumnIndex(DISTRICT));
                    CountyLocation district = countyLocationTable.getDistrictByName(districtName);
                    Long districtId;
                    if (district == null){
                        try {
                            Log.e("expansion", "Creating New District "+ districtName);
                            CountyLocation countyLocation = new CountyLocation("District", districtName,
                                    "UG", 0, "", null, "", "", "", 2, "");

                            Long id = countyLocationTable.addData(countyLocation);
                            Log.e("expansion", "New district has been created and ID is: " + String.valueOf(id));
                        }catch (Exception e){
                            Log.e("expansion", e.getMessage());
                        }
                    }
                    try {
                        Thread.sleep(1000L);
                    }catch (Exception e){}
                    CountyLocation savedDistrict = countyLocationTable.getDistrictByName(districtName);
                    if (savedDistrict != null){
                        ContentValues cv = new ContentValues();
                        cv.put(DISTRICT, String.valueOf(savedDistrict.getId()));
                        db.update(TABLE_NAME, cv, ID+"='"+cursor.getString(0)+"'", null);
                    }else{
                        CountyLocation retrievedDistrict = countyLocationTable.getDistrictByName(districtName);
                        Log.e("expansion", "Could not get the district with the name "+ districtName);
                        if (retrievedDistrict != null){
                            ContentValues cv = new ContentValues();
                            cv.put(DISTRICT, String.valueOf(retrievedDistrict.getId()));
                            db.update(TABLE_NAME, cv, ID+"='"+cursor.getString(0)+"'", null);
                        }

                    }
                }else{
                    //Get the county
                    KeCountyTable keCountyTable = new KeCountyTable(context);
                    KeCounty keCounty;
                    keCounty = keCountyTable.getKeCountyByName(cursor.getString(cursor.getColumnIndex(COUNTY)));
                    if (keCounty == null){
                        // create a holding county
                        KeCounty ke = new KeCounty();
                        ke.setCountyName(cursor.getString(cursor.getColumnIndex(COUNTY)));
                        ke.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
                        keCountyTable.addKeCounty(ke);
                    }
                    KeCounty addedKeCounty = keCountyTable.getKeCountyByName(cursor.getString(cursor.getColumnIndex(COUNTY)));
                    // get subCounty
                    SubCountyTable subCountyTable = new SubCountyTable(context);
                    SubCounty subCounty = subCountyTable.getSubCountyByCountyAndName(String.valueOf(
                            addedKeCounty.getId()), country, cursor.getString(cursor.getColumnIndex(SUB_COUNTY)));
                    if (subCounty == null){
                        // Create subcounty
                        String uuid = UUID.randomUUID().toString();
                        subCounty = new SubCounty(uuid, cursor.getString(cursor.getColumnIndex(SUB_COUNTY)),
                                String.valueOf(keCounty.getId()), country, "", "", "", "", "", "",
                                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", false,0,0);
                        subCountyTable.addData(subCounty);
                    }
                    SubCounty addedSubCounty = subCountyTable.getSubCountyByCountyAndName(String.valueOf(
                            addedKeCounty.getId()), country, cursor.getString(cursor.getColumnIndex(SUB_COUNTY)));
                    // noe we can update the Recruitment
                    ContentValues cv = new ContentValues();
                    cv.put(COUNTY, String.valueOf(addedKeCounty.getId()));
                    cv.put(SUB_COUNTY, String.valueOf(addedSubCounty.getId()));
                    db.update(TABLE_NAME, cv, ID+"='"+cursor.getString(0)+"'", null);
                }
            }
        }
}

