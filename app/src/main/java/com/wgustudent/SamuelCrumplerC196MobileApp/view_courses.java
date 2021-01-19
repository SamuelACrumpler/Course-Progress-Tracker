package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Assessments;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Courses;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.DatabaseReaderDbHelper;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Mentor;

import java.util.ArrayList;
import java.util.List;

public class view_courses extends AppCompatActivity {


    int Type;
    int ModId;

    Courses modCourse;
    List<String> cs = new ArrayList<>();
    List<String> assessName = new ArrayList<>();
    List<Integer> assessId = new ArrayList<>();
    List<Assessments> assessList = new ArrayList<>();


    TextView txtTitle;
    TextView txtStart;
    TextView txtEnd;
    TextView txtStatus;
    TextView txtMentor;
    TextView txtNotes;
    TextView txtMenEmail;
    TextView txtMenPhone;



    ListView lstAssess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_courses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        Type = b.getInt("id");
        ModId = b.getInt("key");

        System.out.println("The id is: " + Type);
        System.out.println("The key is: " + ModId);

        if(ModId <= 0){         {
            Toast.makeText(getApplicationContext(), "Error: No data to load!", Toast.LENGTH_LONG).show(); return;}
        }

        txtTitle = (TextView) findViewById(R.id.txtCorTitle);
        txtStart = (TextView) findViewById(R.id.txtCorStart);
        txtEnd = (TextView) findViewById(R.id.txtCorEnd);
        txtStatus = (TextView) findViewById(R.id.txtCorStatus);
        txtMentor = (TextView) findViewById(R.id.txtCorMentor);
        txtNotes = (TextView) findViewById(R.id.txtCorNotes);
        txtMenPhone = (TextView) findViewById(R.id.txtCorMentorPhone);
        txtMenEmail = (TextView) findViewById(R.id.txtCorMentorEmail);
        lstAssess = (ListView) findViewById(R.id.lstCorAssessments);

        refresh();

    }

    public void refresh(){
        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        modCourse = db.getCourse(ModId);

        txtTitle.setText("Title: "+modCourse.getTitle());
        txtStart.setText("Start Date: "+modCourse.getStart());
        txtEnd.setText("End Date: "+modCourse.getEnd());
        txtStatus.setText("Status: "+modCourse.getStatus());
        //logic for mentor

        Mentor tm = db.getMentor(modCourse.getMentorId());
        txtMentor.setText("Mentor Name: "+tm.getName());
        txtMenEmail.setText("E-Mail: "+tm.getEmail());
        txtMenPhone.setText("Phone: "+tm.getPhone());

        txtNotes.setText(modCourse.getNotes());

        assessList.clear();
        assessId.clear();

        for (Assessments a:db.getAllAssessments()){
            a.print();
            if(a.getCourseId() == ModId){

                assessList.add(a);
                assessId.add(a.getaId());
            }
        }

        //Add assessments to the course list
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, assessList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);


                text1.setText(assessList.get(position).getType()+ " : " +assessList.get(position).getName());
                text2.setText("Due Date : " + assessList.get(position).getDue());
                text2.setTextSize(10);
                return view;
            }
        };//End Adpter Array

        lstAssess.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viewcourse, menu);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("#This is a continue");
        refresh();
        //Use this function to refresh the assessment list.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.actSend) {
            NextAct(modCourse.getNotes());
        }
        else if (id == R.id.actEdit) {
            NextAct(ModId);
        } else if(id == R.id.actDel){

            new AlertDialog.Builder(this)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("#Delete operation test is successful!");
                            DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(getApplicationContext());
                            db.deleteCourse(modCourse);
                            view_courses.this.finish();

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


    private void NextAct(int s){
        Intent i = new Intent(getBaseContext(), ModCourse.class);
        Bundle b2 = new Bundle();
        b2.putInt("key", s); //Your id
        i.putExtras(b2); //Put your id to your next Intent
        startActivity(i);

    }

    private void NextAct(String s){
        Intent i = new Intent(getBaseContext(), send_note.class);
        Bundle b2 = new Bundle();
        b2.putString("key", s); //Your id
        i.putExtras(b2); //Put your id to your next Intent
        startActivity(i);

    }
}
