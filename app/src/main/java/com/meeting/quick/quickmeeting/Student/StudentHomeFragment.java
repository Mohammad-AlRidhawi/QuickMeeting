package com.meeting.quick.quickmeeting.Student;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meeting.quick.quickmeeting.Model.User;
import com.meeting.quick.quickmeeting.R;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeFragment extends Fragment {

    private static final String TAG = "StudentHomeFragment";
    private View view;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    List<User> professorList;
    //the recyclerview
    RecyclerView recyclerView;
    private NavigationView navigationView;
    private boolean newData = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TRANSACTION","I MADE IT HOME!");
        view = inflater.inflate(R.layout.fragment_student, container, false);

        FloatingActionButton addInstructionBtn = view.findViewById(R.id.btn_add);
        addInstructionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new StudentAddProfessorFragment()).commit();

            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference("my_app_user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("professorList");
        professorList = new ArrayList<>();
        //creating recyclerview adapter
        final ProfessorAdapter adapter = new ProfessorAdapter(getContext(), professorList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
        //Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                professorList.clear();
                //System.out.println("Before" + professorList.size());
                recyclerView.removeAllViews();

                adapter.notifyDataSetChanged();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.getValue().toString();
                    System.out.println(uid);
                    DatabaseReference ref = database.getReference("my_app_user").child(uid);
                    ref.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean notFound = false;
                            User u = dataSnapshot.getValue(User.class);
                            if (professorList.isEmpty()) {
                                professorList.add(u);
                                System.out.println(professorList.size());

                                //creating recyclerview adapter
                                ProfessorAdapter adapter = new ProfessorAdapter(getContext(), professorList);
                                //System.out.println("Adapter");
                                //setting adapter to recyclerview
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                for (int i = 0; i < professorList.size(); i++) {
                                    if (professorList.get(i).getUid().equals(u.getUid())) {
                                        //System.out.println("SameOne Found");
                                        professorList.set(i, u);
                                        ProfessorAdapter adapter = new ProfessorAdapter(getContext(), professorList);
                                        recyclerView.setAdapter(adapter);
                                        System.out.println(professorList.get(i).getLastName());
                                        adapter.notifyDataSetChanged();
                                        notFound = true;
                                    }
                                }
                                if (notFound == false) {
                                    professorList.add(u);
                                    //System.out.println(professorList.size());

                                    //creating recyclerview adapter
                                    ProfessorAdapter adapter = new ProfessorAdapter(getContext(), professorList);
                                    //System.out.println("Adapter");
                                    //setting adapter to recyclerview
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    newData = false;


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

}
