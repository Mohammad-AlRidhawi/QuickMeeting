package com.meeting.quick.quickmeeting.Professor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.meeting.quick.quickmeeting.Model.User;
import com.meeting.quick.quickmeeting.R;
import com.meeting.quick.quickmeeting.Student.ProfessorAdapter;
import com.meeting.quick.quickmeeting.Student.StudentAddProfessorFragment;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfessorHomeFragment extends Fragment {

    private static final String TAG = "StudentHomeFragment";
    private View view;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_professor_home, container, false);
        TextView whatThisButton = view.findViewById(R.id.whats_this_id);
        RadioGroup radioGroup = view.findViewById(R.id.radio_group);
        final TextView instructorID = view.findViewById(R.id.instructor_code_ID);
        final TextView numberofPeopleCounter = view.findViewById(R.id.numberofPeopleCounter);
        database = FirebaseDatabase.getInstance();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                final DatabaseReference ref = database.getReference("my_app_user").child(FirebaseAuth.getInstance().getUid());
                if (checkedId == R.id.available_status){
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ref.child("avaliablity").setValue("Available");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                if (checkedId == R.id.away_status){
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ref.child("avaliablity").setValue("Away");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        whatThisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sd = new SweetAlertDialog(getContext());
                sd.setTitleText("The instructor ID is your unique identifier. Please give out this ID to students so they can arrange meetings with you.");
                sd.setCancelable(true);
                sd.setCanceledOnTouchOutside(true);
                sd.show();
                int backgroundColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                Button btn = sd.findViewById(R.id.confirm_button);
                btn.setBackgroundColor(backgroundColor);
            }
        });
        DatabaseReference ref = database.getReference("my_app_user").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                instructorID.setText(u.getUnqieCode());
                numberofPeopleCounter.setText(String.valueOf(u.getProfessorCounter()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        return view;
    }
}
