package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.DatabaseReaderDbHelper;
import com.wgustudent.SamuelCrumplerC196MobileApp.classes.Mentor;

public class mod_mentor extends AppCompatActivity {

    int wid = -1;
    Mentor wM;
    EditText txtName;
    EditText txtPhone;
    EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_mentor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);


        Bundle b = getIntent().getExtras();
        wid = b.getInt("key");
        System.out.println("The id is: " + wid);

        txtName = (EditText) findViewById(R.id.txtMentorName);
        txtPhone = (EditText) findViewById(R.id.txtMentorPhone);
        txtEmail = (EditText) findViewById(R.id.txtMentorEmail);

        if(wid > 0){
            wM = db.getMentor(wid);
            txtName.setText(wM.getName());
            txtPhone.setText(wM.getPhone());
            txtEmail.setText(wM.getEmail());

        }




        FloatingActionButton fab = findViewById(R.id.fabMentorSave);
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

    private void save(){
        if(txtName.getText().toString().isEmpty())
            {Toast.makeText(getApplicationContext(), "Error: Name is empty. Enter name before saving!", Toast.LENGTH_LONG).show(); return;}
        else if(txtPhone.getText().toString().isEmpty())
            {Toast.makeText(getApplicationContext(), "Error: Phone is empty. Enter name before saving!", Toast.LENGTH_LONG).show(); return;}
        else if(txtEmail.getText().toString().isEmpty())
            {Toast.makeText(getApplicationContext(), "Error: E-Mail is empty. Enter name before saving!", Toast.LENGTH_LONG).show(); return;}

        Mentor nM = new Mentor(txtName.getText().toString(),txtPhone.getText().toString(),txtEmail.getText().toString());

        DatabaseReaderDbHelper db = new DatabaseReaderDbHelper(this);

        if(wid <= 0)
            db.addMentor(nM);
        else {
            nM.setMentorId(wid);
            db.updateMentor(nM);
        }

        nM.print();
        mod_mentor.this.finish();

    }

}
