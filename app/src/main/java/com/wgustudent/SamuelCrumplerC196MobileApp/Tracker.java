package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Assessments;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Courses;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.DatabaseReaderDbHelper;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Terms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Tracker extends AppCompatActivity {
    List<Terms> lstTerms = new ArrayList<>();
    List<Courses> lstCourses = new ArrayList<>();
    List<Assessments> lstAssess = new ArrayList<>();

    List<String> lstTToPost = new ArrayList<>();
    List<String> lstCToPost = new ArrayList<>();
    List<String> lstAToPost = new ArrayList<>();


    int countPlan;
    int countProg;
    int countComp;
    int countDrop;



    TextView txtPlanned;
    TextView txtProgress;
    TextView txtCompleted;
    TextView txtDropped;

    TextView txtCompProg;


    ListView lstUpCor;
    ListView lstUpAssess;
    ListView lstUpTerms;


    ProgressBar proCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtPlanned = (TextView) findViewById(R.id.txtTraPlanned);
        txtProgress = (TextView) findViewById(R.id.txtTraProgress);
        txtCompleted = (TextView) findViewById(R.id.txtTraCompleted);
        txtDropped = (TextView) findViewById(R.id.txtTraDropped);

        txtCompProg = (TextView)findViewById(R.id.txtTraCroDesc);

        lstUpCor = (ListView) findViewById((R.id.lstTraUpCor));
        lstUpAssess = (ListView) findViewById((R.id.lstTraUpAssess));
        lstUpTerms = (ListView) findViewById((R.id.lstTraUpTerms));


        proCourse = (ProgressBar) findViewById(R.id.proCorComp);

        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        lstTerms = db.getAllTerms();
        lstCourses = db.getAllCourses();
        lstAssess = db.getAllAssessments();

        for (Terms c: lstTerms){
            Calendar cal = Calendar.getInstance();
            Calendar tcal = Calendar.getInstance();

            String[] splt = c.getStart().split("-");

            System.out.println("Date Test: " + Integer.parseInt(splt[2]) + "-" + Integer.parseInt(splt[1]) + "-" + Integer.parseInt(splt[0]));
            cal.set(Integer.parseInt(splt[2]),(Integer.parseInt(splt[1])-1),Integer.parseInt(splt[0]));
            if(cal.after(tcal))
                lstTToPost.add("Term: " +c.getTitle() + " | Starts: " + c.getStart());
        }

        for (Courses c: lstCourses){
            if(c.getStatus().equals("Plan To Take"))
                countPlan++;
            else if(c.getStatus().equals("In Progress"))
                countProg++;
            else if(c.getStatus().equals("Completed"))
                countComp++;
            else if(c.getStatus().equals("Dropped"))
                countDrop++;
            Calendar cal = Calendar.getInstance();
            Calendar tcal = Calendar.getInstance();

            String[] splt = c.getStart().split("-");

            System.out.println("Date Test: " + Integer.parseInt(splt[2]) + "-" + Integer.parseInt(splt[1]) + "-" + Integer.parseInt(splt[0]));
            cal.set(Integer.parseInt(splt[2]),(Integer.parseInt(splt[1])-1),Integer.parseInt(splt[0]));
            if(cal.after(tcal))
                lstCToPost.add("Course: " +c.getTitle() + " | Starts: " + c.getStart());
        }

        for (Assessments c: lstAssess){
            Calendar cal = Calendar.getInstance();
            Calendar tcal = Calendar.getInstance();

            String[] splt = c.getDue().split("-");

            System.out.println("Date Test: " + Integer.parseInt(splt[2]) + "-" + Integer.parseInt(splt[1]) + "-" + Integer.parseInt(splt[0]));
            cal.set(Integer.parseInt(splt[2]),(Integer.parseInt(splt[1])-1),Integer.parseInt(splt[0]));
            if(cal.after(tcal))
                lstAToPost.add("Assessment: " +c.getName() + " | Due: " + c.getDue());
        }
        System.out.println("prog " + countProg);
        txtPlanned.setText("Planned: " + countPlan);
        txtProgress.setText("In Progress: " + countProg);
        txtCompleted.setText("Completed: " + countComp);
        txtDropped.setText("Dropped: " + countDrop);
        System.out.println("Ratio is :" + Math.floor((countComp/(countDrop+countProg+countComp+countDrop+0.0))*100));
       // System.out.println("#Ratio is: " + ratio);
        int per = (int) Math.floor((countComp/(countDrop+countProg+countComp+countDrop+0.0))*100);
        txtCompProg.setText("You completion rate is: " + per + "%");
        proCourse.setProgress(per);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lstCToPost);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lstTToPost);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lstAToPost);



        lstUpCor.setAdapter(adapter);
        lstUpTerms.setAdapter(adapter2);

        lstUpAssess.setAdapter(adapter3);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.actHome){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
