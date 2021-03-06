package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Exam;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.mzigos.Registration;
import com.expansion.lg.kimaru.expansion.other.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class ExamTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="exam";
    public static final String JSON_ROOT="exams";
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

    Context context;


    public static String varchar_field = " varchar(512) ";
    public static String real_field = " REAL ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID= "id";
    public static final String APPLICANT= "applicant";
    public static final String RECRUITMENT = "recruitment";
    public static final String COUNTRY = "country";
    public static final String MATH = "math";
    public static final String PERSONALITY = "personality";
    public static final String ENGLISH = "english";
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String DATE_ADDED = "client_time";
    public static final String SYNCED = "synced";
    String [] columns=new String[]{ID, APPLICANT, RECRUITMENT, MATH, PERSONALITY, ENGLISH,
            ADDED_BY, COMMENT, DATE_ADDED, SYNCED, COUNTRY};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + APPLICANT + varchar_field + ", "
            + RECRUITMENT + varchar_field + ", "
            + COUNTRY + varchar_field + ", "
            + MATH + real_field + ", "
            + PERSONALITY + real_field + ", "
            + ENGLISH + real_field + ", "
            + ADDED_BY + integer_field + ", "
            + COMMENT + text_field + ", "
            + DATE_ADDED + integer_field + ", "
            + SYNCED + integer_field + "); ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public ExamTable(Context context) {
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

    public long addData(Exam exam) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ID, exam.getId());
        cv.put(APPLICANT, exam.getApplicant());
        cv.put(RECRUITMENT, exam.getRecruitment());
        cv.put(COUNTRY, exam.getCountry());
        cv.put(MATH, exam.getMath());
        cv.put(PERSONALITY, exam.getPersonality());
        cv.put(ENGLISH, exam.getEnglish());
        cv.put(ADDED_BY, exam.getAddedBy());
        cv.put(COMMENT, exam.getComment());
        cv.put(DATE_ADDED, exam.getDateAdded());
        cv.put(SYNCED, exam.getSynced());


        long id;
        if (isExist(exam)){
            id = db.update(TABLE_NAME, cv, ID+"='"+exam.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();

        // When one passes the interview, we need to update the registration
        RegistrationTable registrationTable = new RegistrationTable(context);
        Registration registration = registrationTable.getRegistrationById(exam.getApplicant());
        if (registration.hasPassed() && exam.hasPassed()){
            registration.setProceed(1);
        }else{
            registration.setProceed(0);
        }
        registrationTable.addData(registration);
        return id;

    }
    public List<Exam> getExamData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Exam> examList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Exam exam=new Exam();

            exam.setId(cursor.getString(0));
            exam.setApplicant(cursor.getString(1));
            exam.setRecruitment(cursor.getString(2));
            exam.setMath(cursor.getDouble(3));
            exam.setPersonality(cursor.getDouble(4));
            exam.setEnglish(cursor.getDouble(5));
            exam.setAddedBy(cursor.getInt(6));
            exam.setComment(cursor.getString(7));
            exam.setDateAdded(cursor.getLong(8));
            exam.setSynced(cursor.getInt(9));
            exam.setCountry(cursor.getString(10));

            examList.add(exam);
        }
        db.close();

        return examList;
    }

    public Exam getExamByRegistration(String registrationUuid){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = APPLICANT+" = ?";
        String[] whereArgs = new String[] {
                registrationUuid,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            Exam exam=new Exam();
            exam.setId(cursor.getString(0));
            exam.setApplicant(cursor.getString(1));
            exam.setRecruitment(cursor.getString(2));
            exam.setMath(cursor.getDouble(3));
            exam.setPersonality(cursor.getDouble(4));
            exam.setEnglish(cursor.getDouble(5));
            exam.setAddedBy(cursor.getInt(6));
            exam.setComment(cursor.getString(7));
            exam.setDateAdded(cursor.getLong(8));
            exam.setSynced(cursor.getInt(9));
            exam.setCountry(cursor.getString(10));
            return exam;
        }

    }

    public void fromJson(JSONObject jsonObject){
        try{
            Exam exam=new Exam();
            exam.setId(jsonObject.getString(ExamTable.ID));
            exam.setApplicant(jsonObject.getString(ExamTable.APPLICANT));
            exam.setRecruitment(jsonObject.getString(ExamTable.RECRUITMENT));
            exam.setCountry(jsonObject.getString(ExamTable.COUNTRY));
            exam.setMath(jsonObject.getDouble(ExamTable.MATH));
            exam.setPersonality(jsonObject.getDouble(ExamTable.PERSONALITY));
            exam.setEnglish(jsonObject.getDouble(ExamTable.ENGLISH));
            exam.setAddedBy(jsonObject.getInt(ExamTable.ADDED_BY));
            exam.setComment(jsonObject.getString(ExamTable.COMMENT));
            exam.setDateAdded(jsonObject.getLong(ExamTable.DATE_ADDED));
            exam.setSynced(jsonObject.getInt(ExamTable.SYNCED));
            this.addData(exam);
        }catch (Exception e){}
    }

    public Exam getExamById(String id){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                id,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            Exam exam=new Exam();
            exam.setId(cursor.getString(0));
            exam.setApplicant(cursor.getString(1));
            exam.setRecruitment(cursor.getString(2));
            exam.setMath(cursor.getDouble(3));
            exam.setPersonality(cursor.getDouble(4));
            exam.setEnglish(cursor.getDouble(5));
            exam.setAddedBy(cursor.getInt(6));
            exam.setComment(cursor.getString(7));
            exam.setDateAdded(cursor.getLong(8));
            exam.setSynced(cursor.getInt(9));
            exam.setCountry(cursor.getString(10));
            return exam;
        }

    }

    public List<Exam> getExamsByRecruitment(Recruitment recruitment) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED + " desc";
        String whereClause = RECRUITMENT+" = ?";
        String[] whereArgs = new String[] {
                recruitment.getId(),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);


        List<Exam> examList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Exam exam=new Exam();

            exam.setId(cursor.getString(0));
            exam.setApplicant(cursor.getString(1));
            exam.setRecruitment(cursor.getString(2));
            exam.setMath(cursor.getDouble(3));
            exam.setPersonality(cursor.getDouble(4));
            exam.setEnglish(cursor.getDouble(5));
            exam.setAddedBy(cursor.getInt(6));
            exam.setComment(cursor.getString(7));
            exam.setDateAdded(cursor.getLong(8));
            exam.setSynced(cursor.getInt(9));
            exam.setCountry(cursor.getString(10));

            examList.add(exam);
        }
        db.close();

        return examList;
    }

    public boolean isExist(Exam exam) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + exam.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public long getPendingRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                SYNCED + "=?",
                new String[] {String.valueOf(Constants.SYNC_STATUS_UNSYNCED)});
        db.close();
        return cnt;
    }

    public long getExamCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return cnt;
    }

    public Cursor getExamDataCursor() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        return cursor;
    }
    public JSONObject getExamJson() {

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

    public JSONObject getExamsToSyncAsJson(int offset) {

        SQLiteDatabase db=getReadableDatabase();
        String whereClause = SYNCED+" = ?";
        String[] whereArgs = new String[] {
                "0",
        };

        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,
                String.format("%d,%d", offset, Constants.SYNC_PAGINATION_SIZE));

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

