package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Courses;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.DatabaseReaderDbHelper;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Mentor;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Terms;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewStandardActivity extends AppCompatActivity {

    ListView listView;
    int Type;
    int ModId;

    Terms modTerm;
    List<String> tc= new ArrayList<>();

    Mentor modMentor;
    List<String> tm = new ArrayList<>();

    String cDate = DateFormat.getDateTimeInstance().format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_standard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        Type = b.getInt("id");
        ModId = b.getInt("key");

        refresh();

        System.out.println("The id is: " + Type);
        System.out.println("The key is: " + ModId);


    }

    public void refresh(){

        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        //Key1 = type
        //Key2 = to pull info from




        tc.clear();
        tm.clear();
        if(ModId > 0 && Type == 0) {
            modTerm = db.getTerm(ModId);
            tc.add("Term: " + modTerm.getTitle());
            tc.add("Start Date: " + modTerm.getStart());
            tc.add("End Date: " +modTerm.getEnd());

            String[] ts = modTerm.split();

                for (String s : ts) {
                    if(!s.equals("")) {
                        Courses tcs = db.getCourse(Integer.parseInt(s));
                        tc.add("Course: " + tcs.getTitle());
                    }
                }

        } else if(ModId > 0 && Type == 2){
            modMentor = db.getMentor(ModId);
            tm = modMentor.pushList();
        } else {
            Toast.makeText(getApplicationContext(), "Error: No data to load!", Toast.LENGTH_LONG).show();
            ViewStandardActivity.this.finish();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tc);

        if(Type == 0){//Terms
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tc);
        }else if (Type == 2) {//Mentors
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tm);
        }
        listView = (ListView) findViewById(R.id.lstGView);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viewstandard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.actEdit) {
            NextAct(ModId,Type);
        } else if(id == R.id.actDel){

            new AlertDialog.Builder(this)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")

                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("#Delete operation test is successful!");
                            DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(getApplicationContext());
                            if(Type == 0) {
                                if (!modTerm.getcIds().equals("")) {
                                    Toast.makeText(getApplicationContext(), "Error: Please remove all courses before trying to delete this term.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                db.deleteTerm(modTerm);
                            }
                            else if(Type == 2)
                                db.deleteMentor(modMentor);
                            ViewStandardActivity.this.finish();

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

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("#This is a continue");
        refresh();
        //Use this function to refresh the assessment list.
    }

    private void NextAct(int s, int t){
        Intent i = new Intent(getApplicationContext(), mod_term.class);
        if(t == 0)//Terms
            i = new Intent(getApplicationContext(), mod_term.class);
        else if(t == 2)//mentors
            i = new Intent(getApplicationContext(), mod_mentor.class);


        Bundle b2 = new Bundle();
        b2.putInt("key", s); //New entries use negative numbers to avoid triggering any if statements
        i.putExtras(b2);
        startActivity(i);

    }
}
