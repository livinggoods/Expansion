package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kimaru on 2/28/17.
 */


public class InterviewTable extends SQLiteOpenHelper {

    public static final String TABLE_NAME="interview";
    public static final String JSON_ROOT="interviews";
    public static final String DATABASE_NAME="expansion";
    public static final int DATABASE_VERSION=1;

    public static String varchar_field = " varchar(512) ";
    public static String primary_field = " _id INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";


    //applicant, recruitment, motivation, community, mentality, selling, health, investment,
    // interpersonal, commitment, total, selected, addedBy,
    // dateAdded, synced
    public static final String ID = "id";
    public static final String APPLICANT= "applicant";
    public static final String RECRUITMENT = "recruitment";
    public static final String MOTIVATION = "motivation";
    public static final String COMMUNITY = "community";
    public static final String MENTALITY = "mentality";
    public static final String COUNTRY = "country";
    public static final String SELLING = "selling";
    public static final String HEALTH = "health";
    public static final String INVESTMENT = "investment";
    public static final String INTERPERSONAL = "interpersonal";
    public static final String CANJOIN = "canjoin";
    public static final String COMMITMENT = "commitment";
    public static final String TOTAL = "total";
    public static final String SELECTED = "selected";
    public static final String SYNCED = "synced";
    public static final String ADDED_BY = "added_by";
    public static final String COMMENT = "comment";
    public static final String DATE_ADDED = "date_added";
    String [] columns=new String[]{ID, APPLICANT, RECRUITMENT, MOTIVATION, COMMUNITY,MENTALITY,
            SELLING, HEALTH, INVESTMENT, INTERPERSONAL, TOTAL, SELECTED, ADDED_BY, COMMENT,
            COMMITMENT, DATE_ADDED, SYNCED, CANJOIN, COUNTRY};

    public static final String CREATE_DATABASE="CREATE TABLE " + TABLE_NAME + "("
            + ID + varchar_field + ", "
            + APPLICANT + varchar_field + ", "
            + RECRUITMENT + integer_field + ", "
            + MOTIVATION + integer_field + ", "
            + COMMUNITY + integer_field + ", "
            + MENTALITY + integer_field + ", "
            + SELLING + integer_field + ", "
            + COUNTRY + varchar_field + ", "
            + HEALTH + integer_field + ", "
            + INVESTMENT + integer_field + ", "
            + INTERPERSONAL + integer_field + ", "
            + COMMITMENT + integer_field + ", "
            + TOTAL + integer_field + ", "
            + CANJOIN + integer_field + ", "
            + SELECTED + integer_field + ", "
            + ADDED_BY + integer_field + ", "
            + COMMENT + text_field + ", "
            + DATE_ADDED + integer_field + ", "
            + SYNCED + integer_field + ") ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;

    public InterviewTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
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

    public long addData(Interview interview) {

        SQLiteDatabase db=getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put(ID, interview.getId());
        cv.put(APPLICANT, interview.getApplicant());
        cv.put(RECRUITMENT, interview.getRecruitment());
        cv.put(MOTIVATION, interview.getMotivation());
        cv.put(COMMUNITY, interview.getCommunity());
        cv.put(MENTALITY, interview.getMentality());
        cv.put(COUNTRY, interview.getCountry());
        cv.put(SELLING, interview.getSelling());
        cv.put(HEALTH, interview.getHealth());
        cv.put(INVESTMENT, interview.getInvestment());
        cv.put(INTERPERSONAL, interview.getInterpersonal());
        cv.put(TOTAL, interview.getTotal());
        cv.put(SELECTED, interview.getSelected()? 1 : 0);
        cv.put(CANJOIN, interview.isCanJoin() ? 1 : 0);
        cv.put(ADDED_BY, interview.getAddedBy());
        cv.put(COMMENT, interview.getComment());
        cv.put(COMMITMENT, interview.getCommitment());
        cv.put(DATE_ADDED, interview.getDateAdded());
        cv.put(SYNCED, interview.getSynced());

        long id;
        if (isExist(interview)){
            id = db.update(TABLE_NAME, cv, ID+"='"+interview.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }

        db.close();
        return id;

    }
    public boolean isExist(Interview interview) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE "+ID+" = '" + interview.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public List<Interview> getInterviewData() {

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        List<Interview> interviewList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Interview interview = new Interview();

            interview.setId(cursor.getString(0));
            interview.setApplicant(cursor.getString(1));
            interview.setRecruitment(cursor.getString(2));
            interview.setMotivation(cursor.getInt(3));
            interview.setCommunity(cursor.getInt(4));
            interview.setMentality(cursor.getInt(5));
            interview.setSelling(cursor.getInt(6));
            interview.setHealth(cursor.getInt(7));
            interview.setInvestment(cursor.getInt(8));
            interview.setInterpersonal(cursor.getInt(9));
            interview.setSelected(cursor.getInt(11) == 1);
            interview.setAddedBy(cursor.getInt(12));
            interview.setComment(cursor.getString(13));
            interview.setCommitment(cursor.getInt(14));
            interview.setDateAdded(cursor.getLong(15));
            interview.setSynced(cursor.getInt(16));
            interview.setCanJoin(cursor.getInt(17) == 1);
            interview.setCountry(cursor.getString(18));

            interviewList.add(interview);
        }

        db.close();

        return interviewList;
    }
    public Cursor getInterviewDataCursor() {

        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);

        return cursor;
    }
    public Interview getInterviewByRegistrationId (String registrationUuid){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = APPLICANT+" = ?";
        String[] whereArgs = new String[] {
                registrationUuid,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);

        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{

            Interview interview = new Interview();

            interview.setId(cursor.getString(0));
            interview.setApplicant(cursor.getString(1));
            interview.setRecruitment(cursor.getString(2));
            interview.setMotivation(cursor.getInt(3));
            interview.setCommunity(cursor.getInt(4));
            interview.setMentality(cursor.getInt(5));
            interview.setSelling(cursor.getInt(6));
            interview.setHealth(cursor.getInt(7));
            interview.setInvestment(cursor.getInt(8));
            interview.setInterpersonal(cursor.getInt(9));
            // interview.setTotal(cursor.getInt(10));
            interview.setSelected(cursor.getInt(11) == 1);
            interview.setAddedBy(cursor.getInt(12));
            interview.setComment(cursor.getString(13));
            interview.setCommitment(cursor.getInt(14));
            interview.setDateAdded(cursor.getLong(15));
            interview.setSynced(cursor.getInt(16));
            interview.setCanJoin(cursor.getInt(17) == 1);
            return interview;
        }
    }

    public List<Interview> getInterviewsByRecruitment(Recruitment recruitment) {

        SQLiteDatabase db=getReadableDatabase();

        String whereClause = APPLICANT+" = ?";
        String[] whereArgs = new String[] {
                recruitment.getId(),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);


        List<Interview> interviewList=new ArrayList<>();


        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){


            Interview interview = new Interview();

            interview.setId(cursor.getString(0));
            interview.setApplicant(cursor.getString(1));
            interview.setRecruitment(cursor.getString(2));
            interview.setMotivation(cursor.getInt(3));
            interview.setCommunity(cursor.getInt(4));
            interview.setMentality(cursor.getInt(5));
            interview.setSelling(cursor.getInt(6));
            interview.setHealth(cursor.getInt(7));
            interview.setInvestment(cursor.getInt(8));
            interview.setInterpersonal(cursor.getInt(9));
            interview.setSelected(cursor.getInt(11) == 1);
            interview.setAddedBy(cursor.getInt(12));
            interview.setComment(cursor.getString(13));
            interview.setCommitment(cursor.getInt(14));
            interview.setDateAdded(cursor.getLong(15));
            interview.setSynced(cursor.getInt(16));
            interview.setCanJoin(cursor.getInt(17) == 1);
            interview.setCountry(cursor.getString(18));

            interviewList.add(interview);
        }

        db.close();

        return interviewList;
    }

    public JSONObject getInterviewJson() {

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
}

