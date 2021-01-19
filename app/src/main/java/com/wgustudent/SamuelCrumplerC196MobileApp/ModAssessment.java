package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Assessments;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Broadcaster;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.DatabaseReaderDbHelper;

import java.util.Calendar;

public class ModAssessment extends AppCompatActivity {

    final Calendar c = Calendar.getInstance();
    private int cYear = c.get(Calendar.YEAR);
    private int cMon = c.get(Calendar.MONTH);
    private int cDay = c.get(Calendar.DAY_OF_MONTH);

    int aId = -1; //This needs to be populated when calling information from the database
    int cId = -1;
    int groupId;
    Assessments wAssess;

    EditText txtAName;
    RadioGroup Type;
    RadioButton selected;
    TextView txtDue;
    AlarmManager AlarmMan;

    TextView txtGoal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        cId = b.getInt("cid");
        aId = b.getInt("aid");
        System.out.println("I got the courseId: " + cId);
        System.out.println("I got the assesId: " + aId);

        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);
        txtAName = (EditText) findViewById(R.id.txtAssessName);
        txtDue = (TextView) findViewById(R.id.txtDue);
        txtGoal = (TextView) findViewById(R.id.txtGoal);

        RadioButton p = (RadioButton) findViewById(R.id.radPer);
        RadioButton o = (RadioButton) findViewById(R.id.radObj);
        AlarmMan = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        //If Statement to fill in information if the ID is greater than 0
        if (aId > 0){
            wAssess = db.getAssessment(aId);
            txtAName.setText(wAssess.getName());
            txtDue.setText(wAssess.getDue());
            if(!wAssess.getGoal().isEmpty())
                txtGoal.setText(wAssess.getGoal());

            if(wAssess.getType().equals("Objective"))
                o.setChecked(true);
            else
                p.setChecked(true);
        }






        txtDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDueDate();
            }
        });

        txtGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGoalDate();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabCorSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modassess, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if(id == R.id.actDel){

            new AlertDialog.Builder(this)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("#Delete operation test is successful!");
                            DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(getApplicationContext());
                            if(aId >= 0)
                            db.deleteAssessment(db.getAssessment(aId));
                            ModAssessment.this.finish();

                        }
                    })

                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if(id == R.id.actHome){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }




    private void Save(){
        if(txtAName.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Error: Name is empty. Enter name before saving!", Toast.LENGTH_LONG).show(); return;
        } else if(txtDue.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Error: Due date is empty. Enter due date before saving!", Toast.LENGTH_LONG).show(); return;
        }
        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        RadioButton p = (RadioButton) findViewById(R.id.radPer);
        RadioButton o = (RadioButton) findViewById(R.id.radObj);

        String type = "";

        if(o.isChecked())
            type = "Objective";
        else
            type = "Performance";



       Assessments nA = new Assessments(aId,cId,txtAName.getText().toString(), type,txtDue.getText().toString(),txtGoal.getText().toString());
       if (aId <= 0)
           db.addAssessment(nA);
       else if(aId > 0)
           db.updateAssessment(nA);
       // nA.print();
        //nA.save();

        alertGoal(nA);

        ModAssessment.this.finish();



    }

    private void alertGoal(Assessments c){
        Calendar cal = Calendar.getInstance();
        String[] splt = c.getGoal().split("-");
        cal.set(Integer.parseInt(splt[2]),(Integer.parseInt(splt[1])-1),Integer.parseInt(splt[0]),0,0,0);
        Intent intent=new Intent(this, Broadcaster.class);
        Bundle b = new Bundle();
        b.putString("message","This assessment goal date has passed :" + c.getName());
        intent.putExtras(b);
        int id = (int) System.currentTimeMillis();
        PendingIntent pi = PendingIntent.getBroadcast(this, id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmMan.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

    }



    private void setDueDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //Save as a string instead, then return it??
                        txtDue.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, cYear, cMon, cDay);
        datePickerDialog.show();
    }

    private void setGoalDate(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //Save as a string instead, then return it??
                        txtGoal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, cYear, cMon, cDay);
        datePickerDialog.show();
    }

}
