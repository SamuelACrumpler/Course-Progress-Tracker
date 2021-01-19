package com.wgustudent.SamuelCrumplerC196MobileApp.classes;


public class Assessments {
    private int aId; //PK AUTOINC
    private int courseId; //FK NOT NULL
    private String name;

    private String type; //true == performance, false == objective
    private String due;
    private String goal;

    public Assessments(int aId, int courseId, String name, String type, String due, String goal) {
        this.aId = aId;
        this.courseId = courseId;
        this.name = name;
        this.type = type;
        this.due = due;
        this.goal = goal;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Assessments(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public void print(){
        System.out.println("#Asses Name: " + name);
        System.out.println("#Asses aId: " + aId);
        System.out.println("#Asses CourseId: " + courseId);
        System.out.println("#Assess Type: " +  type);
        System.out.println("#Asses Due: " + due);
        System.out.println("#Asses Goal: " + goal);

    }

    public void save(){

        //Database Logic here

    }
}
