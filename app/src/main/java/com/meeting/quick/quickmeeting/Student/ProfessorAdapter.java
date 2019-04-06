package com.meeting.quick.quickmeeting.Student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.meeting.quick.quickmeeting.Model.User;
import com.meeting.quick.quickmeeting.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Elias on 3/10/2019.
 */


public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.ProductViewHolder> {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<User> professorList;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference ref;


    //getting the context and product list with constructor
    public ProfessorAdapter(Context mCtx, List<User> professorList) {
        this.mCtx = mCtx;
        this.professorList = professorList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_professor_card, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        //getting the product of the specified position
        final User professor = professorList.get(position);
        final int deletePostion = position;
        //binding the data with the viewholder views
        int professorCounter = professor.getProfessorCounter();
        String name = professor.getFirstName() + " " + professor.getLastName();
        String avaliablity = professor.getAvaliablity();
        holder.textViewName.setText(name);
        holder.textViewOfficeLocation.setText(professor.getLocation());
        holder.textViewHours.setText(avaliablity);
        holder.textViewPrice.setText(String.valueOf(professorCounter));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sd = new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE);
                        sd.setTitleText("Are you sure?");
                        sd.setContentText("This will delete the instructor from your dashboard");
                        sd.setConfirmText("Delete");
                        sd.setCancelText("Cancel");
                        sd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                System.out.println("Here");
                                System.out.println(deletePostion);
                                List<String> professorListTemp  = new ArrayList<>();

                                database = FirebaseDatabase.getInstance();
                                ref = database.getReference("my_app_user/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).child("professorList");
                                ref.child(Integer.toString(deletePostion)).removeValue();
                                professorList.remove(deletePostion);
                                System.out.print("test");
                                for(int i=0; i<professorList.size();i++){
                                    professorListTemp.add(professorList.get(i).getUid());
                                }
                                ref.setValue(professorListTemp);
                                notifyDataSetChanged();

                            }
                        });
                        sd.show();
                        int backgroundColor = ContextCompat.getColor(v.getContext(), R.color.red);
                        Button btn = sd.findViewById(R.id.confirm_button);
                        btn.setBackgroundColor(backgroundColor);
                        Button btnC = sd.findViewById(R.id.cancel_button);
                        btnC.getBackground().setAlpha(65);
                        btnC.setBackgroundColor(Color.parseColor("#808080"));


            }});
        if (avaliablity.equals("Available")) {
            holder.textViewHours.setTextColor(Color.parseColor("#437921"));
        } else if (avaliablity.equals("Busy")) {
            holder.textViewHours.setTextColor(Color.parseColor("#ff0000"));
        } else if (avaliablity.equals("Away")){
            holder.textViewHours.setTextColor(Color.parseColor("#FFCC00"));
        }

        if(professor.getAvaliablity().equals("Available") && professor.getProfessorCounter()==0 ){
            holder.seeInstructorButton.setEnabled(true);
            holder.seeInstructorButton.getBackground().setAlpha(255);
            holder.seeInstructorButton.setText("Meet Instructor");
            holder.seeInstructorButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    database = FirebaseDatabase.getInstance();
                    Log.d("DEBUG", "What");
                    myRef = database.getReference("my_app_user/" + professor.getUid());
                    myRef.child("avaliablity").setValue("Busy");
                    //Log.d("TRANSACTION","Going to See Professor Fragment");
                    FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                    SeeProfessorFragment fragment = new SeeProfessorFragment ();
                    Bundle args = new Bundle();
                    args.putString("UID", professor.getUid());
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();


                }
            });
        }else if(professor.getAvaliablity().equals("Busy") && professor.getProfessorCounter()>=0){
            holder.seeInstructorButton.setEnabled(true);
            holder.seeInstructorButton.getBackground().setAlpha(255);
            holder.seeInstructorButton.setText("Enter Queue");
            holder.seeInstructorButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("my_app_user/" + FirebaseAuth.getInstance().getUid());
                    int position = professor.getProfessorCounter()+1;
                    myRef.child("studentPosition").setValue(position);
                    myRef = database.getReference("my_app_user/" + professor.getUid());
                    myRef.child("professorCounter").setValue(position);
                    //Log.d("TRANSACTION","Going to Queue Fragment!");
                    FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                    EnterQueueFragment fragment = new EnterQueueFragment ();
                    Bundle args = new Bundle();
                    args.putString("UID", professor.getUid());
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
            });
        }else if (professor.getAvaliablity().equals("Away")){
            holder.seeInstructorButton.setBackgroundColor(Color.parseColor("#808080"));
            holder.seeInstructorButton.getBackground().setAlpha(64);
            holder.seeInstructorButton.setEnabled(false);
            holder.seeInstructorButton.setText("Unavailable");
        }


    }


    @Override
    public int getItemCount() {
        return professorList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewOfficeLocation, textViewHours, textViewPrice;
        Button seeInstructorButton;
        Button deleteButton;
        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textViewName);
            textViewOfficeLocation = itemView.findViewById(R.id.textViewOfficeLocation);
            textViewHours = itemView.findViewById(R.id.textViewHours);
            seeInstructorButton = itemView.findViewById(R.id.button_see_professor);
            textViewPrice = itemView.findViewById(R.id.peopleInLIne);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}