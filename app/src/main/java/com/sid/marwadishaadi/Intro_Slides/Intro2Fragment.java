package com.sid.marwadishaadi.Intro_Slides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sid.marwadishaadi.Login.LoginActivity;
import com.sid.marwadishaadi.R;

public class Intro2Fragment extends Fragment {

    Button getStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_intro2, container, false);

        getStarted = (Button) view.findViewById(R.id.get_started);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences userinfo = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editors = userinfo.edit();
                editors.putBoolean("isFirstTime", true);
                editors.apply();

                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        return view;
    }
}
