package com.example.sid.marwadishaadi.User_Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Similar_Profiles.SimilarActivity;
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class PartnerPreferencesFragment extends Fragment {

    private TextView edit_prefs;
    private Button similar;
    private TextView age, height, build, complexion, physicalStatus, highestDegree, occup, maritalStatus, annualIncome, city;

    private String clickedID, customer_id;
    private LinearLayout complexionLayout;

    public PartnerPreferencesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_partner_preferences, container, false);

        boolean called = true;

        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

        clickedID = customer_id;

        edit_prefs = (TextView) mview.findViewById(R.id.partner_prefs_clear);
        similar = (Button) mview.findViewById(R.id.similar);

        age = (TextView) mview.findViewById(R.id.age);
        height = (TextView) mview.findViewById(R.id.height);

        complexionLayout = (LinearLayout) mview.findViewById(R.id.complexionLayout);
        complexion = (TextView) mview.findViewById(R.id.complexion);
        build = (TextView) mview.findViewById(R.id.build);
        physicalStatus = (TextView) mview.findViewById(R.id.physical_status);
        city = (TextView) mview.findViewById(R.id.city);
        highestDegree = (TextView) mview.findViewById(R.id.highest_degree);
        occup = (TextView) mview.findViewById(R.id.occup);
        maritalStatus = (TextView) mview.findViewById(R.id.marital_status);
        annualIncome = (TextView) mview.findViewById(R.id.annual_income);

        Intent data = getActivity().getIntent();
        String from = data.getStringExtra("from");

        if (data.getStringExtra("customerNo") != null) {
            called = false;
            clickedID = data.getStringExtra("customerNo");
            new PartnerPreference().execute(clickedID);

        }


        if ("suggestion".equals(from) | "recent".equals(from) | "reverseMatching".equals(from) | "favourites".equals(from) | "interestReceived".equals(from) | "interestSent".equals(from) | "similar".equals(from)) {

            edit_prefs.setVisibility(View.GONE);
            edit_prefs.setVisibility(View.GONE);

        }

        if (called) {
            new PartnerPreference().execute(clickedID);
        }
        if(customer_id.equals(clickedID)){
        similar.setVisibility(View.GONE);}
        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SimilarActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        edit_prefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), EditPreferencesActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });

        return mview;
    }

    class PartnerPreference extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String cus = strings[0];
            AndroidNetworking.post("http://208.91.199.50:5000/profilePartnerPreferences")

                    .addBodyParameter("customerNo", cus)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(final JSONArray response) {

                            try {
                                String str;
                                String a = response.getString(0) + " yrs to " + response.getString(1) + " yrs";
                                age.setText(a);

//                                    double feet = Double.parseDouble(response.getString(2)) / 30.48;
//                                    double inches = (Double.parseDouble(response.getString(2)) / 2.54) - ((int) feet * 12);
//                                    String heightFrom = (int) feet + "ft " + (int) inches + "in ";
//
//                                    feet = Double.parseDouble(response.getString(3)) / 30.48;
//                                    inches = (Double.parseDouble(response.getString(3)) / 2.54) - ((int) feet * 12);
//                                    String heightTo = (int) feet + "ft " + (int) inches + "in";

                                String h = response.getString(2) + " to " + response.getString(3);

                                height.setText(h);


                                str = response.getString(4).replace("[", "").replace("]", "").replace("\"", "");
                                final String c = str;

                                str = response.getString(5).replace("[", "").replace("]", "").replace("\"", "");
                                final String b = str;

                                final String ps = response.getString(6);


                                if (response.getString(7) != null && response.getString(7).trim().length() == 0) {
                                    city.setVisibility(View.GONE);
                                } else {
                                    city.setText(response.getString(7));
                                }

                                str = response.getString(8).replace("[", "").replace("]", "").replace("\"", "").replace("/", " ");

                                final String hd = str;



                                str = response.getString(9).replace("[", "").replace("]", "").replace("\"", "");

                                final String o = str;

                                final String ms = response.getString(10);

                                final String ai = response.getString(11);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (c != null && c.trim().length() == 0) {
                                            complexionLayout.setVisibility(View.GONE);
                                        } else {
                                            complexion.setText(c);
                                        }

                                        if (b != null && b.trim().length() == 0) {
                                            build.setVisibility(View.GONE);
                                        } else {
                                            build.setText(b);
                                        }

                                        if ( ps != null && ps.trim().length() == 0) {
                                            physicalStatus.setVisibility(View.GONE);
                                        } else {
                                            physicalStatus.setText(ps);
                                        }

                                        if (o != null && o.trim().length() == 0) {
                                            occup.setVisibility(View.GONE);
                                        } else {
                                            occup.setText(o);
                                        }

                                        if (ms != null && ms.trim().length() == 0) {
                                            maritalStatus.setVisibility(View.GONE);

                                        } else {
                                            maritalStatus.setText(ms);
                                        }

                                        if (hd != null && hd.trim().length() == 0) {
                                            highestDegree.setVisibility(View.GONE);
                                        } else {
                                            highestDegree.setText(hd);
                                        }

                                        if ( ai != null && ai.replace("[", "").replace("]", "").replace("\"", "").trim().length() == 0) {
                                           annualIncome.setVisibility(View.GONE);

                                        } else {
                                            String annualIn = ai.replace("[", "").replace("]", "").replace("\"", "").replace("000000", "0L").replace("00000", "L");
                                            annualIncome.setText(annualIn);
                                        }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError error) {

                            // handle error
                        }
                    });

            return null;

        }


        public String income(String aincome) {

            String annualI = aincome;
            annualI = annualI.replaceAll("[^-?0-9]+", " ");
            List<String> incomeArray = Arrays.asList(annualI.trim().split(" "));

            if (annualI.contains("Upto")) {
                annualI = "Upto 3L";
            } else if (annualI.contains("Above")) {
                annualI = "Above 50L";

            } else if (incomeArray.size() == 3) {

                double first = Integer.parseInt(incomeArray.get(0)) / 100000.0;
                double second = Integer.parseInt(incomeArray.get(2)) / 100000.0;
                annualI = (int) first + "L - " + (int) second + "L";
            } else {
                annualI = "No Income mentioned.";
            }
            return annualI;
        }


    }


}
