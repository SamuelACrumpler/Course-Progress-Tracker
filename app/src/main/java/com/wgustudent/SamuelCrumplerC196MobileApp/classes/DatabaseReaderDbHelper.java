package com.wgustudent.SamuelCrumplerC196MobileApp.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseReaderDbHelper extends SQLiteOpenHelper {



    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "studentScheduler.db";
    public static final String  COURSE_TABLE = "courses";
    public static final String  TERM_TABLE = "terms";
    public static final String  MENTOR_TABLE = "mentors";
    public static final String  ASSESSMENT_TABLE = "assessments";




    public DatabaseReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + COURSE_TABLE + "(courseID INTEGER PRIMARY KEY AUTOINCREMENT, mentorID INTEGER, title VARCHAR(255) NOT NULL, startDate DATE NOT NULL, endDate DATE NOT NULL, status VARCHAR(255) NOT NULL, notes VARCHAR(6000), startchk VARCHAR(255) NOT NULL, endchk VARCHAR(255) NOT NULL" +
                ", FOREIGN KEY (mentorID) REFERENCES mentors(mentorID))");

        db.execSQL("CREATE TABLE " + TERM_TABLE + "(termID INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR(255) NOT NULL, startDate DATE NOT NULL, endDate DATE NOT NULL, courseIds VARCHAR(6000))");
        db.execSQL("CREATE TABLE " + MENTOR_TABLE + "(mentorID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255) NOT NULL, phone VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, courseIds VARCHAR(6000))");
        db.execSQL("CREATE TABLE " + ASSESSMENT_TABLE + "(assessmentID INTEGER PRIMARY KEY AUTOINCREMENT, courseID INTEGER, name VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, due DATE NOT NULL, goal DATE, FOREIGN KEY (courseID) REFERENCES courses(courseID))");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TERM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MENTOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ASSESSMENT_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Courses - 3rd

    public void addCourse(Courses c){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mentorID",c.getMentorId());
        values.put("title",c.getTitle());
        values.put("startDate",c.getStart());
        values.put("endDate",c.getEnd());
        values.put("status",c.getStatus());
        values.put("notes",c.getNotes());
        values.put("startchk",c.isChkStart());
        values.put("endchk",c.isChkEnd());


        sqLiteDatabase.insert(COURSE_TABLE, null, values);
        sqLiteDatabase.close();
    }

    public Courses getCourse(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(COURSE_TABLE, new String[]
                //put in id section, but skip when filling out information
                { "courseID","mentorID", "title", "startDate","endDate","status","notes","startchk","endchk" }, "courseID" + "=?", new String[]
                { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) { cursor.moveToFirst(); }
        Courses course = new Courses(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6), cursor.getString(7),cursor.getString(8));
        return course;
    }

    public int updateCourse(Courses c) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mentorID",c.getMentorId());
        values.put("title",c.getTitle());
        values.put("startDate",c.getStart());
        values.put("endDate",c.getEnd());
        values.put("status",c.getStatus());
        values.put("notes",c.getNotes());
        values.put("startchk",c.isChkStart());
        values.put("endchk",c.isChkEnd());
        return sqLiteDatabase.update(COURSE_TABLE, values, "courseID" +
                " = ?", new String[]{String.valueOf(c.getCourseId())});
    }

    public void deleteCourse(Courses c) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(COURSE_TABLE, "courseID" + " = ?",
                new String[] { String.valueOf(c.getCourseId())});
        sqLiteDatabase.close();
    }

    public List<Courses> getAllCourses() {
        List<Courses> CoursesList = new ArrayList<Courses>();
        String selectQuery = "SELECT  * FROM " + COURSE_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Courses c = new Courses();
                c.setCourseId(Integer.parseInt(cursor.getString(0)));
                c.setMentorId(Integer.parseInt(cursor.getString(1)));
                c.setTitle(cursor.getString(2));
                c.setStart(cursor.getString(3));
                c.setEnd(cursor.getString(4));
                c.setStatus(cursor.getString(5));
                c.setNotes(cursor.getString(6));
                c.setChkStart(cursor.getString(7));
                c.setChkEnd(cursor.getString(8));
                CoursesList.add(c);
            } while (cursor.moveToNext());

        }
        return CoursesList;

    }


    //Terms - 1st

    public void addTerm(Terms t){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",t.getTitle());
        values.put("startDate",t.getStart());
        values.put("endDate",t.getEnd());
        values.put("courseIds",t.getcIds());
        sqLiteDatabase.insert(TERM_TABLE, null, values);
        sqLiteDatabase.close();
    }

    public Terms getTerm(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TERM_TABLE, new String[]
                //put in id section, but skip when filling out information
                { "termID", "title", "startDate","endDate","courseIds" }, "termID" + "=?", new String[]
                { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) { cursor.moveToFirst(); }
        Terms term = new Terms(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
        return term;
    }

    public int updateTerm(Terms t) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",t.getTitle());
        values.put("startDate",t.getStart());
        values.put("endDate",t.getEnd());
        values.put("courseIds",t.getcIds());
        return sqLiteDatabase.update(TERM_TABLE, values, "termID" +
                " = ?", new String[]{String.valueOf(t.getId())});
    }

    public void deleteTerm(Terms t) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TERM_TABLE, "termID" + " = ?",
                new String[] { String.valueOf(t.getId())});
        sqLiteDatabase.close();
    }

    public List<Terms> getAllTerms() {
        List<Terms> TermsList = new ArrayList<Terms>();
        String selectQuery = "SELECT  * FROM " + TERM_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Terms t = new Terms();
                t.setId(Integer.parseInt(cursor.getString(0)));
                t.setTitle(cursor.getString(1));
                t.setStart(cursor.getString(2));
                t.setEnd(cursor.getString(3));
                t.setcIds(cursor.getString(4));
                TermsList.add(t);
            } while (cursor.moveToNext());

        }
        return TermsList;

    }

    public int getTermCount() {
        String countQuery = "SELECT * FROM " + TERM_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        cursor.close();
        sqLiteDatabase.close();
        return cursor.getCount();
    }

    //Mentors - 2nd

    public void addMentor(Mentor m){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courseIds",m.getcIds());
        values.put("name",m.getName());
        values.put("phone",m.getPhone());
        values.put("email",m.getEmail());
        sqLiteDatabase.insert(MENTOR_TABLE, null, values);
        sqLiteDatabase.close();
    }

    public Mentor getMentor(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(MENTOR_TABLE, new String[]
                //put in id section, but skip when filling out information
                { "mentorID", "name", "phone","email","courseIds" }, "mentorID" + "=?", new String[]
                { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) { cursor.moveToFirst(); }
        Mentor term = new Mentor(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
        return term;
    }

    public int updateMentor(Mentor m) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courseIds",m.getcIds());
        values.put("name",m.getName());
        values.put("phone",m.getPhone());
        values.put("email",m.getEmail());
        return sqLiteDatabase.update(MENTOR_TABLE, values, "mentorID" +
                " = ?", new String[]{String.valueOf(m.getMentorId())});
    }

    public void deleteMentor(Mentor m) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(MENTOR_TABLE, "mentorID" + " = ?",
                new String[] { String.valueOf(m.getMentorId())});
        sqLiteDatabase.close();
    }

    public List<Mentor> getAllMentors() {
        List<Mentor> MentorsList = new ArrayList<Mentor>();
        String selectQuery = "SELECT  * FROM " + MENTOR_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Mentor m = new Mentor();
                m.setMentorId(Integer.parseInt(cursor.getString(0)));
                m.setName(cursor.getString(1));
                m.setPhone(cursor.getString(2));
                m.setEmail(cursor.getString(3));
                m.setcIds(cursor.getString(4));
                MentorsList.add(m);
            } while (cursor.moveToNext());

        }
        return MentorsList;

    }


    //Assessments 4th

    public void addAssessment(Assessments a){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courseId",a.getCourseId());
        values.put("name",a.getName());
        values.put("type",a.getType());
        values.put("due",a.getDue());
        values.put("goal",a.getGoal());
        sqLiteDatabase.insert(ASSESSMENT_TABLE, null, values);
        sqLiteDatabase.close();
    }

    public Assessments getAssessment(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(ASSESSMENT_TABLE, new String[]
                //put in id section, but skip when filling out information
                { "assessmentID", "courseID", "name","type","due","goal" }, "assessmentID" + "=?", new String[]
                { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) { cursor.moveToFirst(); }
        Assessments assess = new Assessments(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
        return assess;
    }

    public int updateAssessment(Assessments a) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courseId",a.getCourseId());
        values.put("name",a.getName());
        values.put("type",a.getType());
        values.put("due",a.getDue());
        values.put("goal",a.getGoal());
        return sqLiteDatabase.update(ASSESSMENT_TABLE, values, "assessmentID" +
                " = ?", new String[]{String.valueOf(a.getaId())});
    }

    public void deleteAssessment(Assessments a) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(ASSESSMENT_TABLE, "assessmentID" + " = ?",
                new String[] { String.valueOf(a.getaId())});
        sqLiteDatabase.close();
    }

    public List<Assessments> getAllAssessments() {
        List<Assessments> AssessmentsList = new ArrayList<Assessments>();
        String selectQuery = "SELECT  * FROM " + ASSESSMENT_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Assessments a = new Assessments();
                a.setaId(Integer.parseInt(cursor.getString(0)));
                a.setCourseId(Integer.parseInt(cursor.getString(1)));
                a.setName(cursor.getString(2));
                a.setType(cursor.getString(3));
                a.setDue(cursor.getString(4));
                a.setGoal(cursor.getString(5));
                AssessmentsList.add(a);
            } while (cursor.moveToNext());

        }
        return AssessmentsList;

    }

    /*public boolean addData(String n)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put("name", n);
        long result = db.insert(TABLE_NAME,null,cValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean addTerm(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        long result = db.insert(TABLE_NAME,null,cValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getdata()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;

    }*/
}


