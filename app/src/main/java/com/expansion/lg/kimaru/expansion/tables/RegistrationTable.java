package com.expansion.lg.kimaru.expansion.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimaru on 3/11/17.
 */


public class RegistrationTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="registration";
    public static final String DATABASE_NAME="expansion";
    public static final int DATABASE_VERSION=1;
    public static final String JSON_ROOT="registrations";

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID = "id";
    public static final String NAME= "name";
    public static final String PHONE = "phone";
    public static final String GENDER = "gender";
    public static final String RECRUITMENT = "recruitment";
    public static final String COUNTRY = "country";
    public static final String DOB = "dob";
    public static final String DISTRICT = "district";
    public static final String SUB_COUNTY = "subcounty";
    public static final String DIVISION = "division";
    public static final String VILLAGE = "village";
    public static final String MARK = "feature";
    public static final String READ_ENGLISH = "english";
    public static final String DATE_MOVED = "date_moved";
    public static final String LANGS = "languages";
    public static final String BRAC = "brac";
    public static final String BRAC_CHP = "brac_chp";
    public static final String EDUCATION = "education";
    public static final String OCCUPATION = "occupation";
    public static final String COMMUNITY = "community_membership";
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String PROCEED = "proceed";
    public static final String DATE_ADDED = "date_added";
    public static final String SYNCED = "synced";
    public static final String CHEW_NAME = "chew_name";
    public static final String CHEW_NUMBER = "chew_number";
    public static final String WARD = "ward";
    public static final String CU_NAME = "cu_name";
    public static final String LINK_FACILITY = "link_facility";
    public static final String HOUSEHOLDS = "households";
    public static final String TRAININGS = "trainings";
    public static final String CHV = "is_chv";
    public static final String GOK_TRAINED = "is_gok_trained";
    public static final String REFERRAL_NAME = "referral";
    public static final String REFERRAL_NUMBER = "referral_number";
    public static final String REFERRAL_TITLE = "referral_title";
    public static final String VHT = "vht";
    public static final String PARISH = "parish";
    public static final String ACCOUNTS = "financial_accounts";
    public static final String REC_TRANSPORT = "recruitment_transport";
    public static final String BRANCH_TRANPORT = "branch_transport";

    Context context;

    public static final String [] columns=new String[]{ID, NAME, PHONE, GENDER, DOB, DISTRICT, SUB_COUNTY, DIVISION,
            VILLAGE, MARK, READ_ENGLISH, DATE_MOVED, LANGS, BRAC, BRAC_CHP, EDUCATION, OCCUPATION,
            COMMUNITY, ADDED_BY, COMMENT, PROCEED, DATE_ADDED, SYNCED, RECRUITMENT, COUNTRY,
            CHEW_NAME, CHEW_NUMBER, WARD, CU_NAME, LINK_FACILITY, HOUSEHOLDS, TRAININGS, CHV,
            GOK_TRAINED, REFERRAL_NAME, REFERRAL_NUMBER, REFERRAL_TITLE, VHT, PARISH, ACCOUNTS,
            REC_TRANSPORT, BRANCH_TRANPORT};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + NAME + varchar_field + ", "
            + PHONE + varchar_field + ", "
            + GENDER + varchar_field + ", "
            + DOB + integer_field + ", "
            + DISTRICT + varchar_field + ", "
            + SUB_COUNTY + varchar_field + ", "
            + RECRUITMENT + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + DIVISION + varchar_field + ", "
            + VILLAGE + varchar_field + ", "
            + MARK + text_field + ", "
            + READ_ENGLISH + integer_field + ", "
            + DATE_MOVED + integer_field + ", "
            + LANGS + varchar_field + ", "
            + BRAC + integer_field + ", "
            + BRAC_CHP + integer_field + ", "
            + EDUCATION + text_field + ", "
            + OCCUPATION + text_field + ", "
            + COMMUNITY + integer_field + ", "
            + ADDED_BY + integer_field + ", "
            + COMMENT + text_field + ", "
            + PROCEED + integer_field + ", "
            + DATE_ADDED + integer_field + ", "
            + CHEW_NAME + varchar_field + ", "
            + CHEW_NUMBER + varchar_field + ", "
            + WARD + varchar_field + ", "
            + CU_NAME + varchar_field + ", "
            + LINK_FACILITY + varchar_field + ", "
            + HOUSEHOLDS + integer_field + ", "
            + TRAININGS + text_field + ", "
            + CHV + integer_field + ", "
            + GOK_TRAINED + integer_field + ", "
            + REFERRAL_NAME + varchar_field + ", "
            + REFERRAL_TITLE + varchar_field + ", "
            + REFERRAL_NUMBER + varchar_field + ", "
            + VHT + integer_field + ", "
            + PARISH + varchar_field + ", "
            + ACCOUNTS + integer_field + ", "
            + REC_TRANSPORT + integer_field + ", "
            + BRANCH_TRANPORT + integer_field + ", "
            + SYNCED + integer_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public RegistrationTable(Context context) {
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
        db.execSQL(DATABASE_DROP);
    }

    public long addData(Registration registration) {

        SQLiteDatabase db=getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(ID, registration.getId());
        cv.put(NAME, registration.getName());
        cv.put(PHONE, registration.getPhone());
        cv.put(GENDER, registration.getGender());
        cv.put(DOB, registration.getDob());
        cv.put(COUNTRY, registration.getCountry());
        cv.put(RECRUITMENT, registration.getRecruitment());
        cv.put(DISTRICT, registration.getDistrict());
        cv.put(SUB_COUNTY, registration.getSubcounty());
        cv.put(DIVISION, registration.getDivision());
        cv.put(VILLAGE, registration.getVillage());
        cv.put(MARK, registration.getMark());
        cv.put(READ_ENGLISH, registration.getReadEnglish());
        cv.put(DATE_MOVED, registration.getDateMoved());
        cv.put(LANGS, registration.getLangs());
        cv.put(BRAC, registration.getBrac());
        cv.put(BRAC_CHP, registration.getBracChp());
        cv.put(EDUCATION, registration.getEducation());
        cv.put(OCCUPATION, registration.getOccupation());
        cv.put(COMMUNITY, registration.getCommunity());
        cv.put(ADDED_BY, registration.getAddedBy());
        cv.put(COMMENT, registration.getComment());
        cv.put(PROCEED, registration.getProceed());
        cv.put(DATE_ADDED, registration.getDateAdded());
        cv.put(SYNCED, registration.getSynced());
        cv.put(CHEW_NAME, registration.getChewName());
        cv.put(CHEW_NUMBER, registration.getChewNumber());
        cv.put(WARD, registration.getWard());
        cv.put(CU_NAME, registration.getCuName());
        cv.put(LINK_FACILITY, registration.getLinkFacility());
        cv.put(HOUSEHOLDS, registration.getNoOfHouseholds());
        cv.put(TRAININGS, registration.getOtherTrainings());
        cv.put(CHV, registration.isChv() ? 1 : 0);
        cv.put(GOK_TRAINED, registration.isGokTrained() ? 1 : 0);
        cv.put(REFERRAL_NAME, registration.getReferralName());
        cv.put(REFERRAL_NUMBER, registration.getReferralPhone());
        cv.put(REFERRAL_TITLE, registration.getReferralTitle());
        cv.put(VHT, registration.isVht() ? 1 : 0);
        cv.put(PARISH, registration.getParish());
        cv.put(ACCOUNTS, registration.isAccounts() ? 1 : 0);
        cv.put(REC_TRANSPORT, registration.getRecruitmentTransportCost());
        cv.put(BRANCH_TRANPORT, registration.getTransportCostToBranch());

        long id;
        if (isExist(registration)){
            cv.put(SYNCED, 0);
            id = db.update(TABLE_NAME, cv, ID+"='"+registration.getId()+"'", null);
        }else{
            id = db.insert(TABLE_NAME,null,cv);
        }
        db.close();
        return id;

    }
    public Cursor getRegistrationDataCursor() {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = "id desc";

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,orderBy,null);
        db.close();
        return cursor;
    }

    public List<Registration> getRegistrationData() {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = "id desc";

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,orderBy,null);

        List<Registration> registrationList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Registration registration=new Registration();

            registration.setId(cursor.getString(0));
            registration.setName(cursor.getString(1));
            registration.setPhone(cursor.getString(2));
            registration.setGender(cursor.getString(3));
            registration.setDob(cursor.getLong(4));
            registration.setDistrict(cursor.getString(5));
            registration.setSubcounty(cursor.getString(6));
            registration.setDivision(cursor.getString(7));
            registration.setVillage(cursor.getString(8));
            registration.setMark(cursor.getString(9));
            registration.setReadEnglish(cursor.getInt(10));
            registration.setDateMoved(cursor.getLong(11));
            registration.setLangs(cursor.getString(12));
            registration.setBrac(cursor.getInt(13));
            registration.setBracChp(cursor.getInt(14));
            registration.setEducation(cursor.getString(15));
            registration.setOccupation(cursor.getString(16));
            registration.setCommunity(cursor.getInt(17));
            registration.setAddedBy(cursor.getInt(18));
            registration.setComment(cursor.getString(19));
            registration.setProceed(cursor.getInt(20));
            registration.setDateAdded(cursor.getLong(21));
            registration.setSynced(cursor.getInt(22));
            registration.setRecruitment(cursor.getString(23));
            registration.setCountry(cursor.getString(24));
            registration.setChewName(cursor.getString(25));
            registration.setChewNumber(cursor.getString(26));
            registration.setWard(cursor.getString(27));
            registration.setCuName(cursor.getString(28));
            registration.setLinkFacility(cursor.getString(29));
            registration.setNoOfHouseholds(cursor.getLong(30));
            registration.setOtherTrainings(cursor.getString(31));
            registration.setChv(cursor.getInt(32) == 1);
            registration.setGokTrained(cursor.getInt(33) == 1);
            registration.setReferralName(cursor.getString(34));
            registration.setReferralPhone(cursor.getString(35));
            registration.setReferralTitle(cursor.getString(36));
            registration.setVht(cursor.getInt(37) == 1);
            registration.setParish(cursor.getString(38));
            registration.setAccounts(cursor.getInt(39) == 1);
            registration.setRecruitmentTransportCost(cursor.getLong(40));
            registration.setTransportCostToBranch(cursor.getLong(41));
            registration.setPicture("");
            registrationList.add(registration);
        }
        db.close();
        return registrationList;
    }

    public Registration getRegistrationById(String registrationUuid){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                registrationUuid,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            Registration registration=new Registration();

            registration.setId(cursor.getString(0));
            registration.setName(cursor.getString(1));
            registration.setPhone(cursor.getString(2));
            registration.setGender(cursor.getString(3));
            registration.setDob(cursor.getLong(4));
            registration.setDistrict(cursor.getString(5));
            registration.setSubcounty(cursor.getString(6));
            registration.setDivision(cursor.getString(7));
            registration.setVillage(cursor.getString(8));
            registration.setMark(cursor.getString(9));
            registration.setReadEnglish(cursor.getInt(10));
            registration.setDateMoved(cursor.getLong(11));
            registration.setLangs(cursor.getString(12));
            registration.setBrac(cursor.getInt(13));
            registration.setBracChp(cursor.getInt(14));
            registration.setEducation(cursor.getString(15));
            registration.setOccupation(cursor.getString(16));
            registration.setCommunity(cursor.getInt(17));
            registration.setAddedBy(cursor.getInt(18));
            registration.setComment(cursor.getString(19));
            registration.setProceed(cursor.getInt(20));
            registration.setDateAdded(cursor.getLong(21));
            registration.setSynced(cursor.getInt(22));
            registration.setRecruitment(cursor.getString(23));
            registration.setCountry(cursor.getString(24));
            registration.setChewName(cursor.getString(25));
            registration.setChewNumber(cursor.getString(26));
            registration.setWard(cursor.getString(27));
            registration.setCuName(cursor.getString(28));
            registration.setLinkFacility(cursor.getString(29));
            registration.setNoOfHouseholds(cursor.getLong(30));
            registration.setOtherTrainings(cursor.getString(31));
            registration.setChv(cursor.getInt(32) == 1);
            registration.setGokTrained(cursor.getInt(33) == 1);
            registration.setReferralName(cursor.getString(34));
            registration.setReferralPhone(cursor.getString(35));
            registration.setReferralTitle(cursor.getString(36));
            registration.setVht(cursor.getInt(37) == 1);
            registration.setParish(cursor.getString(38));
            registration.setAccounts(cursor.getInt(39) == 1);
            registration.setRecruitmentTransportCost(cursor.getLong(40));
            registration.setTransportCostToBranch(cursor.getLong(41));
            registration.setPicture("");
            db.close();
            return registration;
        }

    }


    public boolean isExist(Registration registration) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT "+ID+" FROM " + TABLE_NAME + " WHERE " + ID + " = '" + registration.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }


    public long getRegistrationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return cnt;
    }

    public List<Registration> getRegistrationsByRecruitment(Recruitment recruitment) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED + " desc";
        String whereClause = RECRUITMENT+" = ?";
        String[] whereArgs = new String[] {
                recruitment.getId(),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);

        List<Registration> registrationList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Registration registration=new Registration();

            registration.setId(cursor.getString(0));
            registration.setName(cursor.getString(1));
            registration.setPhone(cursor.getString(2));
            registration.setGender(cursor.getString(3));
            registration.setDob(cursor.getLong(4));
            registration.setDistrict(cursor.getString(5));
            registration.setSubcounty(cursor.getString(6));
            registration.setDivision(cursor.getString(7));
            registration.setVillage(cursor.getString(8));
            registration.setMark(cursor.getString(9));
            registration.setReadEnglish(cursor.getInt(10));
            registration.setDateMoved(cursor.getLong(11));
            registration.setLangs(cursor.getString(12));
            registration.setBrac(cursor.getInt(13));
            registration.setBracChp(cursor.getInt(14));
            registration.setEducation(cursor.getString(15));
            registration.setOccupation(cursor.getString(16));
            registration.setCommunity(cursor.getInt(17));
            registration.setAddedBy(cursor.getInt(18));
            registration.setComment(cursor.getString(19));
            registration.setProceed(cursor.getInt(20));
            registration.setDateAdded(cursor.getLong(21));
            registration.setSynced(cursor.getInt(22));
            registration.setRecruitment(cursor.getString(23));
            registration.setCountry(cursor.getString(24));
            registration.setChewName(cursor.getString(25));
            registration.setChewNumber(cursor.getString(26));
            registration.setWard(cursor.getString(27));
            registration.setCuName(cursor.getString(28));
            registration.setLinkFacility(cursor.getString(29));
            registration.setNoOfHouseholds(cursor.getLong(30));
            registration.setOtherTrainings(cursor.getString(31));
            registration.setChv(cursor.getInt(32) == 1);
            registration.setGokTrained(cursor.getInt(33) == 1);
            registration.setReferralName(cursor.getString(34));
            registration.setReferralPhone(cursor.getString(35));
            registration.setReferralTitle(cursor.getString(36));
            registration.setVht(cursor.getInt(37) == 1);
            registration.setParish(cursor.getString(38));
            registration.setAccounts(cursor.getInt(39) == 1);
            registration.setRecruitmentTransportCost(cursor.getLong(40));
            registration.setTransportCostToBranch(cursor.getLong(41));
            registration.setPicture("");
            registrationList.add(registration);
        }
        db.close();
        return registrationList;
    }

    public void fromJsonObject(JSONObject jsonObject){
        try{
            Registration registration=new Registration();
            registration.setId(jsonObject.getString(ID));
            registration.setName(jsonObject.getString(NAME));
            registration.setPhone(jsonObject.getString(PHONE));
            registration.setGender(jsonObject.getString(GENDER));
            registration.setDob(jsonObject.getLong(DOB));
            registration.setDistrict(jsonObject.getString(DISTRICT));
            registration.setCountry(jsonObject.getString(COUNTRY));
            registration.setRecruitment(jsonObject.getString(RECRUITMENT));
            registration.setSubcounty(jsonObject.getString(SUB_COUNTY));
            registration.setDivision(jsonObject.getString(DIVISION));
            registration.setVillage(jsonObject.getString(VILLAGE));
            registration.setMark(jsonObject.getString(MARK));
            registration.setReadEnglish(jsonObject.getInt(READ_ENGLISH));
            registration.setDateMoved(jsonObject.getLong(DATE_MOVED));
            registration.setLangs(jsonObject.getString(LANGS));
            registration.setBrac(jsonObject.getInt(BRAC));
            registration.setBracChp(jsonObject.getInt(BRAC_CHP));
            registration.setEducation(jsonObject.getString(EDUCATION));
            registration.setOccupation(jsonObject.getString(OCCUPATION));
            registration.setCommunity(jsonObject.getInt(COMMUNITY));
            registration.setAddedBy(jsonObject.getInt(ADDED_BY));
            registration.setComment(jsonObject.getString(COMMENT));
            registration.setProceed(jsonObject.getInt(PROCEED));
            registration.setDateAdded(jsonObject.getLong(DATE_ADDED));
            registration.setSynced(jsonObject.getInt(SYNCED));
            registration.setChewName(jsonObject.getString(CHEW_NAME));
            registration.setChewNumber(jsonObject.getString(CHEW_NUMBER));
            registration.setWard(jsonObject.getString(WARD));
            registration.setCuName(jsonObject.getString(CU_NAME));
            registration.setLinkFacility(jsonObject.getString(LINK_FACILITY));
            registration.setNoOfHouseholds(jsonObject.getLong(HOUSEHOLDS));
            registration.setOtherTrainings(jsonObject.getString(TRAININGS));
            registration.setChv(jsonObject.getInt(CHV) == 1);
            registration.setGokTrained(jsonObject.getInt(GOK_TRAINED) == 1);
            registration.setReferralName(jsonObject.getString(REFERRAL_NAME));
            registration.setReferralPhone(jsonObject.getString(REFERRAL_NUMBER));
            registration.setReferralTitle(jsonObject.getString(REFERRAL_TITLE));
            registration.setVht(jsonObject.getInt(VHT) == 1);
            registration.setParish(jsonObject.getString(PARISH));
            registration.setAccounts(jsonObject.getInt(ACCOUNTS) == 1);
            registration.setRecruitmentTransportCost(jsonObject.getLong(REC_TRANSPORT));
            registration.setTransportCostToBranch(jsonObject.getLong(BRANCH_TRANPORT));
            registration.setPicture("");
            this.addData(registration);
        }catch (Exception e){}
    }

    public JSONObject getRegistrationJson() {

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
    public JSONObject getRegistrationToSyncAsJson() {

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = SYNCED+" = ?";
        String[] whereArgs = new String[] {
                "0",
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

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
}
