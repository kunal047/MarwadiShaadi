package com.example.sid.marwadishaadi.User_Profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Search.BottomSheet;
import com.example.sid.marwadishaadi.Similar_Profiles.SimilarActivity;

import org.json.JSONArray;
import org.json.JSONException;

import static android.content.ContentValues.TAG;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;

public class ProfileAdditionalDetailsFragment extends Fragment {

    public static final String SOME_INTENT_FILTER_NAME = "SOME_INTENT_FILTER_NAME";
    private TextView edit_about;
    private TextView edit_hobbies;
    private TextView edit_lifestyle;
    private TextView edit_horoscope;
    private static  int casebreak;
    private Button similar;
    private String clickedID  = customer_id;

    TextView aboutMe, hobbies, eatingHabits, drinkingHabits, smokingHabits, birthtime, gotra, manglik, matchHoroscope;

    public ProfileAdditionalDetailsFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver someBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String hobby= bundle.getString("hobbies");
            String am=bundle.getString("aboutMe");
            String eh=bundle.getString("eatingHabit");
            String dh=bundle.getString("drinkingHabit");
            String sh=bundle.getString("smokingHabit");
            String bt=bundle.getString("birthTime");
            String bp=bundle.getString("birthPlace");
            String g=bundle.getString("gotra");
            String m=bundle.getString("manglik");
            String mh=bundle.getString("matchHoroscope");

            if(hobby!=null)
                hobbies.setText(hobby);

            if(am!=null)
                aboutMe.setText(am);

            if(eh!=null) {
                eatingHabits.setText(eh);
            }

            if(dh!=null)
                drinkingHabits.setText(dh);

            if(sh!=null)
                smokingHabits.setText(sh);

            if(bt!=null || bp!=null)
                birthtime.setText(bt+" at "+bp);

            if(g!=null)
                gotra.setText(g);

            if(m!=null)
                manglik.setText(m);

            if(mh!=null)
                matchHoroscope.setText(mh);


        }
    };
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(someBroadcastReceiver,
                new IntentFilter(SOME_INTENT_FILTER_NAME));
    }
    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(someBroadcastReceiver);
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_profile__additional__details, container, false);
        edit_about = (TextView) mview.findViewById(R.id.aboutme_clear);
        edit_hobbies=(TextView) mview.findViewById(R.id.hobbies_clear);
        edit_lifestyle=(TextView)mview.findViewById(R.id.lifestyle_clear);
        edit_horoscope = (TextView) mview.findViewById(R.id.horoscope_clear);
        similar = (Button) mview.findViewById(R.id.similar);

        aboutMe = (TextView) mview.findViewById(R.id.about_me);
        hobbies = (TextView) mview.findViewById(R.id.hobbies);
        eatingHabits = (TextView) mview.findViewById(R.id.eating_habits);
        drinkingHabits = (TextView) mview.findViewById(R.id.drinking_habit);
        smokingHabits = (TextView) mview.findViewById(R.id.smoking_habits);
        birthtime = (TextView) mview.findViewById(R.id.birthtime_location);
        gotra = (TextView) mview.findViewById(R.id.gotra);
        manglik = (TextView) mview.findViewById(R.id.manglik);
        matchHoroscope = (TextView) mview.findViewById(R.id.match_horoscope);


        Intent data = getActivity().getIntent();
        String from = data.getStringExtra("from");
        if (data.getStringExtra("customerNo") != null) {

            clickedID = data.getStringExtra("customerNo");
            new ProfileAdditionalDetails().execute(clickedID);

            edit_about.setVisibility(View.GONE);
            edit_hobbies.setVisibility(View.GONE);
            edit_horoscope.setVisibility(View.GONE);
            edit_lifestyle.setVisibility(View.GONE);

            Toast.makeText(getContext(), clickedID, Toast.LENGTH_SHORT).show();
        }




        new ProfileAdditionalDetails().execute(clickedID);

        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SimilarActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        edit_horoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak=24;
                BottomSheetDialogFragment btm= new BottomSheet(2);
                btm.show(getFragmentManager(),btm.getTag());
            }
        });

        edit_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak=21;
                BottomSheetDialogFragment btm= new BottomSheet(2);
                btm.show(getFragmentManager(),btm.getTag());
            }
        });

        edit_hobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak=22;
                BottomSheetDialogFragment btm= new BottomSheet(2);
                btm.show(getFragmentManager(),btm.getTag());
            }
        });


        edit_lifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak=23;
                BottomSheetDialogFragment btm= new BottomSheet(2);
                btm.show(getFragmentManager(),btm.getTag());
            }
        });




        return mview;
    }





    private class ProfileAdditionalDetails extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params){

            String cus = params[0];
            AndroidNetworking.post( URL + "profileAdditionalDetails")
                    .addBodyParameter("customerNo", cus)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener(){
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray result = response.getJSONArray(0);

                                aboutMe.setText(result.getString(0));
                                hobbies.setText(result.getString(1));
                                eatingHabits.setText(result.getString(2));
                                drinkingHabits.setText(result.getString(3));
                                smokingHabits.setText(result.getString(4));

                                String bl= result.getString(5) + " at " + result.getString(6);

                                birthtime.setText(bl);
                                gotra.setText(result.getString(7));
                                manglik.setText(result.getString(8));
                                matchHoroscope.setText(result.getString(9));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        @Override
                        public void onError(ANError error) {
                            Log.d(TAG, "onError: errr ------------- " + error.toString());
                            // handle error
                        }

                        });
            return null;
        }

    }

    public int getCasebreak()
    {
        return this.casebreak;
    }
}
