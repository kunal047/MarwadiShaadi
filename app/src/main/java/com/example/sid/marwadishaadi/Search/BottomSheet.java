package com.example.sid.marwadishaadi.Search;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionAdapter;
import com.example.sid.marwadishaadi.Dashboard_Suggestions.SuggestionModel;
import com.example.sid.marwadishaadi.Filter;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Signup.Signup_Partner_Preferences_Fragment;
import com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity;
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

import static android.content.ContentValues.TAG;
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
import static com.example.sid.marwadishaadi.User_Profile.Edit_User_Profile.EditPreferencesActivity.prefannualincome;
import static com.example.sid.marwadishaadi.User_Profile.ProfileAdditionalDetailsFragment.SOME_INTENT_FILTER_NAME;

/**
 * Created by vivonk on 01-06-2017.
 */


public class BottomSheet extends BottomSheetDialogFragment {

    private int content;
    private View contentView;
    private UsersAdapter adapter;
    private static String s;
    private int coun;
    private static EditText fname, lname, id;
    private static int count = 0, size = 0;
    public static ArrayList<SuggestionModel> sm;
    public static SuggestionAdapter SA;
    public static String err, success;
    public ProgressDialog dialog;
    //Edit Educational details
    Spinner editEducation;
    TextView eduDegree, eduInstituteLocation;
    Button updateEducation;

    String e, hd, in;
    //-------------------------------------------
//Edit Professional details of User Profile
    Spinner designation, annualIncome, occupation;
    TextView companyName, companyLocation;
    Button updateProfession;

    String d, cn, o, cl, ai;

    //-------------------------------------------


    //About Me additional details

    EditText aboutMe;
    Button aboutMe_update;
    String am;

    //Hobbies additional details
    EditText hobbies;
    Button hobbies_update;
    String h;

    //LifeStyle additional details
    Spinner eatingHabit,smokingHabit,drinkingHabit;
    String eh,dh,sh;
    Button lifestyleUpdate;

    //Horoscope additional details
    Spinner manglik,matchHoroscope;
    EditText birthTime,birthPlace,gotra;
    String m,bt,bp,g,mh;
    Button horoscopeUpdate;

    //Relation Family Details
    Spinner relation;
    EditText relativeName, relativeOccupation, relativeLocation, relativeMobile;
    Button relationUpdate;

    String r,rn,ro,rl,rm;
    String[] array;
    Bundle bundle;

    public BottomSheet() {

    }

    public BottomSheet(int i ,String[] array)
    {
        this.array=array;
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
            content = mfilter.getCasebreak();
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
            case 112:contentView=viewGetterEditPref(R.array.aincome_array,array);
                Toast.makeText(getContext(),"incase",Toast.LENGTH_LONG).show();

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
                editEducation = (Spinner) contentView.findViewById(R.id.edit_education);
                eduDegree = (TextView) contentView.findViewById(R.id.edit_highest_degree);
                eduInstituteLocation = (TextView) contentView.findViewById(R.id.edit_institute_name);
                updateEducation = (Button) contentView.findViewById(R.id.education_update);


//                UPDATE tbl_user SET education="Engineer", edu_degree="B.E.", college_name="Dr. Babasaheb Ambedakar" WHERE customer_no="A1028"


                new FetchPersonalEducationDetails().execute();

                updateEducation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        e = editEducation.getSelectedItem().toString();
                        hd = eduDegree.getText().toString();
                        in = eduInstituteLocation.getText().toString();
                        Log.d(TAG, "setupDialog: education is-----------" + e);
                        Log.d(TAG, "setupDialog: degree is--------" + hd);
                        Log.d(TAG, "setupDialog: institute is-------" + in);
                        new EditPersonalEducationDetails().execute();
                        Intent i = new Intent(getContext(), UserProfileActivity.class);
                        startActivity(i);

                    }


                });

                break;
            case 13:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_profession, null);

                designation = (Spinner) contentView.findViewById(R.id.profession);
                annualIncome = (Spinner) contentView.findViewById(R.id.edit_annual_income);
                companyName = (TextView) contentView.findViewById(R.id.job_company);
                companyLocation = (TextView) contentView.findViewById(R.id.job_location);
                occupation = (Spinner) contentView.findViewById(R.id.occupation);
                updateProfession = (Button) contentView.findViewById(R.id.professionUpdate);

                new FetchProfessionalEducationDetails().execute();

                updateProfession.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        d = designation.getSelectedItem().toString();
                        ai= annualIncome.getSelectedItem().toString();

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

                aboutMe = (EditText) contentView.findViewById(R.id.about_me);
                aboutMe_update=(Button) contentView.findViewById(R.id.aboutMe_update);

                new FetchAdditionalAboutMeDetails().execute();

                aboutMe_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        am=aboutMe.getText().toString();

                        new EditAdditionalAboutMeDetails().execute();
                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("aboutMe",am);

                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                        dialog.dismiss();
                    }
                });


                break;
            case 22:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_hobbies, null);

                hobbies = (EditText) contentView.findViewById(R.id.hobbies);
                hobbies_update=(Button) contentView.findViewById(R.id.hobbiesUpdate);

                new FetchAdditionalHobbiesDetails().execute();

                hobbies_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        h=hobbies.getText().toString();

                        new EditAdditionalHobbiesDetails().execute();

                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("hobbies",h);

                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                        dialog.dismiss();

                    }
                });

                break;
            case 23:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_lifestyle, null);
                eatingHabit = (Spinner)contentView.findViewById(R.id.eating_habits);
                drinkingHabit = (Spinner)contentView.findViewById(R.id.drinking_habit);
                smokingHabit = (Spinner)contentView.findViewById(R.id.smoking_habits);
                lifestyleUpdate=(Button)contentView.findViewById(R.id.lifestyle_update);

                new FetchAdditionalLifeStyleDetails().execute();

                lifestyleUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        eh=eatingHabit.getSelectedItem().toString();
                        dh=drinkingHabit.getSelectedItem().toString();
                        sh=smokingHabit.getSelectedItem().toString();

                        new EditAdditionalLifeStyleDetails().execute();
                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("eatingHabit",eh);
                        someIntent.putExtra("drinkingHabit",dh);
                        someIntent.putExtra("smokingHabit",sh);


                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                        dialog.dismiss();

                    }
                });


                break;
            case 24:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_horoscope, null);

                birthTime = (EditText)contentView.findViewById(R.id.birthtime);
                birthPlace = (EditText)contentView.findViewById(R.id.birth_location);
                gotra = (EditText)contentView.findViewById(R.id.gotra);
                manglik = (Spinner)contentView.findViewById(R.id.manglik);
                matchHoroscope = (Spinner)contentView.findViewById(R.id.match_horoscope);
                horoscopeUpdate = (Button)contentView.findViewById(R.id.horoscope_update);

                new FetchAdditionalHoroscopeDetails().execute();

                horoscopeUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bt=birthTime.getText().toString();
                        bp=birthPlace.getText().toString();
                        g=gotra.getText().toString();
                        m =manglik.getSelectedItem().toString();
                        mh=matchHoroscope.getSelectedItem().toString();

                        new EditAdditionalHoroscopeDetails();
                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("birthTime",bt);
                        someIntent.putExtra("birthPlace",bp);
                        someIntent.putExtra("gotra",g);
                        someIntent.putExtra("manglik", m);
                        someIntent.putExtra("matchHoroscope",mh);



                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                        dialog.dismiss();


                    }
                });

                break;

            //-------------------- EDIT PROFILE - FAMILY DETAILS -----------------------------

            case 32:
                contentView = View.inflate(getContext(), R.layout.bottom_sheet_relatives, null);

                relation = (Spinner) contentView.findViewById(R.id.relation);
                relativeName=(EditText) contentView.findViewById(R.id.relative_name);
                relativeOccupation=(EditText)contentView.findViewById(R.id.relative_occupation);
                relativeLocation=(EditText)contentView.findViewById(R.id.relative_location);
                relativeMobile=(EditText)contentView.findViewById(R.id.relative_mobile);
                relationUpdate=(Button)contentView.findViewById(R.id.relation_update);

                new FetchFamilyRelationDetails().execute();

                relationUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rn=relativeName.getText().toString();
                        rl=relativeLocation.getText().toString();
                        ro=relativeOccupation.getText().toString();
                        r=relation.getSelectedItem().toString();
                        rm=relativeMobile.getText().toString();

                        new EditFamilyRelationDetails().execute();
                        Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                        someIntent.putExtra("relation",r);
                        someIntent.putExtra("relationName",rn);
                        someIntent.putExtra("relationOccupation",ro);
                        someIntent.putExtra("relationLocation",rl);
                        someIntent.putExtra("relationMobile",rm);



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
                    if (!strid.trim().isEmpty() & (!strlname.trim().isEmpty() | !strfname.trim().isEmpty())) {
                        Toast.makeText(getContext(), " Please use either ID or Name ", Toast.LENGTH_SHORT).show();
                        fname.setText("");
                        lname.setText("");
                        id.setText("");
                    } else if (!strid.trim().isEmpty()) {
                        new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_user.city,tbl_user.occup_designation  from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no and tbl_user.customer_no=\"" + strid + "\" ; ");
                        if (success == "success") {
                            if (size == 0) {
                                Toast.makeText(getContext(), "No result found on these query", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(getContext(), SearchResultsActivity.class);
                                i.putExtra("COUNT", size);
                                size = 0;
                                fname.setText("");
                                lname.setText("");
                                id.setText("");
                                startActivity(i);

                            }
                        } else {
                            Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                        }
                    } else if ((!strlname.trim().isEmpty() & !strfname.trim().isEmpty())) {
                        new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_user.city,tbl_user.occup_designation  from tbl_user  INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no and tbl_user.first_name=\" " + strfname + "\"and tbl_user.surname=\"" + strlname + "\" order by created_on asc ;");
                        if (success == "success") {
                            if (size == 0) {
                                Toast.makeText(getContext(), "No result found on these query", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent i = new Intent(getContext(), SearchResultsActivity.class);
                                i.putExtra("COUNT", size);
                                size = 0;
                                fname.setText("");
                                lname.setText("");
                                id.setText("");
                                startActivity(i);

                            }
                        } else {
                            Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                        }
                    } else if ((!strlname.trim().isEmpty() | !strfname.trim().isEmpty())) {

                        if (!strlname.trim().isEmpty()) {
                            new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_user.city,tbl_user.occup_designation from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no and tbl_user.surname=\"" + strlname + "\"   order by created_on asc ;");
                            if (success == "success") {
                                if (size == 0) {

                                    Toast.makeText(getContext(), "No result found on these query", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent i = new Intent(getContext(), SearchResultsActivity.class);
                                    i.putExtra("COUNT", size);
                                    Log.d(TAG, "onClick: sizzw is ************************************* " + Integer.toString(size));
                                    fname.setText("");
                                    lname.setText("");
                                    id.setText("");
                                    size = 0;
                                    startActivity(i);
                                }
                            } else {
                                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            new BackNd().execute("select YEAR(tbl_user.birthdate),tbl_user_files.file_name,tbl_user.first_name,tbl_user.customer_no,tbl_user.edu_degree,tbl_user.occup_location,tbl_user.height,tbl_user.occup_company,tbl_user.anuual_income,tbl_user.marrital_status,tbl_user.city,tbl_user.occup_designation from tbl_user INNER JOIN tbl_user_files ON tbl_user.customer_no=tbl_user_files.customer_no and tbl_user.first_name=\"" + strfname + "\"   order by created_on asc;");


                            if (success == "success") {
                                if (size == 0) {
                                    Toast.makeText(getContext(), "No results found! ", Toast.LENGTH_SHORT).show();
                                } else {

                                    Intent i = new Intent(getContext(), SearchResultsActivity.class);
                                    i.putExtra("COUNT", size);
                                    fname.setText("");
                                    lname.setText("");
                                    id.setText("");
                                    size = 0;
                                    i.putExtra("which", "second");
                                    startActivity(i);
                                }
                            } else {
                                Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
                            }
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
                    List<String> result = new ArrayList<String>();
                    for (User p : adapter.getBox()) {
                        if (p.box) {
                            coun++;
                            result.add(p.name);

                            Log.d(TAG, "onClick: result is " + result.toString());

                        }
                    }
                    switch (content) {
                        case 1:
                            spinnerCastSearch.setText(result.toString());
                            CastList = result;
                            countspinnerCastSearch = coun;
                            break;
                        case 3:
                            maritalstatus.setText(result.toString());
                            maritalstatusList = result;
                            countmaritalstatus = coun;
                            break;
                        case 4:
                            familystatus.setText(result.toString());
                            familystatusList = result;
                            countfamilystatus = coun;
                            break;
                        case 5:
                            if(getContext().toString().contains("Edit_User_Profile.EditPreferencesActivity"))
                            {
                                prefannualincome.setText(result.toString());
                                Intent someIntent = new Intent(SOME_INTENT_FILTER_NAME);
                                someIntent.putExtra("annualIncome",result.toString());

                                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(someIntent);
                                dialog.dismiss();


                            }else{
                            prefannualincome.setText(result.toString());
                            AIList = result;
                            countannualincome = coun;
                            }
                            break;
                        case 6:
                            physicalstatus.setText(result.toString());
                            physicalstatusList = (result);
                            countphysicalstatus = coun;
                            break;
                    }
                    dialog.dismiss();
                }
            });


        }
    }

    class BackNd extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Please Wait...");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "response query is  ----------" + strings[0]);
            AndroidNetworking.post("http://192.168.43.61:5050/searchById")
                    .addBodyParameter("query", strings[0])
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            String s = response.toString();
                            Log.d(TAG, "onResponse:----------------" + s);
                            size = response.length();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONArray user = response.getJSONArray(i);

                                    Calendar calender = Calendar.getInstance();
                                    int year = calender.get(Calendar.YEAR);
                                    SuggestionModel suggestionModel = new SuggestionModel(year - (int) user.get(0), "http://www.marwadishaadi.com/uploads/cust_" + user.get(3).toString() + "/thumb/" + user.get(1).toString(), user.get(2).toString(), user.get(3).toString(), user.get(4).toString(), user.get(5).toString(), user.get(6).toString(), user.get(7).toString(), user.get(8).toString(), user.get(9).toString(), user.get(10).toString(), user.get(11).toString(), user.get(12).toString(), user.get(13).toString());
                                    sm.add(suggestionModel);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            success = "success";

                        }

                        @Override
                        public void onError(ANError error) {
                            Log.d(TAG, "onError: --------------------- error is " + error);
                            err = "Network Error, Check Connection";
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
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
        return view;
    }

    private View viewGetterEditPref(int array,String[] arr) {

        ArrayList<User> arrayOfUsers = new ArrayList<>();
        boolean b=true;
        String[] str = getResources().getStringArray(array);
        for (int i = 0; i < str.length; i++) {
           /* if(Arrays.asList(arr).indexOf(str[i]) == -1)
            {
                b=false;
            }
            else
                b=true;*/

            User user = new User(str[i], "null",false);
            user.setBox(true);
            arrayOfUsers.add(user);
        }
        adapter = new UsersAdapter(getContext(), arrayOfUsers);
        View view = View.inflate(getContext(), R.layout.custom_list_view, null);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        Toast.makeText(getContext(),"here",Toast.LENGTH_LONG).show();
        listView.setAdapter(adapter);
        return view;
    }

    class EditPersonalEducationDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AndroidNetworking.post("http://192.168.43.143:5050/editPersonalEducationDetails")
                    .addBodyParameter("customerNo", "A1028")
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

            AndroidNetworking.post("http://192.168.43.143:5050/profilePersonalDetails")
                    .addBodyParameter("customerNo", "A1028")
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

            AndroidNetworking.post("http://192.168.43.143:5050/profilePersonalDetails")
                    .addBodyParameter("customerNo", "A1028")
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
            AndroidNetworking.post("http://192.168.43.143:5050/editPersonalProfessionDetails")
                    .addBodyParameter("customerNo", "A1028")
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

            AndroidNetworking.post("http://192.168.43.143:5050/profileAdditionalDetails")
                    .addBodyParameter("customerNo", "A1028")
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
            AndroidNetworking.post("http://192.168.43.143:5050/editAdditionalDetailsAboutMe")
                    .addBodyParameter("customerNo", "A1028")
                    .addBodyParameter("aboutMe",am)
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

    class FetchAdditionalHobbiesDetails extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            AndroidNetworking.post("http://192.168.43.143:5050/profileAdditionalDetails")
                    .addBodyParameter("customerNo", "A1028")
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
            AndroidNetworking.post("http://192.168.43.143:5050/editAdditionalDetailsHobbies")
                    .addBodyParameter("customerNo", "A1028")
                    .addBodyParameter("hobbies",h)
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

            AndroidNetworking.post("http://192.168.43.143:5050/profileAdditionalDetails")
                    .addBodyParameter("customerNo", "A1028")
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
                                        Log.d(TAG, "onResponse: ---------eating----"+s);
                                    }
                                }

                                String[] drinkingArray = getResources().getStringArray(R.array.drinking_array);

                                for (String s : drinkingArray) {
                                    if (jsonArray.getString(3).equals(s)) {
                                        drinkingHabit.setSelection(Arrays.asList(drinkingArray).indexOf(s));
                                        Log.d(TAG, "onResponse: ---------drinking----"+s);

                                    }
                                }

                                String[] smokingArray = getResources().getStringArray(R.array.smoking_array);

                                for (String s : smokingArray) {
                                    if (jsonArray.getString(4).equals(s)) {
                                        smokingHabit.setSelection(Arrays.asList(smokingArray).indexOf(s));
                                        Log.d(TAG, "onResponse: ---------smoking----"+s);

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
            AndroidNetworking.post("http://192.168.43.143:5050/editAdditionalDetailsLifestyle")
                    .addBodyParameter("customerNo", "A1028")
                    .addBodyParameter("smokingHabit",sh)
                    .addBodyParameter("eatingHabit",eh)
                    .addBodyParameter("drinkingHabit",dh)
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

            AndroidNetworking.post("http://192.168.43.143:5050/profileAdditionalDetails")
                    .addBodyParameter("customerNo", "A1028")
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
            AndroidNetworking.post("http://192.168.43.143:5050/editAdditionalDetailsHoroscope")
                    .addBodyParameter("customerNo", "A1028")
                    .addBodyParameter("birthTime",bt)
                    .addBodyParameter("birthPlace",bp)
                    .addBodyParameter("gotra",g)
                    .addBodyParameter("manglik",m)
                    .addBodyParameter("matchHoroscope",mh)
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

            AndroidNetworking.post("http://192.168.43.143:5050/profileFamilyDetails")
                    .addBodyParameter("customerNo", "A1028")
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
            AndroidNetworking.post("http://192.168.43.143:5050/editFamilyDetailsRelation")
                    .addBodyParameter("customerNo", "A1028")
                    .addBodyParameter("relation",r)
                    .addBodyParameter("relativeName",rn)
                    .addBodyParameter("relativeOccupation",ro)
                    .addBodyParameter("relativeLocation",rl)
                    .addBodyParameter("relativeMobile",rm)
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


}

