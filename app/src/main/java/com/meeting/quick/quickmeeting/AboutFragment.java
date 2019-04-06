package com.meeting.quick.quickmeeting;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meeting.quick.quickmeeting.R;


/**
 * A simple {@link Fragment} subclass.
 */

public class AboutFragment extends Fragment {
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String description = "Quick Meeting was designed by students to help save the precious time of students and " +
                "instructors by getting rid of the hassle in scheduling meetings. We would love to hear back from you!";
        view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }

}
