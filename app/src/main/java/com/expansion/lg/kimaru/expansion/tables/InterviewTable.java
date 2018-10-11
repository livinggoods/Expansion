package com.expansion.lg.kimaru.expansion.tables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.expansion.lg.kimaru.expansion.mzigos.Interview;
import com.expansion.lg.kimaru.expansion.mzigos.Recruitment;
import com.expansion.lg.kimaru.expansion.other.Constants;

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
    public static final String DATABASE_NAME= Constants.DATABASE_NAME;
    public static final int DATABASE_VERSION= Constants.DATABASE_VERSION;

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
    public static final String DATE_ADDED = "client_time";
    public static final String INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION = "interviewer_motivation";
    public static final String INTERVIEWER_ASSESSMENT_AGE = "interviewer_age";
    public static final String INTERVIEWER_ASSESSMENT_RESIDENCY = "interviewer_residency";
    public static final String INTERVIEWER_ASSESSMENT_BRAC_CHP = "interviewer_brac";
    public static final String INTERVIEWER_ASSESSMENT_QUALIFIES = "interviewer_qualifies";
    public static final String INTERVIEWER_ASSESSMENT_ABILITY_TO_READ = "interviewer_read";
    public static final String READ_AND_INTERPRET_PASSAGE = "read_and_interpret";
    String [] columns=new String[]{ID, APPLICANT, RECRUITMENT, MOTIVATION, COMMUNITY,MENTALITY,
            SELLING, HEALTH, INVESTMENT, INTERPERSONAL, TOTAL, SELECTED, ADDED_BY, COMMENT,
            COMMITMENT, DATE_ADDED, SYNCED, CANJOIN, COUNTRY, READ_AND_INTERPRET_PASSAGE,
            INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION, INTERVIEWER_ASSESSMENT_AGE, INTERVIEWER_ASSESSMENT_RESIDENCY,
            INTERVIEWER_ASSESSMENT_BRAC_CHP, INTERVIEWER_ASSESSMENT_ABILITY_TO_READ, INTERVIEWER_ASSESSMENT_QUALIFIES};

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
            + READ_AND_INTERPRET_PASSAGE + integer_field + ", "
            + INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION + integer_field + ", "
            + INTERVIEWER_ASSESSMENT_AGE + integer_field + ", "
            + INTERVIEWER_ASSESSMENT_RESIDENCY + integer_field + ", "
            + INTERVIEWER_ASSESSMENT_BRAC_CHP + integer_field + ", "
            + INTERVIEWER_ASSESSMENT_ABILITY_TO_READ + integer_field + ", "
            + INTERVIEWER_ASSESSMENT_QUALIFIES + integer_field + ", "
            + DATE_ADDED + integer_field + ", "
            + SYNCED + integer_field + ") ";

    public static final String DATABASE_DROP="DROP TABLE IF EXISTS" + TABLE_NAME;
    public static final String ADD_READ_FIELD = "ALTER TABLE " + TABLE_NAME +
            "  ADD "+ READ_AND_INTERPRET_PASSAGE + integer_field +";";

    public static final String ADD_INTERVIEWER_MOTIVATION = "ALTER TABLE " + TABLE_NAME +
            "  ADD "+ INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION + integer_field +";";

    public static final String ADD_INTERVIEWER_AGE_FIELD = "ALTER TABLE " + TABLE_NAME +
            "  ADD "+ INTERVIEWER_ASSESSMENT_AGE + integer_field +";";
    public static final String ADD_INTERVIEWER_RESIDENCY_FIELD = "ALTER TABLE " + TABLE_NAME +
            "  ADD "+ INTERVIEWER_ASSESSMENT_RESIDENCY + integer_field +";";
    public static final String ADD_INTERVIEWER_BRAC_CHP_FIELD = "ALTER TABLE " + TABLE_NAME +
            "  ADD "+ INTERVIEWER_ASSESSMENT_BRAC_CHP + integer_field +";";
    public static final String ADD_INTERVIEWER_ABILITY_TO_READ = "ALTER TABLE " + TABLE_NAME +
            "  ADD "+ INTERVIEWER_ASSESSMENT_ABILITY_TO_READ + integer_field +";";
    public static final String ADD_INTERVIEWER_QUALIFIES = "ALTER TABLE " + TABLE_NAME +
            "  ADD "+ INTERVIEWER_ASSESSMENT_QUALIFIES + integer_field +";";

    public InterviewTable(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
        if (!isFieldExist(READ_AND_INTERPRET_PASSAGE)){
            this.addReadAndinterpretField();
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION)){
            this.addInterviewerGoodMotivation();
        }

        if (!isFieldExist(INTERVIEWER_ASSESSMENT_AGE)){
            this.addInterviewerAge();
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_RESIDENCY)){
            this.addInterviewerResidency();
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_BRAC_CHP)){
            this.addInterviewerBracChp();
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_ABILITY_TO_READ)){
            this.addInterviewerAbilityToRead();
        }
        if (!isFieldExist(INTERVIEWER_ASSESSMENT_QUALIFIES)){
            this.addInterviewerQualifies();
        }
    }
    public void addReadAndinterpretField(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(ADD_READ_FIELD);
    }
    public void addInterviewerGoodMotivation(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(ADD_INTERVIEWER_MOTIVATION);
    }
    public void addInterviewerAge(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(ADD_INTERVIEWER_AGE_FIELD);
    }
    public void addInterviewerResidency(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(ADD_INTERVIEWER_RESIDENCY_FIELD);
    }
    public void addInterviewerBracChp(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(ADD_INTERVIEWER_BRAC_CHP_FIELD);
    }
    public void addInterviewerAbilityToRead(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(ADD_INTERVIEWER_ABILITY_TO_READ);
    }

    public void addInterviewerQualifies(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(ADD_INTERVIEWER_QUALIFIES);
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

    public long getAllRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                null,
                null);
        db.close();
        return cnt;
    }

    public long getPendingRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME,
                SYNCED + "=?",
                new String[] {String.valueOf(Constants.SYNC_STATUS_UNSYNCED)});
        db.close();
        return cnt;
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
        cv.put(SELECTED, interview.getSelected());
        cv.put(CANJOIN, interview.isCanJoin() ? 1 : 0);
        cv.put(ADDED_BY, interview.getAddedBy());
        cv.put(COMMENT, interview.getComment());
        cv.put(COMMITMENT, interview.getCommitment());
        cv.put(DATE_ADDED, interview.getDateAdded());
        cv.put(SYNCED, interview.getSynced());
        cv.put(READ_AND_INTERPRET_PASSAGE, interview.getReadAndInterpret());
        cv.put(INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION, interview.getInterviewerMotivationAssessment());
        cv.put(INTERVIEWER_ASSESSMENT_AGE, interview.getInterviewerAgeAssessment());
        cv.put(INTERVIEWER_ASSESSMENT_RESIDENCY, interview.getInterviewerResidenyAssessment());
        cv.put(INTERVIEWER_ASSESSMENT_BRAC_CHP, interview.getInterviewerBracAssessment());
        cv.put(INTERVIEWER_ASSESSMENT_ABILITY_TO_READ, interview.getInterviewerAbilityToReadAssessment());
        cv.put(INTERVIEWER_ASSESSMENT_QUALIFIES, interview.getInterviewerQualifyAssessment());

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
            Interview interview = cursorToInterview(cursor);
            interviewList.add(interview);
        }
        cursor.close();
        return interviewList;
    }
    public Cursor getInterviewDataCursor() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        db.close();
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

            Interview interview = cursorToInterview(cursor);
            cursor.close();
            return interview;
        }
    }

    public Interview getInterviewById (String id){
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                id,
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            Interview interview = cursorToInterview(cursor);
            cursor.close();
            return interview;
        }
    }

    //getInterviewById

    public List<Interview> getInterviewsByRecruitment(Recruitment recruitment) {

        SQLiteDatabase db=getReadableDatabase();
        String orderBy = DATE_ADDED + " desc";
        String whereClause = RECRUITMENT+" = ?";
        String[] whereArgs = new String[] {
                recruitment.getId(),
        };
        Cursor cursor=db.query(TABLE_NAME,columns,whereClause,whereArgs,null,null,orderBy,null);
        List<Interview> interviewList=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            Interview interview = cursorToInterview(cursor);
            interviewList.add(interview);
        }
        db.close();
        return interviewList;
    }

    public void fromJson(JSONObject jsonObject){
        Interview interview = new Interview();
        try {
            interview.setId(jsonObject.getString(InterviewTable.ID));
            interview.setApplicant(jsonObject.getString(InterviewTable.APPLICANT));
            interview.setRecruitment(jsonObject.getString(InterviewTable.RECRUITMENT));
            interview.setMotivation(jsonObject.getInt(InterviewTable.MOTIVATION));
            interview.setCommunity(jsonObject.getInt(InterviewTable.COMMUNITY));
            interview.setMentality(jsonObject.getInt(InterviewTable.MENTALITY));
            interview.setSelling(jsonObject.getInt(InterviewTable.SELLING));
            interview.setHealth(jsonObject.getInt(InterviewTable.HEALTH));
            interview.setInvestment(jsonObject.getInt(InterviewTable.INVESTMENT));
            interview.setCountry(jsonObject.getString(InterviewTable.COUNTRY));
            interview.setInterpersonal(jsonObject.getInt(InterviewTable.INTERPERSONAL));
            interview.setSelected(jsonObject.getInt(SELECTED));
            interview.setAddedBy(jsonObject.getInt(InterviewTable.ADDED_BY));
            interview.setComment(jsonObject.getString(InterviewTable.COMMENT));
            interview.setCommitment(jsonObject.getInt(InterviewTable.COMMITMENT));
            interview.setDateAdded(jsonObject.getLong(InterviewTable.DATE_ADDED));
            interview.setSynced(jsonObject.getInt(InterviewTable.SYNCED));
            interview.setCanJoin(jsonObject.getInt(InterviewTable.CANJOIN) == 1);
            interview.setReadAndInterpret(jsonObject.getInt(READ_AND_INTERPRET_PASSAGE));
            interview.setInterviewerMotivationAssessment(jsonObject.getInt(INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION));
            interview.setInterviewerAgeAssessment(jsonObject.getInt(INTERVIEWER_ASSESSMENT_AGE));
            interview.setInterviewerResidenyAssessment(jsonObject.getInt(INTERVIEWER_ASSESSMENT_RESIDENCY));
            interview.setInterviewerBracAssessment(jsonObject.getInt(INTERVIEWER_ASSESSMENT_BRAC_CHP));
            interview.setInterviewerAbilityToReadAssessment(jsonObject.getInt(INTERVIEWER_ASSESSMENT_ABILITY_TO_READ));
            interview.setInterviewerQualifyAssessment(jsonObject.getInt(INTERVIEWER_ASSESSMENT_QUALIFIES));
            // add six fields
            this.addData(interview);
        }catch (Exception e){}
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
            } catch (JSONException e) {}
        }
        cursor.close();
        db.close();
        return results;
    }

    public JSONObject getInterviewsToSyncAsJson(int offset) {
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
            } catch (JSONException e) {}
        }
        cursor.close();
        db.close();
        return results;
    }
    private Interview cursorToInterview(Cursor cursor){
        Interview interview = new Interview();
        interview.setId(cursor.getString(cursor.getColumnIndex(ID)));
        interview.setApplicant(cursor.getString(cursor.getColumnIndex(APPLICANT)));
        interview.setRecruitment(cursor.getString(cursor.getColumnIndex(RECRUITMENT)));
        interview.setMotivation(cursor.getInt(cursor.getColumnIndex(MOTIVATION)));
        interview.setCommunity(cursor.getInt(cursor.getColumnIndex(COMMUNITY)));
        interview.setMentality(cursor.getInt(cursor.getColumnIndex(MENTALITY)));
        interview.setSelling(cursor.getInt(cursor.getColumnIndex(SELLING)));
        interview.setHealth(cursor.getInt(cursor.getColumnIndex(HEALTH)));
        interview.setInvestment(cursor.getInt(cursor.getColumnIndex(INVESTMENT)));
        interview.setInterpersonal(cursor.getInt(cursor.getColumnIndex(INTERPERSONAL)));
        interview.setSelected(cursor.getInt(cursor.getColumnIndex(SELECTED)));
        interview.setAddedBy(cursor.getInt(cursor.getColumnIndex(ADDED_BY)));
        interview.setComment(cursor.getString(cursor.getColumnIndex(COMMENT)));
        interview.setCommitment(cursor.getInt(cursor.getColumnIndex(COMMITMENT)));
        interview.setDateAdded(cursor.getLong(cursor.getColumnIndex(DATE_ADDED)));
        interview.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED)));
        interview.setCanJoin(cursor.getInt(cursor.getColumnIndex(CANJOIN)) == 1);
        interview.setReadAndInterpret(cursor.getInt(cursor.getColumnIndex(READ_AND_INTERPRET_PASSAGE)));
        interview.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
        interview.setReadAndInterpret(cursor.getInt(cursor.getColumnIndex(READ_AND_INTERPRET_PASSAGE)));
        interview.setInterviewerMotivationAssessment(cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_GOOD_MOTIVATION)));
        interview.setInterviewerAgeAssessment(cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_AGE)));
        interview.setInterviewerResidenyAssessment(cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_RESIDENCY)));
        interview.setInterviewerBracAssessment(cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_BRAC_CHP)));
        interview.setInterviewerAbilityToReadAssessment(cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_ABILITY_TO_READ)));
        interview.setInterviewerQualifyAssessment(cursor.getInt(cursor.getColumnIndex(INTERVIEWER_ASSESSMENT_QUALIFIES)));
        return interview;
    }

    public boolean isFieldExist(String fieldName)
    {
        SQLiteDatabase db = getReadableDatabase();
        boolean isExist = false;
        Cursor res = null;
        try {
            res = db.rawQuery("Select * from "+ TABLE_NAME +" limit 1", null);
            int colIndex = res.getColumnIndex(fieldName);
            if (colIndex!=-1){
                isExist = true;
            }else{
                Log.d("Tremap", "The col "+fieldName+" is NOT found");
            }
        } catch (Exception e) {
            Log.d("Tremap", "Error getting  "+fieldName);
        } finally {
            try {
                if (res !=null){ res.close();}
            } catch (Exception e1) {}
        }
        return isExist;
    }
    private void upgradeVersion2(SQLiteDatabase db) {}
}

