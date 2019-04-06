package com.meeting.quick.quickmeeting.Student;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meeting.quick.quickmeeting.Model.User;
import com.meeting.quick.quickmeeting.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class StudentAddProfessorFragment extends Fragment {

    private static final String TAG = "StudentHomeFragment";
    private View view;

    private Button searchButton;
    private EditText searchField;
    private DatabaseReference mUserDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_add_professor, container, false);


        TextView whatThisButton = view.findViewById(R.id.instructor_code_help);
        whatThisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sd = new SweetAlertDialog(getContext());
                sd.setTitleText("You can register to an instructor by using their unique identifier. Please contact your instructor " +
                                "to obtain their unique ID.");
                sd.setCancelable(true);
                sd.setCanceledOnTouchOutside(true);
                sd.show();
                int backgroundColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                Button btn = sd.findViewById(R.id.confirm_button);
                btn.setBackgroundColor(backgroundColor);
            }
        });

        mUserDatabase = FirebaseDatabase.getInstance().getReference("my_app_user");
        searchButton = view.findViewById(R.id.search_btn);
        searchField = view.findViewById(R.id.search_field);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), searchField.getText().toString().toUpperCase(), Toast.LENGTH_SHORT).show();

                String searchText = searchField.getText().toString().toUpperCase();

                Query query3 = FirebaseDatabase.getInstance().getReference("my_app_user")
                        .orderByChild("unqieCode")
                        .equalTo(searchText);


                query3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists() ) {
                            SweetAlertDialog sdQ = new SweetAlertDialog(getContext());
                            sdQ.setTitleText("Instructor ID does not exist");
                            sdQ.setContentText("Sorry! We were not able to find this instructor. Please verify you have entered " +
                                               "the correct ID");
                            sdQ.setCancelable(true);
                            sdQ.setCanceledOnTouchOutside(true);
                            sdQ.show();
                            int backgroundColor = ContextCompat.getColor(getContext(), R.color.red);
                            Button btn = sdQ.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(backgroundColor);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                query3.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        final String professor = snapshot.getValue(User.class).getUid();
                        System.out.println(snapshot.getValue(User.class).getUid());
                        DatabaseReference myRef = mUserDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("professorList");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
                                };
                                List<String> professorList = dataSnapshot.getValue(genericTypeIndicator);
                                // Check if friends list exists
                                if (professorList == null) {
                                    // Create list
                                    professorList = new ArrayList<String>();
                                }
                                // Add new user
                                professorList.add(professor);

                                // Set updated friends list
                                mUserDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("professorList").setValue(professorList);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container, new StudentHomeFragment()).commit();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("David");
                    }


                });


            }
        });
        return view;
    }
}
