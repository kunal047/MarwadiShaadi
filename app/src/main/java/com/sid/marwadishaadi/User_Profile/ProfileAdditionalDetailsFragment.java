package com.sid.marwadishaadi.User_Profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.sid.marwadishaadi.CacheHelper;
import com.sid.marwadishaadi.Constants;
import com.sid.marwadishaadi.Membership.UpgradeMembershipActivity;
import com.sid.marwadishaadi.R;
import com.sid.marwadishaadi.Search.BottomSheet;
import com.sid.marwadishaadi.Similar_Profiles.SimilarActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getCacheDir;

public class ProfileAdditionalDetailsFragment extends Fragment {

    public static final String SOME_INTENT_FILTER_NAME = "SOME_INTENT_FILTER_NAME";
    private static int casebreak;
    TextView aboutMe, hobbies, eatingHabits, drinkingHabits, smokingHabits, birthtime, gotra, manglik, matchHoroscope;
    private TextView edit_about;
    private TextView edit_hobbies;
    private TextView edit_lifestyle;
    private TextView edit_horoscope;
    private Button similar;
    private CardView mCardViewAboutMe, mCardViewHobbies;
    private File cache = null;
    private boolean isAlreadyLoadedFromCache = false;
    private String clickedID, customer_id;
    private boolean isPaidMember = false;
    private BroadcastReceiver someBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String hobby = bundle.getString("hobbies");
            String am = bundle.getString("aboutMe");
            String eh = bundle.getString("eatingHabit");
            String dh = bundle.getString("drinkingHabit");
            String sh = bundle.getString("smokingHabit");
            String bt = bundle.getString("birthTime");
            String bp = bundle.getString("birthPlace");
            String g = bundle.getString("gotra");
            String m = bundle.getString("manglik");
            String mh = bundle.getString("matchHoroscope");

            if (hobby != null)
                hobbies.setText(hobby);

            if (am != null)
                aboutMe.setText(am);

            if (eh != null) {
                eatingHabits.setText(eh);
            }

            if (dh != null)
                drinkingHabits.setText(dh);

            if (sh != null)
                smokingHabits.setText(sh);


            if (bt != null || bp != null)
                birthtime.setText(bt + " at " + bp);

            if (g != null)
                gotra.setText(g);
            else {
                gotra.setVisibility(View.GONE);
            }

            if (m != null)
                manglik.setText(m);

            if (mh != null)
                matchHoroscope.setText(mh);


        }
    };

    public ProfileAdditionalDetailsFragment() {
        // Required empty public constructor
    }

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

        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        clickedID = customer_id;


        mCardViewAboutMe = mview.findViewById(R.id.cardViewAboutMe);
        mCardViewHobbies = mview.findViewById(R.id.cardViewHobbies);

        edit_about = mview.findViewById(R.id.aboutme_clear);
        edit_hobbies = mview.findViewById(R.id.hobbies_clear);
        edit_lifestyle = mview.findViewById(R.id.lifestyle_clear);
        edit_horoscope = mview.findViewById(R.id.horoscope_clear);
        similar = mview.findViewById(R.id.similar);

        aboutMe = mview.findViewById(R.id.about_me);
        hobbies = mview.findViewById(R.id.hobbies);
        eatingHabits = mview.findViewById(R.id.eating_habits);
        drinkingHabits = mview.findViewById(R.id.drinking_habit);
        smokingHabits = mview.findViewById(R.id.smoking_habits);
        birthtime = mview.findViewById(R.id.birthtime_location);
        gotra = mview.findViewById(R.id.gotra);
        manglik = mview.findViewById(R.id.manglik);
        matchHoroscope = mview.findViewById(R.id.match_horoscope);

        try {

            String[] array = getResources().getStringArray(R.array.communities);

            int communityLength = sharedpref.getInt("cal", 0);
            if (customer_id != null && array.length > 0) {

                for (int i = 0; i < communityLength; i++) {

                    if (sharedpref.getString(array[i], "No").contains("Yes")) {
                        isPaidMember = true;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }


        Intent data = getActivity().getIntent();
        String from = data.getStringExtra("from");

        aboutMe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0 && customer_id != clickedID) {
                    mCardViewAboutMe.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 && customer_id != clickedID) {
                    mCardViewAboutMe.setVisibility(View.GONE);
                }


            }
        });

        hobbies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    mCardViewHobbies.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 && customer_id != clickedID) {
                    mCardViewHobbies.setVisibility(View.GONE);
                }
            }
        });

        eatingHabits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("Diet"))) {
                    eatingHabits.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("Diet"))) {
                    eatingHabits.setVisibility(View.GONE);
                }

            }
        });
        drinkingHabits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Drinking habit)"))) {
                    drinkingHabits.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Drinking habit)"))) {
                    drinkingHabits.setVisibility(View.GONE);
                }

            }
        });

        smokingHabits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Smoking habit)"))) {
                    smokingHabits.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Smoking habit)"))) {
                    smokingHabits.setVisibility(View.GONE);
                }

            }
        });


        birthtime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && customer_id != clickedID) {
                    birthtime.setVisibility(View.GONE);
                } else if (customer_id == clickedID) {

                    // show as it is

                } else if (!isPaidMember) {
                    float radius = birthtime.getTextSize() / 3;
                    BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                    birthtime.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    birthtime.getPaint().setMaskFilter(filter);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 && customer_id != clickedID) {
                    birthtime.setVisibility(View.GONE);
                } else if (customer_id == clickedID) {

                    // show as it is

                } else if (!isPaidMember) {
                    float radius = birthtime.getTextSize() / 3;
                    BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                    birthtime.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    birthtime.getPaint().setMaskFilter(filter);

                }

            }
        });

        birthtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isPaidMember) {

                    Toast.makeText(getContext(), "This feature is only available for paid members", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), UpgradeMembershipActivity.class);
                    getContext().startActivity(intent);
                }
            }
        });

        gotra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Gotra)"))) {
                    gotra.setVisibility(View.GONE);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Gotra)"))) {
                    gotra.setVisibility(View.GONE);
                }

            }
        });
        manglik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Manglik)"))) {
                    manglik.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Manglik)"))) {
                    manglik.setVisibility(View.GONE);
                }

            }
        });
        matchHoroscope.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Should match horoscope ?)"))) {
                    matchHoroscope.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (customer_id != clickedID && (s.length() == 0 || s.toString().trim().equals("(Should match horoscope ?)"))) {
                    matchHoroscope.setVisibility(View.GONE);
                }

            }
        });


        if (data.getStringExtra("customerNo") != null) {

            clickedID = data.getStringExtra("customerNo");

            edit_about.setVisibility(View.GONE);
            edit_hobbies.setVisibility(View.GONE);
            edit_horoscope.setVisibility(View.GONE);
            edit_lifestyle.setVisibility(View.GONE);

        }


        if (customer_id.equals(clickedID)) {
            similar.setVisibility(View.GONE);
        }
        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SimilarActivity.class);
                i.putExtra("similarOf", clickedID);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        edit_horoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 24;
                BottomSheetDialogFragment btm = new BottomSheet(2);
                btm.show(getFragmentManager(), btm.getTag());
            }
        });

        edit_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 21;
                BottomSheetDialogFragment btm = new BottomSheet(2);
                btm.show(getFragmentManager(), btm.getTag());
            }
        });

        edit_hobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 22;
                BottomSheetDialogFragment btm = new BottomSheet(2);
                btm.show(getFragmentManager(), btm.getTag());
            }
        });


        edit_lifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 23;
                BottomSheetDialogFragment btm = new BottomSheet(2);
                btm.show(getFragmentManager(), btm.getTag());
            }
        });


        cache = new File(getCacheDir() + "/" + "additionalprofile" + clickedID + ".srl");


        // loading cached copy
        String res = CacheHelper.retrieve("additionalprofile", cache);
        if (!res.equals("")) {
            try {

                isAlreadyLoadedFromCache = true;

                // storing cache hash
                CacheHelper.saveHash(getContext(), CacheHelper.generateHash(res), "additionalprofile");

                // displaying it
                JSONArray response = new JSONArray(res);
                // Toast.makeText(getContext(), "Loading from cache....", Toast.LENGTH_SHORT).show();
                parseAdditionalProfile(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        new ProfileAdditionalDetails().execute(clickedID);


        return mview;
    }

    private void parseAdditionalProfile(JSONArray response) {

        try {
            JSONArray result = response.getJSONArray(0);

            aboutMe.setText(result.getString(0));
            hobbies.setText(result.getString(1));
            String eh = result.getString(2) + " Diet";
            eatingHabits.setText(eh);
            String dh = result.getString(3) + " (Drinking habit)";
            drinkingHabits.setText(dh);
            String sh = result.getString(4) + " (Smoking habit)";
            smokingHabits.setText(sh);
            String bl;
            if (result.getString(5).length() == 0 && result.getString(6).length() == 0) {
                bl = "";
            } else if (result.getString(5).length() == 0) {
                bl = "Born at " + result.getString(6);
            } else if (result.getString(6).length() == 0) {
                bl = "Born on " + result.getString(5);
            } else {
                bl = "Born on " + result.getString(5) + " at " + result.getString(6);

            }

            birthtime.setText(bl);
            String g = result.getString(7) + " (Gotra)";
            gotra.setText(g);
            String m = result.getString(8) + " (Manglik)";
            manglik.setText(m);
            String mh = result.getString(9) + " (Should match horoscope ?)";
            matchHoroscope.setText(mh);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void loadedFromNetwork(JSONArray response) {


        //saving fresh in cache
        CacheHelper.save("additionalprofile", response.toString(), cache);

        // marking cache
        isAlreadyLoadedFromCache = true;

        // storing latest cache hash
        CacheHelper.saveHash(getContext(), CacheHelper.generateHash(response.toString()), "additionalprofile");

        // displaying it
        parseAdditionalProfile(response);

    }

    public int getCasebreak() {
        return casebreak;
    }

    private class ProfileAdditionalDetails extends AsyncTask<String, Void, Void> {
        @Override

        protected Void doInBackground(String... strings) {
            String cus = strings[0];
            AndroidNetworking.post(Constants.AWS_SERVER + "/profileAdditionalDetails")
                    .addBodyParameter("customerNo", cus)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            //

                            // if no change in data
                            if (isAlreadyLoadedFromCache) {

                                String latestResponseHash = CacheHelper.generateHash(response.toString());
                                String cacheResponseHash = CacheHelper.retrieveHash(getContext(), "additionalprofile");

                                if (cacheResponseHash != null && latestResponseHash.equals(cacheResponseHash)) {

                                    return;
                                } else {

                                    // hash not matched
                                    loadedFromNetwork(response);
                                }
                            } else {
                                // first time load
                                loadedFromNetwork(response);
                            }

                        }

                        @Override
                        public void onError(ANError error) {

                            // handle error
                        }

                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
