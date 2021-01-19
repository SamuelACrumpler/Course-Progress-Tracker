package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Assessments;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Broadcaster;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Courses;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.DatabaseReaderDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE=101;
    List<AlarmManager> lstAlarm = new ArrayList<>();
    List<PendingIntent> lstPend = new ArrayList<>();
    AlarmManager AlarmMan;

    private Button btnCourses;
    private Button btnTerms;
    private Button btnMentors;
    private Button btnProgress;

    /*
    * The notifications for the courses and assessments will trigger at 1PM on the date that it is set.
    * They will continue to trigger when visiting the main page if they are not turned off.
    *
    * */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCourses = (Button) findViewById(R.id.btnCourses);
        btnTerms  = (Button) findViewById(R.id.btnTerms);
        btnMentors  = (Button) findViewById(R.id.btnMentors);
        btnProgress = (Button) findViewById(R.id.btnProgress);
        AlarmMan = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        btnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {

                    NextAct(0);
                }
            });


        btnCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NextAct(1);
            }
        });

        btnMentors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NextAct(2);
            }
        });

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NextAct();
            }
        });


        //Progress Tracker will need an if statement to change the functionality of the NextAct function.
    }


    @Override
    public void onResume(){
        super.onResume();
        //setAlarms();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.actAddTerm) {
            AddTerm();
        } else if(id == R.id.actAddCourse){
            AddCourse();

        } else if(id == R.id.actAddMentor){
            AddMentor();
        }

        return super.onOptionsItemSelected(item);
    }


    private void setAlarms(){
        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);
        lstAlarm.clear();
        lstPend.clear();
        List<Courses> lstC = db.getAllCourses();
        List<Assessments> lstA = db.getAllAssessments();
        Calendar cal = Calendar.getInstance();
        Intent intent=new Intent(this, Broadcaster.class);
        int sCount = 0;

        for (Courses c: lstC){

            if(c.isChkStart().equals("true")){
                Calendar cCal = Calendar.getInstance();
                cal = Calendar.getInstance();
                String[] splt = c.getStart().split("-");
                cal.set(Integer.parseInt(splt[2]),(Integer.parseInt(splt[1])-1),Integer.parseInt(splt[0]),0,1*sCount,0);

                intent=new Intent(this, Broadcaster.class);
                Bundle b = new Bundle();
                b.putString("message","This course has started :" + c.getTitle());
                intent.putExtras(b);
                int id = (int) System.currentTimeMillis();
                PendingIntent pi = PendingIntent.getBroadcast(this, id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                lstPend.add(pi);

            }
        }///course start check loop end


        for (Courses c: lstC){
            if(c.isChkEnd().equals("true")){
                cal = Calendar.getInstance();
                String[] splt = c.getEnd().split("-");
                cal.set(Integer.parseInt(splt[2]),(Integer.parseInt(splt[1])-1),Integer.parseInt(splt[0]),0,1*sCount,0);
                intent=new Intent(this, Broadcaster.class);
                Bundle b = new Bundle();
                b.putString("message","This course has ended :" + c.getTitle());
                intent.putExtras(b);
                int id = (int) System.currentTimeMillis();
                PendingIntent pi = PendingIntent.getBroadcast(this, id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                lstPend.add(pi);
            }
        }//Course start loop enbd

        for (Assessments c: lstA){
            if(!c.getGoal().isEmpty()){
                cal = Calendar.getInstance();
                String[] splt = c.getGoal().split("-");
                //Month-Day-Year 0-1-2
                cal.set(Integer.parseInt(splt[2]),(Integer.parseInt(splt[1])-1),Integer.parseInt(splt[0]),0,1*sCount,0);
                intent=new Intent(this, Broadcaster.class);
                Bundle b = new Bundle();
                b.putString("message","This assessment goal date has passed :" + c.getName());
                intent.putExtras(b);
                int id = (int) System.currentTimeMillis();
                PendingIntent pi = PendingIntent.getBroadcast(this, id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                lstPend.add(pi);
            }

        }
    }

    private void NextAct(int s){
        Intent i = new Intent(this, ListActivity.class);
        Bundle b = new Bundle();
        b.putInt("key", s); //Your id
        i.putExtras(b); //Put your id to your next Intent
        startActivity(i);

    }

    private void NextAct(){
        Intent i = new Intent(this, Tracker.class);
        startActivity(i);

    }

    private void AddTerm(){
        Intent i = new Intent(this, mod_term.class);
        Bundle b = new Bundle();
        b.putInt("key", -1); //Your id
        i.putExtras(b); //Put your id to your next Intent
        startActivity(i);
    }

    private void AddCourse(){
        Intent i = new Intent(this, ModCourse.class);
        Bundle b = new Bundle();
        b.putInt("key", -1); //Your id
        i.putExtras(b); //Put your id to your next Intent
        startActivity(i);
    }

    private void AddMentor(){
        Intent i = new Intent(this, mod_mentor.class);
        Bundle b = new Bundle();
        b.putInt("key", -1); //Your id
        i.putExtras(b); //Put your id to your next Intent
        startActivity(i);
    }
}
