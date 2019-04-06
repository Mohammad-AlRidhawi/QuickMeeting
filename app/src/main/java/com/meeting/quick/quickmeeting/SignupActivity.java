package com.meeting.quick.quickmeeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.meeting.quick.quickmeeting.Model.User;
import com.meeting.quick.quickmeeting.Professor.ProfessorConfig;
import com.meeting.quick.quickmeeting.Student.StudentActivity;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextPassword;
    private Button buttonCreateAccount;
    private Button redirectLogin;
    private RadioGroup radioGroup;
    private RadioButton role;

    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
// ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        buttonCreateAccount = findViewById(R.id.btn_signup);
        editTextEmail = findViewById(R.id.input_email);
        editTextName = findViewById(R.id.input_name);
        editTextLastName = findViewById(R.id.input_last_name);
        editTextPassword = findViewById(R.id.input_password);
        redirectLogin = findViewById(R.id.link_login);
        progressBar = findViewById(R.id.progressbar);

        buttonCreateAccount.setOnClickListener(this);
        redirectLogin.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            //Email Is Empty
            editTextName.setError("First Name is Required");
            editTextName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(lastName)){
            //Email Is Empty
            editTextLastName.setError("Last Name is Required");
            editTextLastName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(email)){
            //Email Is Empty
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }



        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please Enter Valid Email");
            editTextEmail.requestFocus();
            editTextEmail.requestFocus();
            return;

        }

        if(TextUtils.isEmpty(password)){
            // password is empty
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            // password is empty
            editTextPassword.setError("Please Enter Password that longer than 6 characters");
            editTextPassword.requestFocus();
            return;
        }
        // Validation is good:

        progressBar.setVisibility(View.VISIBLE);
        radioGroup = findViewById(R.id.radio_group);
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        role = findViewById(selectedId);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User();
                    user.setFirstName(editTextName.getText().toString().trim());
                    user.setLastName(editTextLastName.getText().toString().trim());
                    user.setUid(firebaseAuth.getUid());
                    user.setUnqieCode();
                    user.setRole(role.getText().toString());
                    user.setAvaliablity("Available");
                    user.setProfessorCounter(0);
                    mDatabase.child("my_app_user").child(firebaseAuth.getUid()).setValue(user);

                    Toast.makeText(getApplicationContext(), "You successfully created an account", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    if (user.getRole().equals("Student")) {
                        finish();
                        startActivity(new Intent(SignupActivity.this, StudentActivity.class));
                    } else {
                        user.setLocation("N/A");
                        user.setProfessorCounter(0);
                        finish();
                        startActivity(new Intent(SignupActivity.this, ProfessorConfig.class));
                    }
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });




    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v == buttonCreateAccount){
            registerUser();
        }

        if(v == redirectLogin){
           // Open Login Screen
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));

        }
    }
}
