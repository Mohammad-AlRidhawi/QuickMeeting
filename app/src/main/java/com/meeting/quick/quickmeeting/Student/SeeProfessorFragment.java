package com.meeting.quick.quickmeeting.Student;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.victor.loading.book.BookLoading;

import java.util.ArrayList;
import java.util.List;

public class SeeProfessorFragment extends Fragment {

    private static final String TAG = "SeeProfessorFragment";
    private View view;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_seeing_professor, container, false);
        Button endSession = view.findViewById(R.id.end_session);
        final String professorUID = getArguments().getString("UID");
        database = FirebaseDatabase.getInstance();
        final BookLoading loading = view.findViewById(R.id.bookloading);
        loading.start();
        endSession.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference ref = database.getReference("my_app_user").child(professorUID);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        myRef = database.getReference("my_app_user/" + professorUID);
                        if(u.getProfessorCounter()==0){
                            myRef.child("avaliablity").setValue("Available");
                        }else{
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
                        loading.stop();
                        FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new StudentHomeFragment()).commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//

            }
        });
        return view;
    }
}
