package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Assessments;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Broadcaster;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Courses;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.DatabaseReaderDbHelper;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Mentor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ModCourse extends AppCompatActivity {

    final Calendar c = Calendar.getInstance();
    private int cYear = c.get(Calendar.YEAR);
    private int cMon = c.get(Calendar.MONTH);
    private int cDay = c.get(Calendar.DAY_OF_MONTH);

    private int wid = -1;
    private Courses wC;

    EditText txtTitle;
    TextView txtStart;
    TextView txtEnd;
    Spinner spnStatus;
    Spinner spnMentor;
    EditText txtNotes;
    Button btnAdd;
    CheckBox chkStart;
    CheckBox chkEnd;
    List<Mentor> mentors = new ArrayList<>();
    List<Integer> mentorIds = new ArrayList<>();
    List<Assessments> assessList = new ArrayList<>();
    AlarmManager AlarmMan;

    //List for assessment is populated by the database


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);




        Bundle b = getIntent().getExtras();
        wid = b.getInt("key");
        System.out.println("I got the position: " + wid);

        txtTitle = (EditText) findViewById(R.id.txtCorTitle);
        txtStart = (TextView) findViewById(R.id.txtCorStart);
        txtEnd = (TextView) findViewById(R.id.txtCorEnd);
        spnMentor = (Spinner) findViewById(R.id.spnCorMentor);
        spnStatus = (Spinner) findViewById(R.id.spnCorStatus);
        txtNotes = (EditText) findViewById(R.id.txtCorNotes);
        btnAdd = (Button) findViewById(R.id.btnCorAdd);
        chkStart = (CheckBox) findViewById(R.id.chkCorStart);
        chkEnd = (CheckBox) findViewById(R.id.chkCorEnd);
        AlarmMan = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        String mName = "";
        List<String> ml = new ArrayList<>();
        //Populating data
        if(wid > 0) {//Editing
            wC = db.getCourse(wid);
            txtTitle.setText(wC.getTitle());
            txtStart.setText(wC.getStart());
            txtEnd.setText(wC.getEnd());
            txtNotes.setText(wC.getNotes());
            if(wC.isChkStart().equals("true"))
                chkStart.setChecked(true);
            else
                chkStart.setChecked(false);
            if(wC.isChkEnd().equals("true"))
                chkEnd.setChecked(true);
            else
                chkEnd.setChecked(false);

            for (Mentor m : db.getAllMentors()) {
                ml.add(m.getName());
                mentorIds.add(m.getMentorId());
                if (m.getMentorId() == wC.getMentorId())
                    mName = m.getName();
            }
        } else if (wid <= 0){//new
            for (Mentor m : db.getAllMentors()) {
                ml.add(m.getName());
                mentorIds.add(m.getMentorId());
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, ml);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Set the proper name for the spinner here
        spnMentor.setSelection(adapter.getPosition(mName));
        spnMentor.setAdapter(adapter);
        //Set the proper name of the progress here
        if (wid > 0)
        spnStatus.setSelection(((ArrayAdapter)spnStatus.getAdapter()).getPosition(wC.getStatus()));

        refresh();



        txtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setStartDate();
            }
        });
        txtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEndDate();
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wid > 0)
                    NextAct(wid,-1);
                else
                    Toast.makeText(getApplicationContext(), "Please add the course before attempting to add assessments.", Toast.LENGTH_LONG).show(); return;

            }
        });

        FloatingActionButton fabSave = findViewById(R.id.fabCorSave);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();

            }
        });



    }// end OnCreate




    @Override
    public void onResume(){
        super.onResume();
        System.out.println("#This is a continue");
        //Use this function to refresh the assessment list.
        refresh();

    }

    private void NextAct(int c, int a){
        Intent i = new Intent(this, ModAssessment.class);
        Bundle b2 = new Bundle();
        Bundle b3 = new Bundle();
        b2.putInt("cid", c); //Your id
        b3.putInt("aid", a);

        i.putExtras(b2); //Put your id to your next Intent
        i.putExtras(b3);
        startActivity(i);
    };


    private void refresh(){
        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        assessList.clear();

        for (Assessments a : db.getAllAssessments()){
            if(a.getCourseId() == wid)
                assessList.add(a);
        }

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, assessList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(assessList.get(position).getType()+ " : " +assessList.get(position).getName()) ;
                text2.setText("Due Date : " + assessList.get(position).getDue());
                text2.setTextSize(10);
                return view;
            }
        };//End Adapter Array

        final ListView listView = (ListView) findViewById(R.id.lstMC);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("This is the position: " + (position));
                NextAct(wid, assessList.get(position).getaId());
            }
        });
        listView.setAdapter(adapter2);
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

    private void setStartDate(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //Save as a string instead, then return it??
                        txtStart.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, cYear, cMon, cDay);
        datePickerDialog.show();


    }

    private void setEndDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //Save as a string instead, then return it??
                        txtEnd.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, cYear, cMon, cDay);
        datePickerDialog.show();
    }


    private void save(){
        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        if(txtTitle.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Error: Name is empty. Enter name before saving!", Toast.LENGTH_LONG).show(); return;}
        else if(txtStart.getText().toString().isEmpty())
        {Toast.makeText(getApplicationContext(), "Error: Start date is empty. Enters start date before saving!", Toast.LENGTH_LONG).show(); return;}
        else if(txtEnd.getText().toString().isEmpty())
        {Toast.makeText(getApplicationContext(), "Error: End date is empty. Enter end date before saving!", Toast.LENGTH_LONG).show(); return;}
        else if(spnMentor.getSelectedItemPosition() < 0)
        {Toast.makeText(getApplicationContext(), "Error: Choose mentor before saving!", Toast.LENGTH_LONG).show(); return;}
        else if(spnStatus.getSelectedItemPosition() < 0)
        {Toast.makeText(getApplicationContext(), "Error: Choose status before saving!", Toast.LENGTH_LONG).show(); return;}


            //Old vs New Mentor check, remove digit from one location, place in the other


        String s = spnStatus.getSelectedItem().toString();
        int m = mentorIds.get(spnMentor.getSelectedItemPosition());
        Mentor tnm;
        //If not new, and if the mentor is different
        if(wid > 0 && wC != null && m != wC.getMentorId()){
            db.getMentor(wC.getMentorId()).print();
            String[] old = db.getMentor(wC.getMentorId()).split();
            List<String> oldl = new ArrayList<>();
            String[] newIds =  db.getMentor(m).split();
            List<String> newl = new ArrayList<>();

            for (String str: old){
                if(!(wC.getMentorId()+"").equals(str))
                    oldl.add(str);
            }
            for (String str: newIds){
                newl.add(str);
            }
            //old
            //pull mentor
            Mentor tm = db.getMentor(wC.getMentorId());

            //call combine
            tm.combine(oldl);
            //update mentor
            db.updateMentor(tm);
            //This will clear the mentor id

            //new
            //pull mentor
             tnm = db.getMentor(m);

            tnm.combine(newl);
            //add id to list
            db.updateMentor(tnm);
            //update mentor
        } else {
            tnm = db.getMentor(m);
            String[] newIds = tnm.split();
            List<String> newl = new ArrayList<>();
            for (String str: newIds){
                newl.add(str);
            }
            newl.add(m+"");
            db.updateMentor(tnm);

        }

        String start;
        String end;
        if(chkStart.isChecked())
            start = "true";
         else
            start = "false";

         if(chkEnd.isChecked())
             end = "true";
         else
             end = "false";

         System.out.println("This is start: " + start);
        System.out.println("This is end: " + end);




        Courses nC = new Courses(wid,m, txtTitle.getText().toString(),txtStart.getText().toString(),txtEnd.getText().toString(),s,txtNotes.getText().toString(),start, end);
        nC.print();
        if(wid <= 0)
            db.addCourse(nC);
        else if(wid > 0)
            db.updateCourse(nC);

        if(chkStart.isChecked())
            alertStart(nC);
        if(chkEnd.isChecked())
            alertEnd(nC);

        ModCourse.this.finish();

    }

    public void alertStart(Courses c){
        Calendar cal = Calendar.getInstance();
        cal = Calendar.getInstance();
        String[] splt = c.getStart().split("-");
        cal.set(Integer.parseInt(splt[2]),(Integer.parseInt(splt[1])-1),Integer.parseInt(splt[0]),0,0,0);

        Intent intent=new Intent(this, Broadcaster.class);
        Bundle b = new Bundle();
        b.putString("message","This course has started :" + c.getTitle());
        intent.putExtras(b);
        int id = (int) System.currentTimeMillis();
        System.out.println("milisection test: " + cal.getTimeInMillis());
        PendingIntent pi = PendingIntent.getBroadcast(this, id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

    }

    public void alertEnd(Courses c){
        Calendar cal = Calendar.getInstance();
        cal = Calendar.getInstance();
        String[] splt = c.getEnd().split("-");
        cal.set(Integer.parseInt(splt[2]),(Integer.parseInt(splt[1])-1),Integer.parseInt(splt[0]),0,0,0);

        Intent intent=new Intent(this, Broadcaster.class);
        Bundle b = new Bundle();
        b.putString("message","This course has ended :" + c.getTitle());
        intent.putExtras(b);
        int id = (int) System.currentTimeMillis();
        PendingIntent pi = PendingIntent.getBroadcast(this, id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

    }


}
