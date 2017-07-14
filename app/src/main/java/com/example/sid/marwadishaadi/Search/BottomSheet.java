package com.example.sid.marwadishaadi.Search;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.App;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionAdapter;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionModel;
import com.example.sid.marwadishaadi.Filter;
import com.example.sid.marwadishaadi.PlacesAdapter;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Signup.Signup_Partner_Preferences_Fragment;
import com.example.sid.marwadishaadi.User_Profile.ProfileAdditionalDetailsFragment;
import com.example.sid.marwadishaadi.User_Profile.ProfileFamilyDetailsFragment;
import com.example.sid.marwadishaadi.User_Profile.ProfilePersonalDetailsFragment;
import com.example.sid.marwadishaadi.User_Profile.UserProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.sid.marwadishaadi.Search.Search.AIList;
import static com.example.sid.marwadishaadi.Search.Search.CastList;
import static com.example.sid.marwadishaadi.Search.Search.annualincome;
import static com.example.sid.marwadishaadi.Search.Search.countannualincome;
import static com.example.sid.marwadishaadi.Search.Search.countfamilystatus;
import static com.example.sid.marwadishaadi.Search.Search.countmaritalstatus;
import static com.example.sid.marwadishaadi.Search.Search.countphysicalstatus;
import static com.example.sid.marwadishaadi.Search.Search.countspinnerCastSearch;
import static com.example.sid.marwadishaadi.Search.Search.familystatus;
import static com.example.sid.marwadishaadi.Search.Search.familystatusList;
import static com.example.sid.marwadishaadi.Search.Search.maritalstatus;
import static com.example.sid.marwadishaadi.Search.Search.maritalstatusList;
import static com.example.sid.marwadishaadi.Search.Search.physicalstatus;
import static com.example.sid.marwadishaadi.Search.Search.physicalstatusList;
import static com.example.sid.marwadishaadi.Search.Search.spinnerCastSearch;
import static com.example.sid.marwadishaadi.Signup.Signup_Partner_Preferences_Fragment.preferenceAnnualincome;
import static com.example.sid.marwadishaadi.Signup.Signup_Partner_Preferences_Fragment.preferenceMaritalstatus;
import static com.example.sid.marwadishaadi.Signup.Signup_Partner_Preferences_Fragment.preferencePhysicalstatus;
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.prefannualincome;
import static com.example.sid.marwadishaadi.User_Profile.ProfileAdditionalDetailsFragment.SOME_INTENT_FILTER_NAME;



public class BottomSheet extends BottomSheetDialogFragment {

    public static ArrayList<SuggestionModel> sm;
    public static SuggestionAdapter SA;
    public static String err, success;
    private static String s;
    private static EditText fname, lname, id;
    private static int count = 0, size = 0;
    public ProgressDialog dialog;
    protected PlacesAdapter placesAdapter;


    //Edit Educational details
    Spinner editEducation;
    TextView eduDegree;
    Button updateEducation;
    String e, hd, in;
    EditText eduInstituteLocation;

    //Edit Professional details of User Profile
    Spinner designation, annualIncome, occupation;
    TextView companyName;
    Button updateProfession;
    String d, cn, o, cl, ai;
    EditText aboutMe;
    Button aboutMe_update;
    String am;
    AutoCompleteTextView companyLocation;

    //Hobbies additional details
    EditText hobbies;

    //About Me additional details
    Button hobbies_update;
    String h;

    //LifeStyle additional details
    Spinner eatingHabit, smokingHabit, drinkingHabit;
    String eh, dh, sh;
    Button lifestyleUpdate;

    //Horoscope additional details
    Spinner manglik, matchHoroscope;
    EditText birthTime, gotra;
    String m, bt, bp, g, mh;
    Button horoscopeUpdate;
    AutoCompleteTextView birthPlace;

    //Relation Family Details
    Spinner relation;
    EditText relativeName, relativeOccupation, relativeMobile;
    Button relationUpdate;
    String r, rn, ro, rl, rm;
    String[] array;
    AutoCompleteTextView relativeLocation;


    Bundle bundle;
    private int content;
    private View contentView;
    private UsersAdapter adapter;
    private int coun;
    private String customer_id;


    public BottomSheet() {

    }

    public BottomSheet(int i, String[] array) {
        this.array = array;
        if (i == 111) {
            content = 111;
        } else if (i == 112) {
            content = 5;
        } else if (i == 113) {
            content = 113;
        }

    }


    public BottomSheet(int i) {
        if (i == 0) {
            Search search = new Search();
            content = search.getCasebreak();
        } else if (i == 1) {
            ProfilePersonalDetailsFragment profile_personal_detailsFragment = new ProfilePersonalDetailsFragment();
            content = profile_personal_detailsFragment.getCasebreak();
        } else if (i == 2) {
            ProfileAdditionalDetailsFragment profile_additional_detailsFragment = new ProfileAdditionalDetailsFragment();
            content = profile_additional_detailsFragment.getCasebreak();
        } else if (i == 4) {
            Filter mfilter = new Filter();
            content = Filter.getCasebreak();
        } else if (i == 7) {
            Signup_Partner_Preferences_Fragment signupPartnerPreferencesFragment = new Signup_Partner_Preferences_Fragment();

            content = signupPartnerPreferencesFragment.getCasebreak();
        } else {
            ProfileFamilyDetailsFragment profile_family_detailsFragment = new ProfileFamilyDetailsFragment();
            content = profile_family_detailsFragment.getCasebreak();
        }
    }


    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        switch (content) {

            //--------------------- EDIT PROFILE PREFERENCES ------------------------------------
            case 112:
//                contentView = viewGetterEditPref(R.array.aincome_array, array);
                Toast.makeText(getContext(), "incase why this is called ", Toast.LENGTH_LONG).show();
                count = 2;
                break;

            case 0:
                break;

            //----------------- SEARCH ----------------------------------------------------------

            case 1:
                contentView = viewGetter(R.array.caste_array);
                count = 2;
                break;

            case 2:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_search, null);
                count = 1;
                sm = new ArrayList<>();
                SA = new SuggestionAdapter(getContext(), sm, SearchResultsActivity.recyclerView);
                break;

            case 3:
                contentView = viewGetter(R.array.status_search_array);
                count = 2;
                break;

            case 4:
                contentView = viewGetter(R.array.fstatus_search_array);
                count = 2;
                break;

            case 5:
                contentView = viewGetter(R.array.aincome_search_array);
                count = 2;
                break;

            case 6:
                contentView = viewGetter(R.array.physicalstatus_search_array);
                count = 2;
                break;


            //------------------ EDIT PROFILE -PERSONAL DETAILS -----------------------------

            case 12:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_education, null);
                SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
                customer_id = sharedpref.getString("customer_id", null);
                Log.d(TAG, "setupDialog: case 12 ------------------------ " + customer_id);
                editEducation = (Spinner) contentView.findViewById(R.id.edit_education);
                eduDegree = (TextView) contentView.findViewById(R.id.edit_highest_degree);
                updateEducation = (Button) contentView.findViewById(R.id.education_update);
                eduInstituteLocation = (EditText) contentView.findViewById(R.id.edit_institute_name);


//                UPDATE tbl_user SET education="Engineer", edu_degree="B.E.", college_name="Dr. Babasaheb Ambedakar" WHERE customer_no="A1028"


                new FetchPersonalEducationDetails().execute();

                updateEducation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        e = editEducation.getSelectedItem().toString();
                        hd = eduDegree.getText().toString();
                        in = eduInstituteLocation.getText().toString();
                        new EditPersonalEducationDetails().execute();
                        Intent i = new Intent(getContext(), UserProfileActivity.class);
                        startActivity(i);

                    }


                });

                break;
            case 13:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_profession, null);
                sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
                customer_id = sharedpref.getString("customer_id", null);
//                count = 2;


                designation = (Spinner) contentView.findViewById(R.id.profession);
                annualIncome = (Spinner) contentView.findViewById(R.id.edit_annual_income);
                companyName = (TextView) contentView.findViewById(R.id.job_company);
                occupation = (Spinner) contentView.findViewById(R.id.occupation);
                updateProfession = (Button) contentView.findViewById(R.id.professionUpdate);

                companyLocation = (AutoCompleteTextView) contentView.findViewById(R.id.job_location);
                companyLocation.setThreshold(1);
                placesAdapter = new PlacesAdapter(getContext(), R.layout.bottom_sheet_profession, R.id.job_location, App.placeslist);
                companyLocation.setAdapter(placesAdapter);

                new FetchProfessionalEducationDetails().execute();

                updateProfession.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        d = designation.getSelectedItem().toString();
                        ai = annualIncome.getSelectedItem().toString();

                        ai = ai.replace("L", "00000");


                        cn = companyName.getText().toString();
                        cl = companyLocation.getText().toString();
                        o = occupation.getSelectedItem().toString();

                        new EditPersonalProfessionalDetails().execute();
                        Intent i = new Intent(getContext(), UserProfileActivity.class);
                        startActivity(i);

                    }
                });

                break;


            // ----------------- EDIT PROFILE - ADDITIONAL DETAILS --------------------------

            case 21:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_about_me, null);
//                count = 2;

                sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
                customer_id = sharedpref.getString("customer_id", null);

                aboutMe = (EditText) contentView.findViewById(R.id.about_me);
                aboutMe_update = (Button) contentView.findViewById(R.id.aboutMe_update);

                new FetchAdditionalAboutMeDetails().execute();

                aboutMe_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        am = aboutMe.getText().toString();

                        new EditAdditionalAboutMeDetails().execute();
                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("aboutMe", am);

                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                        dialog.dismiss();
                    }
                });


                break;
            case 22:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_hobbies, null);

                sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
                customer_id = sharedpref.getString("customer_id", null);
                hobbies = (EditText) contentView.findViewById(R.id.hobbies);
                hobbies_update = (Button) contentView.findViewById(R.id.hobbiesUpdate);

                new FetchAdditionalHobbiesDetails().execute();

                hobbies_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        h = hobbies.getText().toString();

                        new EditAdditionalHobbiesDetails().execute();

                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("hobbies", h);

                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                        dialog.dismiss();

                    }
                });

                break;
            case 23:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_lifestyle, null);


                sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
                customer_id = sharedpref.getString("customer_id", null);

                eatingHabit = (Spinner) contentView.findViewById(R.id.eating_habits);
                drinkingHabit = (Spinner) contentView.findViewById(R.id.drinking_habit);
                smokingHabit = (Spinner) contentView.findViewById(R.id.smoking_habits);
                lifestyleUpdate = (Button) contentView.findViewById(R.id.lifestyle_update);

                new FetchAdditionalLifeStyleDetails().execute();

                lifestyleUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        if (eatingHabit.getSelectedItem().toString().contains("Select")) {
                            eh = "";
                        } else {
                            eh = eatingHabit.getSelectedItem().toString();
                        }
                        if (drinkingHabit.getSelectedItem().toString().contains("Do")) {
                            dh = "";
                        } else {
                            dh = drinkingHabit.getSelectedItem().toString();
                        }

                        if (smokingHabit.getSelectedItem().toString().contains("Do")) {
                            sh = "";
                        } else {
                            sh = smokingHabit.getSelectedItem().toString();
                        }

                        new EditAdditionalLifeStyleDetails().execute();
                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("eatingHabit", eh);
                        someIntent.putExtra("drinkingHabit", dh);
                        someIntent.putExtra("smokingHabit", sh);


                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                        dialog.dismiss();

                    }
                });


                break;
            case 24:

                contentView = View.inflate(getContext(), R.layout.bottom_sheet_horoscope, null);
//                count = 2;

                sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
                customer_id = sharedpref.getString("customer_id", null);
                birthTime = (EditText) contentView.findViewById(R.id.birthtime);
                birthTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR_OF_DAY);
                        int min = c.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), mTimeSetListener, hour, min, DateFormat.is24HourFormat(getActivity()));
                        timePickerDialog.setTitle("Select Time");
                        timePickerDialog.show();
                    }
                });

                gotra = (EditText) contentView.findViewById(R.id.gotra);
                manglik = (Spinner) contentView.findViewById(R.id.manglik);
                matchHoroscope = (Spinner) contentView.findViewById(R.id.match_horoscope);
                horoscopeUpdate = (Button) contentView.findViewById(R.id.horoscope_update);

                // autcomplete location field
                birthPlace = (AutoCompleteTextView) contentView.findViewById(R.id.birth_location);
                birthPlace.setThreshold(1);
                placesAdapter = new PlacesAdapter(getContext(), R.layout.bottom_sheet_horoscope, R.id.birth_location, App.placeslist);
                birthPlace.setAdapter(placesAdapter);

                new FetchAdditionalHoroscopeDetails().execute();

                horoscopeUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bt = birthTime.getText().toString();
                        bp = birthPlace.getText().toString();
                        g = gotra.getText().toString();
                        m = manglik.getSelectedItem().toString();
                        mh = matchHoroscope.getSelectedItem().toString();

                        new EditAdditionalHoroscopeDetails();
                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("birthTime", bt);
                        someIntent.putExtra("birthPlace", bp);
                        someIntent.putExtra("gotra", g);
                        someIntent.putExtra("manglik", m);
                        someIntent.putExtra("matchHoroscope", mh);


                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                        dialog.dismiss();


                    }
                });

                break;

            //-------------------- EDIT PROFILE - FAMILY DETAILS -----------------------------

            case 32:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_relatives, null);


                sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
                customer_id = sharedpref.getString("customer_id", null);

                relation = (Spinner) contentView.findViewById(R.id.relation);
                relativeName = (EditText) contentView.findViewById(R.id.relative_name);
                relativeOccupation = (EditText) contentView.findViewById(R.id.relative_occupation);
                relativeMobile = (EditText) contentView.findViewById(R.id.relative_mobile);
                relationUpdate = (Button) contentView.findViewById(R.id.relation_update);


                relativeLocation = (AutoCompleteTextView) contentView.findViewById(R.id.bottom_sheet_relative_location);
                relativeLocation.setThreshold(1);
                placesAdapter = new PlacesAdapter(getContext(), R.layout.bottom_sheet_relatives, R.id.bottom_sheet_relative_location, App.placeslist);
                relativeLocation.setAdapter(placesAdapter);


                new FetchFamilyRelationDetails().execute();

                relationUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rn = relativeName.getText().toString();
                        rl = relativeLocation.getText().toString();
                        ro = relativeOccupation.getText().toString();
                        r = relation.getSelectedItem().toString();
                        rm = relativeMobile.getText().toString();

                        new EditFamilyRelationDetails().execute();
                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("relation", r);
                        someIntent.putExtra("relationName", rn);
                        someIntent.putExtra("relationOccupation", ro);
                        someIntent.putExtra("relationLocation", rl);
                        someIntent.putExtra("relationMobile", rm);


                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                        dialog.dismiss();


                    }
                });


                break;

            default:
                contentView = View.inflate(getContext(), R.layout.custom_list_view, null);
                break;

        }


        // SETTING VIEW

        dialog.setContentView(contentView);


        //------------------ SEARCH -> BY ID/NAME ---------------------------------------

        if (count == 1) {


            Button srch = (Button) contentView.findViewById(R.id.search_by_id_name_search_button);
            srch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    fname = (EditText) contentView.findViewById(R.id.search_by_first_name);
                    lname = (EditText) contentView.findViewById(R.id.search_by_last_name);
                    id = (EditText) contentView.findViewById(R.id.search_by_id);


                    String strfname, strlname, strid;
                    strfname = fname.getText().toString();
                    strlname = lname.getText().toString();
                    strid = id.getText().toString();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String gender = sharedPreferences.getString("gender", null);
                    if (gender.contains("Male")) {
                        gender = "Female";
                    } else {
                        gender = "Male";
                    }
                    if (strid.trim().isEmpty() && strlname.trim().isEmpty() && strfname.trim().isEmpty()) {

                        Toast.makeText(getContext(), " Please use either ID or Name ", Toast.LENGTH_SHORT).show();
                        fname.setText("");
                        lname.setText("");
                        id.setText("");

                    }
                      else if (!strid.trim().isEmpty()) {
//and tbl_login.user_deleted='0' removed from below queries

                        new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_city.City_name ,tbl_user.occup_designation  from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no inner join tbl_city on tbl_user.city=tbl_city.City_id  INNER JOIN tbl_login ON tbl_user.customer_no=tbl_login.customer_no where ( tbl_login.user_active ='Yes'  ) and  tbl_user.customer_no=\"" + strid.trim() + "\"; ");
                        } else if ((!strlname.trim().isEmpty() && !strfname.trim().isEmpty())) {
                        Log.d(TAG, "onClick: -hhhh-----------inhere"+strfname+"-----"+strlname+"------"+strid);

                        new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_city.City_name,tbl_user.occup_designation  from tbl_user  INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no inner join tbl_city on tbl_user.city=tbl_city.City_id INNER JOIN tbl_login ON tbl_user.customer_no=tbl_login.customer_no where ( tbl_login.user_active ='Yes' ) and  (tbl_user.first_name=\"" + strfname.trim() + "\"and tbl_user.surname=\"" + strlname.trim() + "\" ) order by created_on asc ;");
                        } else if ((!strlname.trim().isEmpty() || !strfname.trim().isEmpty())) {

                            if (!strlname.trim().isEmpty()) {
                                new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_city.City_name,tbl_user.occup_designation from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no inner join tbl_city on tbl_user.city=tbl_city.City_id INNER JOIN tbl_login ON tbl_user.customer_no=tbl_login.customer_no where ( tbl_login.user_active ='Yes' ) and  ( tbl_user.surname=\"" + strlname.trim() + "\"  and tbl_user.gender='" + gender + "') order by created_on asc ;");
                            } else {
                                new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_city.City_name,tbl_user.occup_designation from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no inner join tbl_city on tbl_user.city=tbl_city.City_id INNER JOIN tbl_login ON tbl_user.customer_no=tbl_login.customer_no where ( tbl_login.user_active ='Yes' ) and  tbl_user.first_name=\"" + strfname.trim() + "\"  order by created_on asc;");
                            }
                        } else {
                            Toast.makeText(getContext(), "Search detail can't be empty", Toast.LENGTH_SHORT).show();
                        }

                }
            });
        } else if (count == 2) {
            coun = 0;
            Button btn = (Button) contentView.findViewById(R.id.tick);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> result = new ArrayList<>();
                    for (User p : adapter.getBox()) {
                        if (p.box) {
                            coun++;
                            result.add(p.name);

                            Log.d(TAG, "onClick: result is " + result.toString());

                        }
                    }
                    switch (content) {
                        case 1:
                            String community_text= result.toString().replace("[", "").replace("]", "");
                            spinnerCastSearch.setText(community_text);
                            CastList = result;
                            countspinnerCastSearch = coun;
                            break;
                        case 3:
                            if (getContext().toString().contains("Signup.AdvancedSignupDetailsActivity")) {

                                preferenceMaritalstatus.setText(result.toString());
                                break;
                            } else {
                                String marital_text= result.toString().replace("[", "").replace("]", "");
                                maritalstatus.setText(marital_text);
                                maritalstatusList = result;
                                countmaritalstatus = coun;
                                break;
                            }
                        case 4:
                            String family_text= result.toString().replace("[", "").replace("]", "");
                            familystatus.setText(family_text);
                            familystatusList = result;
                            countfamilystatus = coun;
                            break;
                        case 5:
                            Log.d(TAG, "onClick: context for ann is " + getContext().toString());
                            if (getContext().toString().contains("Signup.AdvancedSignupDetailsActivity")) {
                                preferenceAnnualincome.setText(result.toString());
                            } else if (getContext().toString().contains("User_Profile.Edit_User_Profile.EditPreferencesActivity")) {

                                prefannualincome.setText(result.toString().replace("[", "").replace("]", ""));
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("prefai", MODE_PRIVATE).edit();
                                editor.putString("ai", result.toString());
                                editor.apply();


                            } else {

                                annualincome.setText(result.toString().replace("[","").replace("]",""));
                                AIList = result;
                                countannualincome = coun;
                            }
                            break;
                        case 6:
                            if (getContext().toString().contains("Signup.AdvancedSignupDetailsActivity")) {
                                preferencePhysicalstatus.setText(result.toString());
                                break;

                            } else {
                                String physical_text= result.toString().replace("[", "").replace("]", "");
                                physicalstatus.setText(physical_text);
                                physicalstatusList = (result);
                                countphysicalstatus = coun;
                                break;
                            }


                    }
                    dialog.dismiss();
                }
            });
        }
    }

    public String getS() {
        return s;
    }
    public void setS(String s) {
        BottomSheet.s = s;
    }
    private View viewGetter(int array) {

        ArrayList<User> arrayOfUsers = new ArrayList<>();
        String[] str = getResources().getStringArray(array);
        for (int i = 0; i < str.length; i++) {
            User user = new User(str[i], "null");
            arrayOfUsers.add(user);
        }
        adapter = new UsersAdapter(getContext(), arrayOfUsers);
        View view = View.inflate(getContext(), R.layout.custom_list_view, null);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);
        return view;
    }
    private View viewGetterEditPref(int array, String[] arr) {


        SharedPreferences sharedpref = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        customer_id = sharedpref.getString("customer_id", null);

        ArrayList<User> arrayOfUsers = new ArrayList<>();
        boolean b = true;
        String[] str = getResources().getStringArray(array);
        for (int i = 0; i < str.length; i++) {
            if(Arrays.asList(arr).indexOf(str[i]) == -1)
            {
                b=false;
            }
            else {
                b = true;
            }
            User user = new User(str[i], "null", false);
            user.setBox(true);
            arrayOfUsers.add(user);
        }
        adapter = new UsersAdapter(getContext(), arrayOfUsers);
        View view = View.inflate(getContext(), R.layout.custom_list_view, null);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        Toast.makeText(getContext(), "here", Toast.LENGTH_LONG).show();
        listView.setAdapter(adapter);
        return view;
    }


    class BackNd extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(getContext());
            dialog.setMessage("Please Wait...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "response query is  ----------" + strings[0]);
            AndroidNetworking.post("http://208.91.199.50:5000/searchById")
                    .addBodyParameter("query", strings[0])
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        Vector<String> customers=new Vector<String>();
                        @Override
                        public void onResponse(JSONArray response) {
                            String s = response.toString();
                            Log.e(TAG, "onResponse:----------------" + s);
                            size = response.length();
                            sm.clear();
                            for (int i = 0; i < response.length(); i++) {

                                try {
                                                    /*SuggestionModel(int age, String imgAdd, String name, String cusId, String highDeg, String workLoc, String height, String comapany, String annInc, String mariSta, String hometown, String designation)
*/

                                    JSONArray user = response.getJSONArray(i);
                                    if((customers.indexOf(user.getString(3))==-1)){
                                        customers.add(user.getString(3));
                                        Calendar calender = Calendar.getInstance();
                                        int year = calender.get(Calendar.YEAR);
                                    /*SuggestionModel suggestionModel = new SuggestionModel(Integer.parseInt(age), "http://www.marwadishaadi.com/uploads/cust_" + customerNo + "/thumb/" + imageUrl, name, customerNo, education, occupationLocation, height, occupationCompany, annualIncome, maritalStatus, hometown, occupationDesignation, favouriteStatus, interestStatus);*/
                                        SuggestionModel suggestionModel;
                                        if(user.get(8).equals("")){
                                            suggestionModel = new SuggestionModel(year - (int) user.get(0), "http://www.marwadishaadi.com/uploads/cust_" + user.get(3).toString() + "/thumb/" + user.get(1).toString(), user.get(2).toString(), user.get(3).toString(), user.get(4).toString(), user.get(5).toString(), user.get(6).toString(), user.get(7).toString(), "No Income mentioned.", user.get(9).toString(), user.get(10).toString(), user.get(11).toString(), "0", "Not");
                                        }else{
                                            suggestionModel = new SuggestionModel(year - (int) user.get(0), "http://www.marwadishaadi.com/uploads/cust_" + user.get(3).toString() + "/thumb/" + user.get(1).toString(), user.get(2).toString(), user.get(3).toString(), user.get(4).toString(), user.get(5).toString(), user.get(6).toString(), user.get(7).toString(), user.get(8).toString(), user.get(9).toString(), user.get(10).toString(), user.get(11).toString(), "0", "Not");
                                        }
                                        sm.add(suggestionModel);
//                                        Log.e(TAG, "onResponse: --- sm details are " + sm.get(i).getCusId());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Log.e(TAG, "run: sm size is *************"+sm.size());
                                    Intent i = new Intent(getContext(), SearchResultsActivity.class);
                                    i.putExtra("COUNT", size);
                                    i.putExtra("which","second");
                                    startActivity(i);
                                }
                            });
                            success = "success";

                        }

                        @Override
                        public void onError(ANError error) {
//                        Toast.makeText(BottomSheet.this," ", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onError: --------------------- error is " + error);
                            err = "Network Error, Check Connection";
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    class EditPersonalEducationDetails extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: teeeeeeeeeeeeeeeeeeeeeest " + customer_id);
            AndroidNetworking.post("http://208.91.199.50:5000/editPersonalEducationDetails")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("education", e)
                    .addBodyParameter("edu_degree", hd)
                    .addBodyParameter("college_name", in)
                    .setTag(this)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            return null;
        }
    }
    class FetchPersonalEducationDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/profilePersonalDetails")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(0);
                                String[] education_array = getResources().getStringArray(R.array.education_array);
                                for (String s : education_array) {
                                    if (jsonArray.getString(13).equals(s)) {
                                        editEducation.setSelection(Arrays.asList(education_array).indexOf(s));
                                    }
                                }
                                eduDegree.setText(jsonArray.getString(14));
                                eduInstituteLocation.setText(jsonArray.getString(15));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


            return null;
        }
    }
    class FetchProfessionalEducationDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/profilePersonalDetails")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(0);
                                String[] designationArray = getResources().getStringArray(R.array.designation_array);
                                String[] occupationArray = getResources().getStringArray(R.array.occupation_array);
                                String[] annualIncomeArray = getResources().getStringArray(R.array.aincome_array);
                                Log.d(TAG, "onResponse: profession is " + jsonArray.getString(17));


                                for (String s : designationArray) {
                                    if (jsonArray.getString(20).equals(s)) {
                                        designation.setSelection(Arrays.asList(designationArray).indexOf(s));
                                    }
                                }

                                for (String s : occupationArray) {
                                    if (jsonArray.getString(17).equals(s)) {
                                        occupation.setSelection(Arrays.asList(occupationArray).indexOf(s));
                                    }
                                }

                                String annualI = jsonArray.getString(18);
                                annualI = annualI.replaceAll("[^-?0-9]+", " ");
                                List<String> incomeArray = Arrays.asList(annualI.trim().split(" "));
                                Log.d(TAG, "onResponse: income array is " + incomeArray);
                                if (jsonArray.getString(18).contains("Upto")) {
                                    annualI = "Upto 3L";
                                } else if (jsonArray.getString(18).contains("Above")) {
                                    annualI = "Above 50L";

                                } else if (incomeArray.size() == 3) {
                                    Log.d(TAG, "onResponse: when three");
                                    double first = Integer.parseInt(incomeArray.get(0)) / 100000.0;
                                    double second = Integer.parseInt(incomeArray.get(2)) / 100000.0;
                                    annualI = (int) first + "L - " + (int) second + "L";
                                } else {
                                    annualI = "No Income mentioned.";
                                }


                                for (String s : annualIncomeArray) {
                                    if (annualI.equals(s)) {
                                        annualIncome.setSelection(Arrays.asList(annualIncomeArray).indexOf(s));
                                    }
                                }
                                Log.d(TAG, "onResponse: AI is ------" + annualI);


                                companyName.setText(jsonArray.getString(19));
                                companyLocation.setText(jsonArray.getString(21));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


            return null;
        }
    }
    class EditPersonalProfessionalDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://208.91.199.50:5000/editPersonalProfessionDetails")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("occupation", o)
                    .addBodyParameter("annualIncome", ai)
                    .addBodyParameter("companyName", cn)
                    .addBodyParameter("companyLocation", cl)
                    .addBodyParameter("designation", d)
                    .setTag(this)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            return null;
        }
    }
    class FetchAdditionalAboutMeDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/profileAdditionalDetails")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(0);
                                aboutMe.setText(jsonArray.getString(0));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


            return null;
        }
    }
    class EditAdditionalAboutMeDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://208.91.199.50:5000/editAdditionalDetailsAboutMe")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("aboutMe", am)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            return null;
        }
    }
    class FetchAdditionalHobbiesDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/profileAdditionalDetails")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(0);
                                hobbies.setText(jsonArray.getString(1));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


            return null;
        }
    }
    class EditAdditionalHobbiesDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://208.91.199.50:5000/editAdditionalDetailsHobbies")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("hobbies", h)
                    .setTag(this)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            return null;
        }
    }
    class FetchAdditionalLifeStyleDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/profileAdditionalDetails")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(0);

                                String[] dietArray = getResources().getStringArray(R.array.diet_array);

                                for (String s : dietArray) {
                                    if (jsonArray.getString(2).equals(s)) {
                                        eatingHabit.setSelection(Arrays.asList(dietArray).indexOf(s));
                                        Log.d(TAG, "onResponse: ---------eating----" + s);
                                    }
                                }

                                String[] drinkingArray = getResources().getStringArray(R.array.drinking_array);

                                for (String s : drinkingArray) {
                                    if (jsonArray.getString(3).equals(s)) {
                                        drinkingHabit.setSelection(Arrays.asList(drinkingArray).indexOf(s));
                                        Log.d(TAG, "onResponse: ---------drinking----" + s);

                                    }
                                }

                                String[] smokingArray = getResources().getStringArray(R.array.smoking_array);

                                for (String s : smokingArray) {
                                    if (jsonArray.getString(4).equals(s)) {
                                        smokingHabit.setSelection(Arrays.asList(smokingArray).indexOf(s));
                                        Log.d(TAG, "onResponse: ---------smoking----" + s);

                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


            return null;
        }
    }
    class EditAdditionalLifeStyleDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://208.91.199.50:5000/editAdditionalDetailsLifestyle")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("smokingHabit", sh)
                    .addBodyParameter("eatingHabit", eh)
                    .addBodyParameter("drinkingHabit", dh)
                    .setTag(this)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            return null;
        }
    }
    class FetchAdditionalHoroscopeDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/profileAdditionalDetails")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(0);

                                birthTime.setText(jsonArray.getString(5));
                                gotra.setText(jsonArray.getString(7));
                                birthPlace.setText(jsonArray.getString(6));


                                String[] manglikArray = getResources().getStringArray(R.array.manglik_array);

                                for (String s : manglikArray) {
                                    if (jsonArray.getString(8).equals(s)) {
                                        manglik.setSelection(Arrays.asList(manglikArray).indexOf(s));
                                    }
                                }

                                String[] horoscopeArray = getResources().getStringArray(R.array.horoscope_array);

                                for (String s : horoscopeArray) {
                                    if (jsonArray.getString(9).equals(s)) {
                                        matchHoroscope.setSelection(Arrays.asList(horoscopeArray).indexOf(s));

                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


            return null;
        }
    }
    class EditAdditionalHoroscopeDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://208.91.199.50:5000/editAdditionalDetailsHoroscope")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("birthTime", bt)
                    .addBodyParameter("birthPlace", bp)
                    .addBodyParameter("gotra", g)
                    .addBodyParameter("manglik", m)
                    .addBodyParameter("matchHoroscope", mh)
                    .setTag(this)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            return null;
        }
    }
    class FetchFamilyRelationDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://208.91.199.50:5000/profileFamilyDetails")
                    .addBodyParameter("customerNo", customer_id)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {

                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray(0);
                                String[] relationArray = getResources().getStringArray(R.array.relation_name_array);

                                for (String s : relationArray) {
                                    if (jsonArray.getString(10).equals(s)) {
                                        relation.setSelection(Arrays.asList(relationArray).indexOf(s));
                                    }
                                }
                                relativeName.setText(jsonArray.getString(11));
                                relativeOccupation.setText(jsonArray.getString(12));
                                relativeLocation.setText(jsonArray.getString(13));
                                relativeMobile.setText(jsonArray.getString(14));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });


            return null;
        }
    }
    class EditFamilyRelationDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://208.91.199.50:5000/editFamilyDetailsRelation")
                    .addBodyParameter("customerNo", customer_id)
                    .addBodyParameter("relation", r)
                    .addBodyParameter("relativeName", rn)
                    .addBodyParameter("relativeOccupation", ro)
                    .addBodyParameter("relativeLocation", rl)
                    .addBodyParameter("relativeMobile", rm)
                    .setTag(this)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });

            return null;
        }
    }

    private void updateDisplay(EditText birthtime,int hour,int minute) {
        birthtime.setText(
                new StringBuilder()
                        .append(pad(hour)).append(":")
                        .append(pad(minute)));
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    int pHour = hourOfDay;
                    int pMinute = minute;
                    updateDisplay(birthTime,pHour,pMinute);
                }
            };

};








