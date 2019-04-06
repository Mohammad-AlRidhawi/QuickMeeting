package com.meeting.quick.quickmeeting.Professor;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.meeting.quick.quickmeeting.R;

import java.util.Calendar;


public class ProfessorConfig extends AppCompatActivity {

    EditText chooseTime;
    EditText endTime;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    private ProgressBar progressBar;
    private Button config;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_config);
        progressBar = findViewById(R.id.progressbar);
        config = findViewById(R.id.btn_config_professor);
        firebaseAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("my_app_user/" + firebaseAuth.getUid());
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configOffice();
            }
        });
    }

    private void configOffice() {

        EditText location = findViewById(R.id.office_location);


        if (TextUtils.isEmpty(location.getText().toString().trim())) {
            //Email Is Empty
            location.setError("Location is Required");
            location.requestFocus();
            return;
        }

        // Validation is good:

        progressBar.setVisibility(View.VISIBLE);
        myRef.child("location").setValue(location.getText().toString().trim());

        Toast.makeText(getApplicationContext(), "You successfully configured your account", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        finish();
        startActivity(new Intent(this, ProfessorActivity.class));
    }


}
