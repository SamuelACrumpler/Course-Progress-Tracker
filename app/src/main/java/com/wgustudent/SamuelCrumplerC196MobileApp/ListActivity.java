package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Courses;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.DatabaseReaderDbHelper;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Mentor;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Terms;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    int pid = -1;
    List<Courses> courseList = new ArrayList<>();
    List<Integer> courseIds = new ArrayList<>();
    List<String> tc = new ArrayList<>();
    List<String> ta = new ArrayList<>(); //I don't know if I need to make a list of assessments here.

    List<Terms> termList = new ArrayList<>();
    List<Integer> termIds = new ArrayList<>();
    List<String> tt = new ArrayList<>();

    List<Mentor> mentorList = new ArrayList<>();
    List<Integer> mentorIds = new ArrayList<>();
    List<String> tm = new ArrayList<>();


    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        pid = b.getInt("key");


        System.out.println("The id is: " + pid);

        refresh();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("This is the position: " + (position));
                    ///This will need to pass the ID for the thing

                Intent i;
                Bundle b2 = new Bundle();

                if(pid == 0) {//Terms
                    i = new Intent(getApplicationContext(), ViewStandardActivity.class);
                    b2.putInt("key", termIds.get(position)); //Your id
                }
                else if(pid == 1) {//Courses
                    i = new Intent(getApplicationContext(), view_courses.class);
                    b2.putInt("key", courseIds.get(position));
                }
                else{//TMentors
                    i = new Intent(getApplicationContext(), ViewStandardActivity.class);
                    b2.putInt("key", mentorIds.get(position));
                }


                Bundle b3 = new Bundle();

                b3.putInt("id", pid);//type
                i.putExtras(b2);
                i.putExtras(b3);

                startActivity(i);
            }
        });



        FloatingActionButton fabAdd = findViewById(R.id.fabListAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextAct();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("#This is a continue");
        refresh();
        //Use this function to refresh the assessment list.
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

    private void refresh(){
        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tc);

        if (pid == 0) { //Terms
            termList.clear();
            tt.clear();
            termIds.clear();
            termList = db.getAllTerms();
            for (Terms t: termList){
                tt.add(t.getTitle());
                termIds.add(t.getId());
            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tt);
        } else if(pid == 1){//Courses

            courseList.clear();
            tc.clear();
            courseIds.clear();
            courseList = db.getAllCourses();
            for (Courses c: courseList){
                c.print();
                tc.add(c.getTitle());
                courseIds.add(c.getCourseId());
            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tc);
        }
        else if(pid == 2){//Mentors

            mentorList.clear();
            tm.clear();
            mentorIds.clear();
            mentorList = db.getAllMentors();
            for (Mentor m: mentorList){
                tm.add(m.getName());
                mentorIds.add(m.getMentorId());
            }
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tm);
        }
        listView = (ListView) findViewById(R.id.lstView);
        listView.setAdapter(null);
        listView.setAdapter(adapter);
    }

    private void NextAct(){
        Intent i = new Intent(this,  mod_mentor.class);
        Bundle bu = new Bundle();

        if(pid == 0)//Terms
            i = new Intent(this, mod_term.class);
        else if(pid == 1)//courses
            i = new Intent(getApplicationContext(), ModCourse.class);
        else if(pid == 2)//mentors
            i = new Intent(getApplicationContext(), mod_mentor.class);


        bu.putInt("key", -1); //New entries use negative numbers to avoid triggering any if statements
        i.putExtras(bu);
        startActivity(i);
    }
}
