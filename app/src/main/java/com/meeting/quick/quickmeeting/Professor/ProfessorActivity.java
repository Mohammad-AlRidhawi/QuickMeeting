package com.meeting.quick.quickmeeting.Professor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meeting.quick.quickmeeting.AboutFragment;
import com.meeting.quick.quickmeeting.LoginActivity;
import com.meeting.quick.quickmeeting.Model.User;
import com.meeting.quick.quickmeeting.R;
import com.meeting.quick.quickmeeting.Student.ProfessorAdapter;
import com.meeting.quick.quickmeeting.Student.StudentActivity;
import com.meeting.quick.quickmeeting.Student.StudentAddProfessorFragment;
import com.meeting.quick.quickmeeting.Student.StudentHomeFragment;

public class ProfessorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout_professor);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfessorHomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
        View headerView = navigationView.getHeaderView(0);
        final TextView drawerName = headerView.findViewById(R.id.drawer_name);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("my_app_user").child(FirebaseAuth.getInstance().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(User.class).getFirstName() + " " +
                        dataSnapshot.getValue(User.class).getLastName();
                drawerName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Here");
        System.out.println(FirebaseAuth.getInstance().getCurrentUser() == null);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfessorHomeFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.professor_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfessorSetOfficeFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.log_out:
                drawer.closeDrawer(GravityCompat.START);
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ProfessorActivity.this, LoginActivity.class));
                break;
        }
        return true;
    }
}
