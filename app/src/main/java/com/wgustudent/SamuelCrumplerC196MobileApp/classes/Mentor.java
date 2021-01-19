package com.wgustudent.SamuelCrumplerC196MobileApp.classes;

import java.util.ArrayList;
import java.util.List;

public class Mentor {
    private int mentorId;
    private String cIds;
    private String name;
    private String phone;
    private String email;

    public Mentor(int mentorId, String name, String phone, String email, String cIds) {
        this.mentorId = mentorId;
        this.cIds = cIds;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    
    

    public Mentor(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Mentor(){

    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public String getcIds() {
        return cIds;
    }

    public void setcIds(String cIds) {
        this.cIds = cIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void print(){
        System.out.println("#Mentor Title: " + name);
        System.out.println("#Mentor Start: " + phone);
        System.out.println("#Mentor End: " + email);

    }

    public List<String> pushList(){
        List<String> ts = new ArrayList<>();
        ts.add("Name: "+name);
        ts.add("Phone: "+phone);
        ts.add("E-Mail: "+email);
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

    public void load(int id){
        //Use the ID load information from the database

    }
}
