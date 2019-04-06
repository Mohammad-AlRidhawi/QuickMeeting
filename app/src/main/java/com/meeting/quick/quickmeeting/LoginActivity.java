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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meeting.quick.quickmeeting.Professor.ProfessorActivity;
import com.meeting.quick.quickmeeting.Student.StudentActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button redirectSignup;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = findViewById(R.id.btn_login);
        editTextEmail = findViewById(R.id.input_email);
        editTextPassword = findViewById(R.id.input_password);
        redirectSignup = findViewById(R.id.link_register);

        buttonLogin.setOnClickListener(this);
        redirectSignup.setOnClickListener(this);
        progressBar = findViewById(R.id.progressbar);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void loginUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
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

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseAuth firebaseAuth;
                    FirebaseDatabase database;
                    firebaseAuth = FirebaseAuth.getInstance();

                    database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("my_app_user/" + firebaseAuth.getUid());
                    System.out.println("Here");
                    System.out.println(FirebaseAuth.getInstance().getCurrentUser() == null);

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                String userDepartment = dataSnapshot.child("role").getValue().toString();
                                //Now check with this userDepartment
                                if (userDepartment.equals("Professor")) {
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, ProfessorActivity.class));
                                } else {
                                    finish();
                                    startActivity(new Intent(LoginActivity.this, StudentActivity.class));
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });


                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (firebaseAuth.getCurrentUser() != null) {
//            finish();
//            startActivity(new Intent(this, Reducer.class));
//        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            loginUser();
        }

        if(v == redirectSignup){
            // Open Login Screen
            finish();
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        }
    }
}
