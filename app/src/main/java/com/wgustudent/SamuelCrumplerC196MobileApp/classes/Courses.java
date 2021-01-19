package com.wgustudent.SamuelCrumplerC196MobileApp.classes;

public class Courses {
    private int courseId;
    private int mentorId;
    private String title;
    private String start;
    private String end;
    private String status;
    private String notes;
    private String chkStart;
    private String chkEnd;

    private String mentor;



    public Courses(int courseId, int mentorId, String title, String start, String end, String status, String notes, String chkStart, String chkEnd) {
        this.courseId = courseId;
        this.mentorId = mentorId;
        this.title = title;
        this.start = start;
        this.end = end;
        this.status = status;
        this.notes = notes;
        this.chkStart = chkStart;
        this.chkEnd = chkEnd;
    }

    public Courses(){}

    public String isChkStart() {
        return chkStart;
    }

    public void setChkStart(String chkStart) {
        this.chkStart = chkStart;
    }

    public String isChkEnd() {
        return chkEnd;
    }

    public void setChkEnd(String chkEnd) {
        this.chkEnd = chkEnd;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    //Will need a need to assign its CourseId to assessment upon creation.
    ///Maybe have a assessment creation function


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }



    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void print(){
        System.out.println("#Course ID: " + courseId);
        System.out.println("#Course Mentor: " + mentorId);
        System.out.println("#Course Title: " + title);
        System.out.println("#Course Start: " + start);
        System.out.println("#Course End: " + end);
        System.out.println("#Course Status: " + status);
        System.out.println("#Course Notes: " + notes);
        System.out.println("#Course Start: " + chkStart);
        System.out.println("#Course End: " + chkEnd);


    }

    private void save(){
        //Database save function here

    }
}
