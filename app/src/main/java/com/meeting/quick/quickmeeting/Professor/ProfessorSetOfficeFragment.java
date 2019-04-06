package com.meeting.quick.quickmeeting.Professor;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meeting.quick.quickmeeting.R;
import com.meeting.quick.quickmeeting.Student.StudentHomeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfessorSetOfficeFragment extends Fragment {

    private View view;
    private Button officeButton;
    private EditText officeField;
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_professor_set_office, container, false);
        officeButton = view.findViewById(R.id.office_btn);
        officeField = view.findViewById(R.id.office_field);
        database = FirebaseDatabase.getInstance();
        officeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference ref = database.getReference("my_app_user").child(FirebaseAuth.getInstance().getUid());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ref.child("location").setValue(officeField.getText().toString());
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new ProfessorHomeFragment()).commit();


                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }

}
