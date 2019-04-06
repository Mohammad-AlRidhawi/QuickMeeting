package com.meeting.quick.quickmeeting.Student;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import org.w3c.dom.Text;

public class EnterQueueFragment extends Fragment {

    private static final String TAG = "SeeProfessorFragment";
    private View view;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ValueEventListener mRefListener;
    private DatabaseReference ref;
    private TextView positionInLine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_in_queue, container, false);
        final String professorUID = getArguments().getString("UID");
        positionInLine = (TextView) view.findViewById(R.id.positonInLine);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("my_app_user").child(professorUID).child("professorCounter");
        mRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("trigged");
                long u = (long) dataSnapshot.getValue();
                myRef = database.getReference("my_app_user/" + FirebaseAuth.getInstance().getUid());
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        Log.d("POSITION", String.valueOf(u.getStudentPosition()));
                        if (u.getStudentPosition() > 0) {
                            myRef.child("studentPosition").setValue(u.getStudentPosition() - 1);
                        } else if (u.getStudentPosition() == 0) {
                            FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                            SeeProfessorFragment fragment = new SeeProfessorFragment();
                            Bundle args = new Bundle();
                            args.putString("UID", professorUID);
                            fragment.setArguments(args);
                            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        }
                        positionInLine.setText(String.valueOf(u.getStudentPosition()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addValueEventListener(mRefListener);

        Button leaveQueue = view.findViewById(R.id.leave_queue);
        leaveQueue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference lqRef = database.getReference("my_app_user").child(professorUID);
                lqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        myRef = database.getReference("my_app_user/" + professorUID);
                        if (u.getProfessorCounter() == 0) {
                            myRef.child("avaliablity").setValue("Available");
                        } else {
                            DatabaseReference professorCounterRef = myRef.child("professorCounter");
                            professorCounterRef.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {
                                    Integer currentValue = mutableData.getValue(Integer.class);
                                    mutableData.setValue(currentValue - 1);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(
                                        DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                                    System.out.println("Transaction completed");
                                }
                            });
                        }
                        FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new StudentHomeFragment()).commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        ref.removeEventListener(mRefListener);
    }
}




