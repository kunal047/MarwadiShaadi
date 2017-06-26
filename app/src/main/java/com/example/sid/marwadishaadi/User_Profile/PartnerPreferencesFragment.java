package com.example.sid.marwadishaadi.User_Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

<<<<<<< HEAD

=======
>>>>>>> 53ce3f69073faeed4c224e2c49f7e71abf118eb9
public class PartnerPreferencesFragment extends Fragment {

    private TextView edit_prefs;
    private Button similar;

    private TextView age,height,build,complexion,physicalStatus,highestDegree,occup,maritalStatus,annualIncome,city;
    public PartnerPreferencesFragment() {
        // Required empty public constructor
    }

<<<<<<< HEAD



=======
>>>>>>> 53ce3f69073faeed4c224e2c49f7e71abf118eb9
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview= inflater.inflate(R.layout.fragment_partner_preferences, container, false);
        edit_prefs = (TextView) mview.findViewById(R.id.partner_prefs_clear);
        similar = (Button) mview.findViewById(R.id.similar);

        age = (TextView)mview.findViewById(R.id.age);
        height = (TextView)mview.findViewById(R.id.height);
        complexion = (TextView)mview.findViewById(R.id.complexion);
        build = (TextView)mview.findViewById(R.id.build);
        physicalStatus = (TextView)mview.findViewById(R.id.physical_status);
        city = (TextView)mview.findViewById(R.id.city);
        highestDegree = (TextView)mview.findViewById(R.id.highest_degree);
        occup = (TextView)mview.findViewById(R.id.occup);
        maritalStatus = (TextView)mview.findViewById(R.id.marital_status);
        annualIncome = (TextView)mview.findViewById(R.id.annual_income);

        new PartnerPreference().execute();

        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SimilarActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });
        edit_prefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), EditPreferencesActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });

        return mview;
    }


    class PartnerPreference extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://192.168.43.143:5050/profilePartnerPreferences")
                    .addBodyParameter("customerNo", "A1028")
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                JSONArray array;
                                String a = response.getString(0)+" yrs to "+response.getString(1)+" yrs";
                                age.setText(a);

                                double feet = Double.parseDouble(response.getString(2)) / 30.48 ;
                                double inches = (Double.parseDouble(response.getString(2)) / 2.54) - ((int) feet * 12);
                                double feet1 = Double.parseDouble(response.getString(3)) / 30.48;
                                double inches1 = (Double.parseDouble(response.getString(3)) / 2.54) - ((int) feet * 12);
                                String h = (int)feet+" ft "+inches+" inch to "+(int)feet1+" ft "+inches1+" inch ";
                                height.setText(h);

                                array = response.getJSONArray(4);
                                String c = array.getString(0);
                                for (int i = 1; i<array.length();i++)
                                {
                                    c = c+ "/ "+array.getString(i);
                                }
                                complexion.setText(c);

                                array = response.getJSONArray(5);
                                String b = array.getString(0);
                                for (int i = 1; i<array.length();i++)
                                {
                                    b = b+ "/ "+array.getString(i);
                                }
                                build.setText(b);

                                physicalStatus.setText(response.getString(6));
                                city.setText(response.getString(7));

                                array=response.getJSONArray(8);
                                String hd=array.getString(0);
                                for (int i = 1; i<array.length();i++)
                                {
                                    hd = hd+ "/ "+array.getString(i);
                                }
                                highestDegree.setText(hd);

                                array = response.getJSONArray(9);
                                String o=array.getString(0);
                                for (int i = 1; i<array.length();i++)
                                {
                                    o = o+ "/ "+array.getString(i);
                                }
                                occup.setText(o);

                                array = response.getJSONArray(10);
                                String m = array.getString(0);
                                for (int i = 1; i<array.length();i++)
                                {
                                    m = m+ "/ "+array.getString(i);
                                }
                                maritalStatus.setText(m);





                                annualIncome.setText(response.getString(11));
                            }
                            catch (JSONException e)
                            {
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

        public String income(String aincome) {

            String annualI = aincome;
            annualI = annualI.replaceAll("[^-?0-9]+", " ");
            List<String> incomeArray = Arrays.asList(annualI.trim().split(" "));
            Log.d(TAG, "onResponse: income array is " + incomeArray);
            if (annualI.contains("Upto")) {
                annualI = "Upto 3L";
            } else if (annualI.contains("Above")) {
                annualI = "Above 50L";

            } else if (incomeArray.size() == 3) {
                Log.d(TAG, "onResponse: when three");
                double first = Integer.parseInt(incomeArray.get(0)) / 100000.0;
                double second = Integer.parseInt(incomeArray.get(2)) / 100000.0;
                annualI = (int) first + "L - " + (int) second + "L";
            } else {
                annualI = "No Income mentioned.";
            }
            return  annualI;
        }


    }

<<<<<<< HEAD


=======
>>>>>>> 53ce3f69073faeed4c224e2c49f7e71abf118eb9
}
