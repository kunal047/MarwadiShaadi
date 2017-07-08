package com.example.sid.marwadishaadi.User_Profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
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
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPersonalDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.URL;


public class ProfilePersonalDetailsFragment extends Fragment {

    private static int casebreak;
    TextView name_age, maritalStatus, birthdate, gender, address, mobileNo, caste, height, weight, complexion_build, physicalStatus, education, educationDegree, collegeName_collegeLocation, currentOccupation, designation_companyName, companyLocation, annualIncome;
    private TextView edit_individual;
    private TextView edit_education;
    private TextView edit_profession;
    private Button similar;
    private String clickedID = customer_id;


    public ProfilePersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mview = inflater.inflate(R.layout.fragment_profile__personal__details, container, false);

        edit_individual = (TextView) mview.findViewById(R.id.individual_clear);
        edit_education = (TextView) mview.findViewById(R.id.edu_clear);
        edit_profession = (TextView) mview.findViewById(R.id.profession_clear);
        similar = (Button) mview.findViewById(R.id.similar);

        name_age = (TextView) mview.findViewById(R.id.name_age);
        maritalStatus = (TextView) mview.findViewById(R.id.marital_status);
        birthdate = (TextView) mview.findViewById(R.id.birthdate);
        gender = (TextView) mview.findViewById(R.id.gender);
        address = (TextView) mview.findViewById(R.id.address);
        mobileNo = (TextView) mview.findViewById(R.id.contact_mob);
        caste = (TextView) mview.findViewById(R.id.caste);
        height = (TextView) mview.findViewById(R.id.height);
        weight = (TextView) mview.findViewById(R.id.weight);
        complexion_build = (TextView) mview.findViewById(R.id.complexion_build);
        physicalStatus = (TextView) mview.findViewById(R.id.physical_status);
        education = (TextView) mview.findViewById(R.id.degree);
        educationDegree = (TextView) mview.findViewById(R.id.edu_degree);
        collegeName_collegeLocation = (TextView) mview.findViewById(R.id.collegeName_collegeLocation);
        currentOccupation = (TextView) mview.findViewById(R.id.current_occup);
        designation_companyName = (TextView) mview.findViewById(R.id.occupDesignation_occupCompany);
        companyLocation = (TextView) mview.findViewById(R.id.occup_location);
        annualIncome = (TextView) mview.findViewById(R.id.annual_income);

        Intent data = getActivity().getIntent();
        String from = data.getStringExtra("from");

        if (data.getStringExtra("customerNo") != null) {

            clickedID = data.getStringExtra("customerNo");
            new PersonalDetails().execute(clickedID);

            Toast.makeText(getContext(), clickedID, Toast.LENGTH_SHORT).show();
        }


        if ("suggestion".equals(from)|"recent".equals(from)|"reverseMatching".equals(from)|"favourites".equals(from)|"interestReceived".equals(from)|"interestSent".equals(from)) {
            edit_individual.setVisibility(View.GONE);
            edit_education.setVisibility(View.GONE);
            edit_profession.setVisibility(View.GONE);
            Toast.makeText(getContext(), clickedID, Toast.LENGTH_SHORT).show();
        }



        new PersonalDetails().execute(clickedID);

        similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SimilarActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        edit_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditPersonalDetailsActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        edit_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 12;


                BottomSheetDialogFragment btm = new BottomSheet(1);
                btm.show(getFragmentManager(), btm.getTag());
            }
        });


        edit_profession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casebreak = 13;
                BottomSheetDialogFragment btm = new BottomSheet(1);
                btm.show(getFragmentManager(), btm.getTag());
            }
        });

        return mview;
    }

    public int getAge(int DOByear, int DOBmonth, int DOBday) {

        int age;

        final Calendar calenderToday = Calendar.getInstance();
        int currentYear = calenderToday.get(Calendar.YEAR);
        int currentMonth = 1 + calenderToday.get(Calendar.MONTH);
        int todayDay = calenderToday.get(Calendar.DAY_OF_MONTH);

        age = currentYear - DOByear;

        if (DOBmonth > currentMonth) {
            --age;
        } else if (DOBmonth == currentMonth) {
            if (DOBday > todayDay) {
                --age;
            }
        }
        return age;
    }

    public int getCasebreak() {
        return this.casebreak;
    }

    class PersonalDetails extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            String cus = strings[0];
            AndroidNetworking.post(URL + "profilePersonalDetails")
                    .addBodyParameter("customerNo", cus)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, "onResponse: ******************in personal details");
                            try {
                                JSONArray array = response.getJSONArray(0);

                                String dateOfBirth = array.getString(2);
                                // Thu, 18 Jan 1990 00:00:00 GMT
                                DateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
                                Date date = null;
                                try {
                                    date = formatter.parse(dateOfBirth);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(date);
                                String[] str = {"January",
                                        "February",
                                        "March",
                                        "April",
                                        "May",
                                        "June",
                                        "July",
                                        "August",
                                        "September",
                                        "October",
                                        "November",
                                        "December"};
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                String formatedDate = cal.get(Calendar.DATE) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR);
                                String strDate = cal.get(Calendar.DATE) + " " + str[(cal.get(Calendar.MONTH))] + " " + cal.get(Calendar.YEAR);

                                String[] partsOfDate = formatedDate.split("-");
                                int day = Integer.parseInt(partsOfDate[0]);
                                int month = Integer.parseInt(partsOfDate[1]);
                                int year = Integer.parseInt(partsOfDate[2]);
                                int a = getAge(year, month, day);

                                String na = array.getString(0) + " " + array.getString(1) + ", " + a + " yrs";
                                name_age.setText(na);
                                maritalStatus.setText(array.getString(3));
                                birthdate.setText(strDate);
                                gender.setText(array.getString(4));
                                address.setText(array.getString(5));
                                mobileNo.setText(array.getString(6));

                                String[] c = array.getString(7).split("");
                                String cast = "";
                                if (c[1].equals("A")) {
                                    cast = "Agarwal";
                                } else if (c[1].equals("K")) {
                                    cast = "Khandelwal";
                                } else if (c[1].equals("J")) {
                                    cast = "Jain";
                                } else if (c[1].equals("M")) {
                                    cast = "Maheshwari";
                                } else if (c[1].equals("O")) {
                                    cast = "Other";
                                }


                                caste.setText(cast);


                                height.setText(array.getString(8));
                                String w = "weighs " + array.getString(9) + " kgs";
                                weight.setText(w);

                                String cb = array.getString(10) + ", " + array.getString(11);

                                complexion_build.setText(cb);
                                physicalStatus.setText(array.getString(12));
                                education.setText(array.getString(13));
                                educationDegree.setText(array.getString(14));

                                String cnl = array.getString(15) + ", " + array.getString(16);
                                collegeName_collegeLocation.setText(cnl);
                                currentOccupation.setText(array.getString(17));

                                String dc = array.getString(20) + " at " + array.getString(19);
                                designation_companyName.setText(dc);

                                String cl = "Located in " + array.getString(21);
                                companyLocation.setText(cl);


                                String annualI = array.getString(18);
                                annualI = annualI.replaceAll("[^-?0-9]+", " ");
                                List<String> incomeArray = Arrays.asList(annualI.trim().split(" "));
                                Log.d(TAG, "onResponse: income array is " + incomeArray);
                                if (array.getString(18).contains("Upto")) {
                                    annualI = "Upto 3L";
                                } else if (array.getString(18).contains("Above")) {
                                    annualI = "Above 50L";

                                } else if (incomeArray.size() == 3) {
                                    Log.d(TAG, "onResponse: when three");
                                    double first = Integer.parseInt(incomeArray.get(0)) / 100000.0;
                                    double second = Integer.parseInt(incomeArray.get(2)) / 100000.0;
                                    annualI = (int) first + "L - " + (int) second + "L";
                                } else {
                                    annualI = "No Income mentioned.";
                                }
                                Log.d(TAG, "onResponse: Annual Income ----------- " + array.getString(18));

                                annualIncome.setText(annualI);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError error) {

                        }
                    });

            return null;
        }
    }

    //Edit Educational details of User Personal Details


}
