package com.wgustudent.SamuelCrumplerC196MobileApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class send_note extends AppCompatActivity {


    String notes;
    String[] reps = {""};
    String[] cc = {""};

    String sub;

    TextView txtRep;
    TextView txtSub;
    TextView txtNotes;
    Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        notes = b.getString("key");

        txtRep = (TextView) findViewById(R.id.txtSendRep);
        txtSub = (TextView) findViewById(R.id.txtSendSub);
        txtNotes = (TextView) findViewById(R.id.txtSendNotes);
        btnSend = (Button) findViewById(R.id.btnSendEmail);

        txtNotes.setText(notes);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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


    private void send(){
        Log.i("Send Email","");

        reps = txtRep.getText().toString().split(",");

        Intent eI = new Intent(Intent.ACTION_SEND);
        eI.setData(Uri.parse("mailto:"));
        eI.setType("text/plain");
        eI.putExtra(Intent.EXTRA_EMAIL, reps);
        eI.putExtra(Intent.EXTRA_CC, cc);
        eI.putExtra(Intent.EXTRA_SUBJECT, txtSub.getText().toString());
        eI.putExtra(Intent.EXTRA_TEXT, txtNotes.getText().toString());

        try {
            startActivity(Intent.createChooser(eI, "Send mail..."));
            finish();
            Log.i("Email sent", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(send_note.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }
}
