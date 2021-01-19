package com.wgustudent.SamuelCrumplerC196MobileApp.classes;

import android.database.sqlite.*;

public class Query {
    private static String query = null;
    //private static Statement stmt = null;
    ///private static ResultSet result = null;



    public static void makeQuery(String q) {
        query = q;
        try
        {

            //stmt = conn.createStatement();

            if(query.toLowerCase().startsWith("select")){
            //    result = stmt.executeQuery(query);

            }

            if(query.toLowerCase().startsWith("delete") || query.toLowerCase().startsWith("insert") || query.toLowerCase().startsWith("update")){

            //    stmt.executeUpdate(query);

            }

        }catch (Exception e){
            System.out.println("Error Message: " + e.getMessage());



        }


    }
   // public static ResultSet getResult()
   // {
    //    return result;
   // }
}
