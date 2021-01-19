package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Courses;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.DatabaseReaderDbHelper;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Terms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class mod_term extends AppCompatActivity {
    final Calendar c = Calendar.getInstance();
    private int cYear = c.get(Calendar.YEAR);
    private int cMon = c.get(Calendar.MONTH);
    private int cDay = c.get(Calendar.DAY_OF_MONTH);


    int wid = -1;

    Terms wTerm = new Terms();
    String[] courseList = {};
    List<Courses> cl = new ArrayList<>();

    EditText txtName;
    TextView txtStart;
    TextView txtEnd;
    ListView lstCourses;
    Spinner spnCourses;
    Button btnAdd;
    int lstIndex = 0; //Work around for the alert.
    List<String> cLstList = new ArrayList<>();
    List<Integer> cLstIds = new ArrayList<>();


    List<String> cSpnList = new ArrayList<>();
    List<Integer> cSpnIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        Bundle b = getIntent().getExtras();
        wid = b.getInt("key");
        System.out.println("#The id is: " + wid);

        txtName = (EditText) findViewById(R.id.txtTermName);
        txtStart = (TextView) findViewById(R.id.txtTermStart);
        txtEnd = (TextView) findViewById(R.id.txtTermEnd);
        lstCourses = (ListView) findViewById(R.id.lstTermCourses);
        spnCourses = (Spinner) findViewById((R.id.spnCourses));
        btnAdd = (Button) findViewById(R.id.btnTermAdd);

        if(wid > 0){
            wTerm = db.getTerm(wid);
            wTerm.print();
            txtName.setText(wTerm.getTitle());
            txtStart.setText(wTerm.getStart());
            txtEnd.setText(wTerm.getEnd());
            courseList = wTerm.split();
        }

        loadCourses();
        refresh();



        lstCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lstIndex = position;
                System.out.println("This is the position: " + (position));
                alertTest();

            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fromSpnToList(spnCourses.getSelectedItemPosition());
            }
        });

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

        FloatingActionButton fab = findViewById(R.id.fabTermSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               save();
            }
        });
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


    private void fromSpnToList(int i){
        //Remove from spinner
            //Remove lettering
        //add to list
        if(i < 0)
            return;
        System.out.println("Int num:" + i);
        String tstr = cSpnList.get(i);
        int tint = cSpnIds.get(i);

        cSpnList.remove(i);
        cSpnIds.remove(i);

        cLstList.add(tstr);
        cLstIds.add(tint);


        refresh();
    }

    private void fromListToSpn(int i){
        String tstr = cLstList.get(i);
        int tint = cLstIds.get(i);

        cLstList.remove(i);
        cLstIds.remove(i);

        cSpnList.add(tstr);
        cSpnIds.add(tint);
        refresh();
    }

    private void loadCourses(){
        courseList = wTerm.split();
        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);
        List<Courses> cor = db.getAllCourses();
        List<String> list = Arrays.asList(courseList); //Contains courses from the working term
        for (Courses c: cor){
            if(list.contains(c.getCourseId()+""))
            {
                cLstList.add(c.getTitle());
                cLstIds.add(c.getCourseId());
            } else{
                cSpnList.add(c.getTitle());
                cSpnIds.add(c.getCourseId());
            }

        }


    }

    private void alertTest(){

        new AlertDialog.Builder(this)
                .setTitle("View or Remove")
                .setMessage("Do you want to view or remove this entry?")

                .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("#Delete operation test is successful!");
                        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(getApplicationContext());
                        fromListToSpn(lstIndex);
                        refresh();
                    }
                })

                .setNegativeButton(R.string.view, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NextAct(cSpnIds.get(lstIndex));
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    //Pull info from course list, in spinner
    private void refresh(){
        //reset the list and repopulate the values within

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, cSpnList);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cLstList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Set the proper name for the spinner here
        spnCourses.setAdapter(adapter);
        lstCourses.setAdapter(adapter2);



    }

    private void NextAct(int s){

        Bundle bu = new Bundle();
        Intent i = new Intent(getApplicationContext(), view_courses.class);

        bu.putInt("key", s); //New entries use negative numbers to avoid triggering any if statements
        i.putExtras(bu);
        startActivity(i);
    }

    private void save(){
        if(txtName.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Error: Name is empty. Enter name before saving!", Toast.LENGTH_LONG).show(); return;}
        else if(txtStart.getText().toString().isEmpty())
        {Toast.makeText(getApplicationContext(), "Error: Start date is empty. Enter name before saving!", Toast.LENGTH_LONG).show(); return;}
        else if(txtEnd.getText().toString().isEmpty())
        {Toast.makeText(getApplicationContext(), "Error: End date is empty. Enter name before saving!", Toast.LENGTH_LONG).show(); return;}

        String cFin ="";
        for (int s: cLstIds){
            cFin = cFin + s + ",";
        }

        Terms nT = new Terms(txtName.getText().toString(),txtStart.getText().toString(),txtEnd.getText().toString(),cFin);
        //Diagnostic Lines
        List<Terms> nTL;
        nT.print();

        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        if(wid <= 0)
         db.addTerm(nT);
        else {
            nT.setId(wid);
            db.updateTerm(nT);
        }

        //diagnosticLines

        System.out.println("# Testing the get function!");
        nTL = db.getAllTerms();
        System.out.println("# Testing the get function! Part Two");
        int count = 1;
        for (Terms t:nTL){
            System.out.println("# Entry: " + count);
            t.print();
            count++;
        }


        mod_term.this.finish();

    }
}
