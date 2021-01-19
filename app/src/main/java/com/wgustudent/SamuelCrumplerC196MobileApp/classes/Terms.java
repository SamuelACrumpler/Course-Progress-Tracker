package com.wgustudent.SamuelCrumplerC196MobileApp.classes;

import java.util.ArrayList;
import java.util.List;

public class Terms {
    private int id;
    private String title;
    private String start;
    private String end;
    private String cIds;
    //Store courseIds with a String that is separated by Pipes(|)


    public Terms(int id, String title, String start, String end, String cIds) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.cIds = cIds;
    }

    public Terms(String title, String start, String end, String cIds) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.cIds = cIds;
    }

    public Terms(String title, String start, String end, List<String> cIds) {
        this.title = title;
        this.start = start;
        this.end = end;
        combine(cIds);
        print();
    }

    public Terms(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getcIds() {
        return cIds;
    }

    public void setcIds(String cIds) {
        this.cIds = cIds;
    }

    public void print(){
        System.out.println("#Term ID: " + id);

        System.out.println("#Term Title: " + title);
        System.out.println("#Term Start: " + start);
        System.out.println("#Term End: " + end);
        System.out.println("#Term CourseIds: " + cIds);

    }

    public List<String> pushList(){
        List<String> ts = new ArrayList<>();
        ts.add(title);
        ts.add(start);
        ts.add(end);
        String[] sA = split();
        for (String s: sA){
            if(!s.isEmpty())
                ts.add(s);
        }

        //Parse list of courseIds
        //Pull them and add into the string using "Course: + id"
        return ts;
    }

    public String[] split(){
        String[] st = {""};
        if(cIds == null)
            return st;

        String[] values = cIds.split(",");


        for (String s :values){
            System.out.println("#" + s);
        }

        return values;
    }

    public void combine(List<String> lst){
        cIds = "";
        for (String s: lst){
            cIds = cIds + s + ",";
        }
    }

    public void save(int id){

    }

    public void load (int id){


    }
}
